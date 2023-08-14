package com.mapbefine.mapbefine.member.application;

import com.mapbefine.mapbefine.atlas.domain.Atlas;
import com.mapbefine.mapbefine.atlas.domain.AtlasRepository;
import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.MemberTopicPermission;
import com.mapbefine.mapbefine.member.domain.MemberTopicPermissionRepository;
import com.mapbefine.mapbefine.member.dto.response.MemberDetailResponse;
import com.mapbefine.mapbefine.member.dto.response.MemberResponse;
import com.mapbefine.mapbefine.member.dto.response.MemberTopicPermissionDetailResponse;
import com.mapbefine.mapbefine.member.dto.response.MemberTopicPermissionResponse;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.domain.PinRepository;
import com.mapbefine.mapbefine.pin.dto.response.PinResponse;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberQueryService {

    private final MemberRepository memberRepository;
    private final TopicRepository topicRepository;
    private final PinRepository pinRepository;
    private final AtlasRepository atlasRepository;
    private final MemberTopicPermissionRepository memberTopicPermissionRepository;

    public MemberQueryService(
            MemberRepository memberRepository,
            TopicRepository topicRepository,
            PinRepository pinRepository,
            AtlasRepository atlasRepository,
            MemberTopicPermissionRepository memberTopicPermissionRepository
    ) {
        this.memberRepository = memberRepository;
        this.topicRepository = topicRepository;
        this.pinRepository = pinRepository;
        this.atlasRepository = atlasRepository;
        this.memberTopicPermissionRepository = memberTopicPermissionRepository;
    }

    public MemberDetailResponse findById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);

        return MemberDetailResponse.from(member);
    }

    public List<MemberResponse> findAll() {
        return memberRepository.findAll()
                .stream()
                .map(MemberResponse::from)
                .toList();
    }

    public List<TopicResponse> findTopicsByMember(AuthMember authMember) {
        validateNonExistsMember(authMember.getMemberId());
        List<Topic> topicsByCreator = topicRepository.findByCreatorId(authMember.getMemberId());

        List<Topic> topicsInAtlas = getTopicsInAtlas(authMember);

        return topicsByCreator.stream()
                .map(topic -> TopicResponse.from(topic, isInAtlas(topicsInAtlas, topic)))
                .toList();
    }

    private List<Topic> getTopicsInAtlas(AuthMember authMember) {
        return atlasRepository.findAllByMemberId(authMember.getMemberId())
                .stream()
                .map(Atlas::getTopic)
                .toList();
    }

    private boolean isInAtlas(List<Topic> topicsInAtlas, Topic topic) {
        return topicsInAtlas.contains(topic);
    }

    public List<PinResponse> findPinsByMember(AuthMember authMember) {
        validateNonExistsMember(authMember.getMemberId());
        List<Pin> pinsByCreator = pinRepository.findByCreatorId(authMember.getMemberId());

        return pinsByCreator.stream()
                .map(PinResponse::from)
                .toList();
    }

    public void validateNonExistsMember(Long memberId) {
        if (Objects.isNull(memberId)) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }
    }

    public List<MemberTopicPermissionResponse> findAllWithPermission(Long topicId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(NoSuchElementException::new);

        return memberTopicPermissionRepository.findAllByTopic(topic)
                .stream()
                .map(MemberTopicPermissionResponse::from)
                .toList();
    }

    public MemberTopicPermissionDetailResponse findMemberTopicPermissionById(Long permissionId) {
        MemberTopicPermission memberTopicPermission = memberTopicPermissionRepository.findById(permissionId)
                .orElseThrow(NoSuchElementException::new);

        return MemberTopicPermissionDetailResponse.from(memberTopicPermission);
    }

}
