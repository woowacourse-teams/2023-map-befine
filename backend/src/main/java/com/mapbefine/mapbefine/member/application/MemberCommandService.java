package com.mapbefine.mapbefine.member.application;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.permission.domain.Permission;
import com.mapbefine.mapbefine.permission.domain.PermissionRepository;
import com.mapbefine.mapbefine.permission.dto.request.PermissionCreateRequest;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberCommandService {

    private final MemberRepository memberRepository;
    private final TopicRepository topicRepository;
    private final PermissionRepository permissionRepository;

    public MemberCommandService(
            MemberRepository memberRepository,
            TopicRepository topicRepository,
            PermissionRepository permissionRepository
    ) {
        this.memberRepository = memberRepository;
        this.topicRepository = topicRepository;
        this.permissionRepository = permissionRepository;
    }

    public Long saveMemberTopicPermission(AuthMember authMember, PermissionCreateRequest request) {
        Member member = memberRepository.findById(request.memberId())
                .orElseThrow(NoSuchElementException::new);
        Topic topic = topicRepository.findById(request.topicId())
                .orElseThrow(NoSuchElementException::new);

        validateSaveMemberTopicPermission(authMember, request, member, topic);

        Permission permission =
                Permission.createPermissionAssociatedWithTopicAndMember(topic, member);

        return permissionRepository.save(permission).getId();
    }

    private void validateSaveMemberTopicPermission(
            AuthMember authMember,
            PermissionCreateRequest request,
            Member member,
            Topic topic
    ) {
        validateMemberCanTopicUpdate(authMember, topic);
        validateSelfPermission(authMember, request);
        validateDuplicatePermission(topic.getId(), member.getId());
    }

    private void validateMemberCanTopicUpdate(AuthMember authMember, Topic topic) {
        if (authMember.canTopicUpdate(topic)) {
            return;
        }

        throw new IllegalArgumentException("해당 유저는 해당 토픽에서 다른 유저에게 권한을 줄 수 없습니다.");
    }

    private void validateSelfPermission(
            AuthMember authMember,
            PermissionCreateRequest request
    ) {
        if (authMember.getMemberId().equals(request.memberId())) {
            throw new IllegalArgumentException("본인에게 권한을 줄 수 없습니다.");
        }
    }

    private void validateDuplicatePermission(Long topicId, Long memberId) {
        if (permissionRepository.existsByTopicIdAndMemberId(topicId, memberId)) {
            throw new IllegalArgumentException("권한은 중복으로 줄 수 없습니다.");
        }
    }

    public void deleteMemberTopicPermission(AuthMember authMember, Long permissionId) {
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(NoSuchElementException::new);

        validateMemberCanTopicUpdate(authMember, permission.getTopic());

        permissionRepository.delete(permission);
    }

}
