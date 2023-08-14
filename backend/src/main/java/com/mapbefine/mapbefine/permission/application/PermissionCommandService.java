package com.mapbefine.mapbefine.permission.application;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.permission.domain.Permission;
import com.mapbefine.mapbefine.permission.domain.PermissionRepository;
import com.mapbefine.mapbefine.permission.dto.request.PermissionRequest;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import java.util.NoSuchElementException;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PermissionCommandService {

    private final MemberRepository memberRepository;
    private final TopicRepository topicRepository;
    private final PermissionRepository permissionRepository;

    public PermissionCommandService(
            MemberRepository memberRepository,
            TopicRepository topicRepository,
            PermissionRepository permissionRepository
    ) {
        this.memberRepository = memberRepository;
        this.topicRepository = topicRepository;
        this.permissionRepository = permissionRepository;
    }

    public Long savePermission(AuthMember authMember, PermissionRequest request) {
        validateNull(authMember);
        validateSelfPermission(authMember, request);
        validateDuplicatePermission(request);

        Topic topic = getTopic(request);
        validateMemberCanTopicUpdate(authMember, topic);

        Member member = getMember(request);
        Permission permission =
                Permission.createPermissionAssociatedWithTopicAndMember(topic, member);

        return permissionRepository.save(permission).getId();
    }

    private void validateNull(AuthMember authMember) {
        if (Objects.isNull(authMember.getMemberId())) {
            throw new IllegalArgumentException();
        }
    }

    private void validateSelfPermission(AuthMember authMember, PermissionRequest request) {
        Long sourceMemberId = authMember.getMemberId();
        Long targetMemberId = request.memberId();

        if (sourceMemberId.equals(targetMemberId)) {
            throw new IllegalArgumentException("본인에게 권한을 줄 수 없습니다.");
        }
    }

    private void validateDuplicatePermission(PermissionRequest request) {
        if (permissionRepository.existsByTopicIdAndMemberId(request.topicId(), request.memberId())) {
            throw new IllegalArgumentException("권한은 중복으로 줄 수 없습니다.");
        }
    }

    private Topic getTopic(PermissionRequest request) {
        return topicRepository.findById(request.topicId())
                .orElseThrow(NoSuchElementException::new);
    }

    private void validateMemberCanTopicUpdate(AuthMember authMember, Topic topic) {
        if (authMember.canTopicUpdate(topic)) {
            return;
        }
        throw new IllegalArgumentException("해당 지도에서 다른 유저에게 권한을 줄 수 없습니다.");
    }

    private Member getMember(PermissionRequest request) {
        return memberRepository.findById(request.memberId())
                .orElseThrow(NoSuchElementException::new);
    }

    public void deleteMemberTopicPermission(AuthMember authMember, Long permissionId) {
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(NoSuchElementException::new);

        validateMemberCanTopicUpdate(authMember, permission.getTopic());

        permissionRepository.delete(permission);
    }

}
