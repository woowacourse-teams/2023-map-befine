package com.mapbefine.mapbefine.member.application;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.MemberTopicPermission;
import com.mapbefine.mapbefine.member.domain.MemberTopicPermissionRepository;
import com.mapbefine.mapbefine.member.dto.request.MemberCreateRequest;
import com.mapbefine.mapbefine.member.dto.request.MemberTopicPermissionCreateRequest;
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
    private final MemberTopicPermissionRepository memberTopicPermissionRepository;

    public MemberCommandService(
            MemberRepository memberRepository,
            TopicRepository topicRepository,
            MemberTopicPermissionRepository memberTopicPermissionRepository
    ) {
        this.memberRepository = memberRepository;
        this.topicRepository = topicRepository;
        this.memberTopicPermissionRepository = memberTopicPermissionRepository;
    }

    public Long save(MemberCreateRequest request) {
        Member member = Member.of(
                request.name(),
                request.email(),
                request.imageUrl(),
                request.role()
        );

        return memberRepository.save(member)
                .getId();
    }

    public Long saveMemberTopicPermission(
            AuthMember authMember,
            MemberTopicPermissionCreateRequest request
    ) {
        Member member = memberRepository.findById(request.memberId())
                .orElseThrow(NoSuchElementException::new);
        Topic topic = topicRepository.findById(request.topicId())
                .orElseThrow(NoSuchElementException::new);

        validateSaveMemberTopicPermission(authMember, request, member, topic);

        MemberTopicPermission memberTopicPermission =
                MemberTopicPermission.createPermissionAssociatedWithTopicAndMember(topic, member);
        return memberTopicPermissionRepository.save(memberTopicPermission).getId();
    }

    private void validateSaveMemberTopicPermission(
            AuthMember authMember,
            MemberTopicPermissionCreateRequest request,
            Member member,
            Topic topic
    ) {
        validateMemberCanTopicUpdate(authMember, topic);
        validateSelfPermission(authMember, request);
        validateDuplicatePermission(member, topic);
    }

    private void validateMemberCanTopicUpdate(AuthMember authMember, Topic topic) {
        if (!authMember.canTopicUpdate(topic)) {
            throw new IllegalArgumentException("해당 유저는 해당 토픽에서 다른 유저에게 권한을 줄 수 없습니다.");
        }
    }

    private void validateSelfPermission(
            AuthMember authMember,
            MemberTopicPermissionCreateRequest request
    ) {
        if (authMember.getMemberId().equals(request.memberId())) {
            throw new IllegalArgumentException("본인에게 권한을 줄 수 없습니다.");
        }
    }

    private void validateDuplicatePermission(Member member, Topic topic) {
        if (memberTopicPermissionRepository.existsByTopicAndMember(topic, member)) {
            throw new IllegalArgumentException("권한은 중복으로 줄 수 없습니다.");
        }
    }

    public void deleteMemberTopicPermission(
            AuthMember authMember,
            Long permissionId
    ) {
        MemberTopicPermission memberTopicPermission = memberTopicPermissionRepository.findById(permissionId)
                .orElseThrow(NoSuchElementException::new);

        validateMemberCanTopicUpdate(authMember, memberTopicPermission.getTopic());

        memberTopicPermissionRepository.delete(memberTopicPermission);
    }

}
