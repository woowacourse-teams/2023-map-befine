package com.mapbefine.mapbefine.permission.application;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.permission.domain.Permission;
import com.mapbefine.mapbefine.permission.domain.PermissionRepository;
import com.mapbefine.mapbefine.permission.dto.request.PermissionRequest;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import java.util.List;
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

    public void savePermission(AuthMember authMember, PermissionRequest request) {
        validateGuest(authMember);
        Topic topic = findTopic(request);
        validateMemberCanTopicUpdate(authMember, topic);

        List<Member> members = findTargetMembers(request);
        List<Permission> permissions = createPermissions(authMember, topic, members);

        permissionRepository.saveAll(permissions);
    }

    private void validateGuest(AuthMember authMember) {
        if (Objects.isNull(authMember.getMemberId())) {
            throw new IllegalArgumentException("Guest는 권한을 줄 수 없습니다.");
        }
    }

    private List<Permission> createPermissions(
            AuthMember authMember,
            Topic topic,
            List<Member> members
    ) {
        return members.stream()
                .filter(member -> isNotSelfMember(authMember, member))
                .filter(member -> isNotWithPermission(topic.getId(), member))
                .map(member -> Permission.createPermissionAssociatedWithTopicAndMember(topic, member))
                .toList();
    }

    private boolean isNotWithPermission(Long topicId, Member member) {
        return !permissionRepository.existsByTopicIdAndMemberId(topicId, member.getId());
    }

    private boolean isNotSelfMember(AuthMember authMember, Member member) {
        return !Objects.equals(authMember.getMemberId(), member.getId());
    }

    private Topic findTopic(PermissionRequest request) {
        return topicRepository.findById(request.topicId())
                .orElseThrow(NoSuchElementException::new);
    }

    private void validateMemberCanTopicUpdate(AuthMember authMember, Topic topic) {
        if (authMember.canTopicUpdate(topic)) {
            return;
        }
        throw new IllegalArgumentException("해당 지도에서 다른 유저에게 권한을 줄 수 없습니다.");
    }

    private List<Member> findTargetMembers(PermissionRequest request) {
        return memberRepository.findAllById(request.memberIds());
    }

    public void deleteMemberTopicPermission(AuthMember authMember, Long permissionId) {
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(NoSuchElementException::new);

        validateMemberCanTopicUpdate(authMember, permission.getTopic());

        permissionRepository.delete(permission);
    }

}
