package com.mapbefine.mapbefine.atlas.application;

import static com.mapbefine.mapbefine.atlas.exception.AtlasErrorCode.FORBIDDEN_TOPIC_ADD;
import static com.mapbefine.mapbefine.atlas.exception.AtlasErrorCode.FORBIDDEN_TOPIC_READ;
import static com.mapbefine.mapbefine.atlas.exception.AtlasErrorCode.ILLEGAL_TOPIC_ID;

import com.mapbefine.mapbefine.atlas.domain.Atlas;
import com.mapbefine.mapbefine.atlas.domain.AtlasRepository;
import com.mapbefine.mapbefine.atlas.exception.AtlasException.AtlasBadRequestException;
import com.mapbefine.mapbefine.atlas.exception.AtlasException.AtlasForbiddenException;
import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@Transactional
public class AtlasCommandService {

    private final TopicRepository topicRepository;
    private final MemberRepository memberRepository;
    private final AtlasRepository atlasRepository;

    public AtlasCommandService(
            TopicRepository topicRepository,
            MemberRepository memberRepository,
            AtlasRepository atlasRepository
    ) {
        this.topicRepository = topicRepository;
        this.memberRepository = memberRepository;
        this.atlasRepository = atlasRepository;
    }

    public void addTopic(AuthMember authMember, Long topicId) {
        Long memberId = authMember.getMemberId();

        validateMember(memberId);
        validateTopic(topicId);

        if (isTopicAlreadyAdded(topicId, memberId)) {
            return;
        }

        Topic topic = findTopicById(topicId);
        validateReadPermission(authMember, topic);

        Member member = findMemberById(memberId);

        Atlas atlas = Atlas.createWithAssociatedMember(topic, member);
        atlasRepository.save(atlas);
    }

    private void validateMember(Long memberId) {
        if (Objects.isNull(memberId)) {
            throw new AtlasForbiddenException(FORBIDDEN_TOPIC_ADD);
        }
    }

    private void validateTopic(Long topicId) {
        if (Objects.isNull(topicId)) {
            throw new AtlasBadRequestException(ILLEGAL_TOPIC_ID);
        }
    }

    private boolean isTopicAlreadyAdded(Long topicId, Long memberId) {
        return atlasRepository.existsByMemberIdAndTopicId(memberId, topicId);
    }

    private Topic findTopicById(Long topicId) {
        return topicRepository.findByIdAndIsDeletedFalse(topicId)
                .orElseThrow(() -> new AtlasBadRequestException(ILLEGAL_TOPIC_ID));
    }

    private void validateReadPermission(AuthMember authMember, Topic topic) {
        if (authMember.canRead(topic)) {
            return;
        }
        throw new AtlasForbiddenException(FORBIDDEN_TOPIC_READ);
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("findMemberById; memberId=" + memberId));
    }

    public void removeTopic(AuthMember authMember, Long topicId) {
        atlasRepository.deleteByMemberIdAndTopicId(authMember.getMemberId(), topicId);
    }

}
