package com.mapbefine.mapbefine.permission.application;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.permission.domain.Permission;
import com.mapbefine.mapbefine.permission.domain.PermissionId;
import com.mapbefine.mapbefine.permission.domain.PermissionRepository;
import com.mapbefine.mapbefine.permission.dto.request.PermissionRequest;
import com.mapbefine.mapbefine.permission.exception.PermissionException.PermissionBadRequestException;
import com.mapbefine.mapbefine.permission.exception.PermissionException.PermissionForbiddenException;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.mapbefine.mapbefine.permission.exception.PermissionErrorCode.*;

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

    public void addPermission(AuthMember authMember, PermissionRequest request) {
        validateGuest(authMember);
        Topic topic = findTopic(request.topicId());
        validateMemberCanTopicUpdate(authMember, topic);

        List<Member> members = findTargetMembers(request);
        List<Permission> permissions = createPermissions(authMember, topic, members);

        permissionRepository.saveAll(permissions);
    }

    private void validateGuest(AuthMember authMember) {
        if (Objects.isNull(authMember.getMemberId())) {
            throw new PermissionForbiddenException(FORBIDDEN_ADD_PERMISSION_GUEST);
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
                .map(member -> Permission.of(topic.getId(), member.getId()))
                .toList();
    }

    private boolean isNotWithPermission(Long topicId, Member member) {
        return !permissionRepository.existsByIdTopicIdAndIdMemberId(topicId, member.getId());
    }

    private boolean isNotSelfMember(AuthMember authMember, Member member) {
        return !Objects.equals(authMember.getMemberId(), member.getId());
    }

    private Topic findTopic(Long topicId) {
        return topicRepository.findById(topicId)
                .orElseThrow(() -> new PermissionBadRequestException(ILLEGAL_TOPIC_ID, topicId));
    }

    private void validateMemberCanTopicUpdate(AuthMember authMember, Topic topic) {
        if (authMember.canTopicUpdate(topic)) {
            return;
        }
        throw new PermissionForbiddenException(FORBIDDEN_ADD_PERMISSION);
    }

    private List<Member> findTargetMembers(PermissionRequest request) {
        return memberRepository.findAllById(request.memberIds());
    }

    public void deleteMemberTopicPermission(AuthMember authMember, Long memberId, Long topicId) {
        PermissionId permissionId = PermissionId.of(topicId, memberId);
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new PermissionBadRequestException(ILLEGAL_PERMISSION_ID, permissionId));

        Topic topic = findTopic(permission.getTopicId());
        validateMemberCanTopicUpdate(authMember, topic);

        permissionRepository.delete(permission);
    }

}
