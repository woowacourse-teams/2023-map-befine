package com.mapbefine.mapbefine.member.application;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.MemberTopicBookmark;
import com.mapbefine.mapbefine.member.domain.MemberTopicBookmarkRepository;
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

    private final MemberTopicBookmarkRepository memberTopicBookmarkRepository;

    public MemberCommandService(
            MemberRepository memberRepository,
            TopicRepository topicRepository,
            MemberTopicPermissionRepository memberTopicPermissionRepository,
            MemberTopicBookmarkRepository memberTopicBookmarkRepository
    ) {
        this.memberRepository = memberRepository;
        this.topicRepository = topicRepository;
        this.memberTopicPermissionRepository = memberTopicPermissionRepository;
        this.memberTopicBookmarkRepository = memberTopicBookmarkRepository;
    }

    public Long save(MemberCreateRequest request) {
        validateUniqueNickName(request.nickName());
        validateUniqueEmail(request.email());

        Member member = Member.of(
                request.nickName(),
                request.email(),
                request.imageUrl(),
                request.role()
        );

        return memberRepository.save(member)
                .getId();
    }

    private void validateUniqueNickName(String nickName) {
        if (memberRepository.existsByMemberInfoNickName(nickName)) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }
    }

    private void validateUniqueEmail(String email) {
        if (memberRepository.existsByMemberInfoEmail(email)) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
    }

    public Long saveMemberTopicPermission(AuthMember authMember,
            MemberTopicPermissionCreateRequest request) {
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
            MemberTopicPermissionCreateRequest request
    ) {
        if (authMember.getMemberId().equals(request.memberId())) {
            throw new IllegalArgumentException("본인에게 권한을 줄 수 없습니다.");
        }
    }

    private void validateDuplicatePermission(Long topicId, Long memberId) {
        if (memberTopicPermissionRepository.existsByTopicIdAndMemberId(topicId, memberId)) {
            throw new IllegalArgumentException("권한은 중복으로 줄 수 없습니다.");
        }
    }

    public void deleteMemberTopicPermission(AuthMember authMember, Long permissionId) {
        MemberTopicPermission memberTopicPermission = memberTopicPermissionRepository.findById(
                        permissionId)
                .orElseThrow(NoSuchElementException::new);

        validateMemberCanTopicUpdate(authMember, memberTopicPermission.getTopic());

        memberTopicPermissionRepository.delete(memberTopicPermission);
    }

    public Long addTopicInBookmark(AuthMember authMember, Long topicId) {
        Topic topic = getTopicById(topicId);
        validateBookmarkingPermission(authMember, topic);
        Member member = getMemberById(authMember);

        MemberTopicBookmark memberTopicBookmark
                = MemberTopicBookmark.createWithAssociatedTopicAndMember(topic, member);
        memberTopicBookmarkRepository.save(memberTopicBookmark);

        return memberTopicBookmark.getId();
    }

    private Member getMemberById(AuthMember authMember) {
        return memberRepository.findById(authMember.getMemberId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 멤버입니다."));
    }

    private Topic getTopicById(Long topicId) {
        return topicRepository.findById(topicId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 토픽입니다."));
    }

    private void validateBookmarkingPermission(AuthMember authMember, Topic topic) {
        if (authMember.canRead(topic)) {
            return;
        }

        throw new IllegalArgumentException("토픽에 대한 권한이 없어서 즐겨찾기에 추가할 수 없습니다.");
    }

    public void deleteTopicInBookmark(AuthMember authMember, Long bookmarkId) {
        validateBookmarkDeletingPermission(authMember, bookmarkId);

        memberTopicBookmarkRepository.deleteById(bookmarkId);

    }

    private void validateBookmarkDeletingPermission(AuthMember authMember, Long bookmarkId) {
        boolean canDelete = memberTopicBookmarkRepository.existsByIdAndMemberId(
                authMember.getMemberId(),
                bookmarkId
        );

        if (canDelete) {
            return;
        }

        throw new IllegalArgumentException("즐겨찾기 삭제에 대한 권한이 없습니다.");
    }

    public void deleteAllBookmarks(AuthMember authMember) {
        memberTopicBookmarkRepository.deleteAllByMemberId(authMember.getMemberId());
    }

}
