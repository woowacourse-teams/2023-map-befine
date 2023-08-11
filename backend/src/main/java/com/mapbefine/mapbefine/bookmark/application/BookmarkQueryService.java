package com.mapbefine.mapbefine.bookmark.application;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.bookmark.domain.Bookmark;
import com.mapbefine.mapbefine.bookmark.domain.BookmarkRepository;
import com.mapbefine.mapbefine.bookmark.dto.response.BookmarkResponse;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.MemberTopicPermission;
import com.mapbefine.mapbefine.member.domain.MemberTopicPermissionRepository;
import com.mapbefine.mapbefine.member.dto.response.MemberTopicPermissionDetailResponse;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BookmarkQueryService {

    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;
    private final TopicRepository topicRepository;
    private final MemberTopicPermissionRepository memberTopicPermissionRepository;

    public BookmarkQueryService(
            BookmarkRepository bookmarkRepository,
            MemberRepository memberRepository,
            TopicRepository topicRepository,
            MemberTopicPermissionRepository memberTopicPermissionRepository
    ) {
        this.bookmarkRepository = bookmarkRepository;
        this.memberRepository = memberRepository;
        this.topicRepository = topicRepository;
        this.memberTopicPermissionRepository = memberTopicPermissionRepository;
    }

    public MemberTopicPermissionDetailResponse findMemberTopicPermissionById(Long permissionId) {
        MemberTopicPermission memberTopicPermission = memberTopicPermissionRepository.findById(
                        permissionId)
                .orElseThrow(NoSuchElementException::new);

        return MemberTopicPermissionDetailResponse.from(memberTopicPermission);
    }

    public List<BookmarkResponse> findAllTopicsInBookmark(AuthMember authMember) {
        validateNonExistsMember(authMember);
        List<Bookmark> bookmark =
                bookmarkRepository.findAllByMemberId(authMember.getMemberId());

        return bookmark.stream()
                .map(BookmarkResponse::from)
                .toList();
    }

    public void validateNonExistsMember(AuthMember authMember) {
        if (Objects.isNull(authMember.getMemberId())) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }
    }
}
