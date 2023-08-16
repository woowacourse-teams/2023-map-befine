package com.mapbefine.mapbefine.atlas.application;

import com.mapbefine.mapbefine.atlas.domain.Atlas;
import com.mapbefine.mapbefine.atlas.domain.AtlasRepository;
import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import java.util.NoSuchElementException;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        if (Objects.isNull(memberId) || Objects.isNull(topicId)) {
            throw new IllegalArgumentException("잘못된 ID가 입력되었습니다.");
        }

        if (isTopicAlreadyAdded(topicId, memberId)) {
            return;
        }

        Topic topic = findTopicById(topicId);
        validateReadPermission(authMember, topic);

        Member member = findMemberById(memberId);

        Atlas atlas = Atlas.createWithAssociatedMember(topic, member);
        atlasRepository.save(atlas);
    }

    private boolean isTopicAlreadyAdded(Long topicId, Long memberId) {
        return atlasRepository.existsByMemberIdAndTopicId(memberId, topicId);
    }

    private Topic findTopicById(Long topicId) {
        return topicRepository.findById(topicId)
                .orElseThrow(NoSuchElementException::new);
    }

    private void validateReadPermission(AuthMember authMember, Topic topic) {
        if (authMember.canRead(topic)) {
            return;
        }
        throw new IllegalArgumentException("해당 지도에 접근권한이 없습니다.");
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(NoSuchElementException::new);
    }

    public void removeTopic(AuthMember authMember, Long topicId) {
        atlasRepository.deleteByMemberIdAndTopicId(authMember.getMemberId(), topicId);
    }

}
