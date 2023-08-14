package com.mapbefine.mapbefine.member.application;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.permission.domain.Permission;
import com.mapbefine.mapbefine.permission.domain.PermissionRepository;
import com.mapbefine.mapbefine.member.dto.response.MemberDetailResponse;
import com.mapbefine.mapbefine.member.dto.response.MemberResponse;
import com.mapbefine.mapbefine.permission.dto.response.PermissionDetailResponse;
import com.mapbefine.mapbefine.permission.dto.response.PermissionResponse;
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
    private final PermissionRepository permissionRepository;

    public MemberQueryService(
            MemberRepository memberRepository,
            TopicRepository topicRepository,
            PinRepository pinRepository,
            PermissionRepository permissionRepository
    ) {
        this.memberRepository = memberRepository;
        this.topicRepository = topicRepository;
        this.pinRepository = pinRepository;
        this.permissionRepository = permissionRepository;
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

        return topicsByCreator.stream()
                .map(TopicResponse::from)
                .toList();
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

    public List<PermissionResponse> findAllWithPermission(Long topicId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(NoSuchElementException::new);

        return permissionRepository.findAllByTopic(topic)
                .stream()
                .map(PermissionResponse::from)
                .toList();
    }

    public PermissionDetailResponse findMemberTopicPermissionById(Long permissionId) {
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(NoSuchElementException::new);

        return PermissionDetailResponse.from(permission);
    }

}
