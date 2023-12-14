package com.mapbefine.mapbefine.member.application;

import com.mapbefine.mapbefine.atlas.domain.Atlas;
import com.mapbefine.mapbefine.atlas.domain.AtlasRepository;
import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.bookmark.domain.Bookmark;
import com.mapbefine.mapbefine.bookmark.domain.BookmarkId;
import com.mapbefine.mapbefine.bookmark.domain.BookmarkRepository;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.dto.response.MemberDetailResponse;
import com.mapbefine.mapbefine.member.dto.response.MemberResponse;
import com.mapbefine.mapbefine.member.exception.MemberErrorCode;
import com.mapbefine.mapbefine.member.exception.MemberException.MemberNotFoundException;
import com.mapbefine.mapbefine.pin.dto.response.PinResponse;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MemberQueryService {

    private final MemberRepository memberRepository;
    private final AtlasRepository atlasRepository;
    private final BookmarkRepository bookmarkRepository;
    private final TopicRepository topicRepository;

    public MemberQueryService(
            MemberRepository memberRepository,
            AtlasRepository atlasRepository,
            BookmarkRepository bookmarkRepository,
            TopicRepository topicRepository
    ) {
        this.memberRepository = memberRepository;
        this.atlasRepository = atlasRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.topicRepository = topicRepository;
    }

    public MemberDetailResponse findMemberDetail(AuthMember authMember) {
        Member member = findMemberById(authMember.getMemberId());

        return MemberDetailResponse.from(member);
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .filter(Member::isNormalStatus)
                .orElseThrow(() -> new MemberNotFoundException(MemberErrorCode.MEMBER_NOT_FOUND, id));
    }

    public List<MemberResponse> findAll() {
        return memberRepository.findAll()
                .stream()
                .filter(Member::isNormalStatus)
                .map(MemberResponse::from)
                .toList();
    }

    public List<TopicResponse> findAllTopicsInBookmark(AuthMember authMember) {
        List<Topic> bookMarkedTopics = findTopicsInBookmark(authMember);

        return bookMarkedTopics.stream()
                .map(topic -> TopicResponse.from(
                        topic,
                        isInAtlas(authMember.getMemberId(), topic.getId()),
                        true
                ))
                .toList();
    }

    private List<Topic> findTopicsInBookmark(AuthMember authMember) {
        return bookmarkRepository.findAllByIdMemberId(authMember.getMemberId())
                .stream()
                .map(Bookmark::getTopic)
                .toList();
    }

    private boolean isInAtlas(Long memberId, Long topicId) {
        return atlasRepository.existsByMemberIdAndTopicId(memberId, topicId);
    }

    public List<TopicResponse> findAllTopicsInAtlas(AuthMember authMember) {
        Member member = findMemberById(authMember.getMemberId());
        List<Topic> topicsInAtlas = findTopicsInAtlas(member);

        return topicsInAtlas.stream()
                .map(topic -> TopicResponse.from(
                        topic,
                        true,
                        isInBookmark(authMember.getMemberId(), topic.getId())
                ))
                .toList();
    }

    private List<Topic> findTopicsInAtlas(Member member) {
        return member.getAtlantes()
                .stream()
                .map(Atlas::getTopic)
                .toList();
    }

    private boolean isInBookmark(Long memberId, Long topicId) {
        return bookmarkRepository.existsById(BookmarkId.of(topicId, memberId));
    }

    public List<TopicResponse> findMyAllTopics(AuthMember authMember) {
        Long memberId = authMember.getMemberId();
        List<Topic> createdTopics = topicRepository.findAllByCreatorId(memberId);

        return createdTopics
                .stream()
                .map(topic -> TopicResponse.from(
                        topic,
                        isInAtlas(memberId, topic.getId()),
                        isInBookmark(memberId, topic.getId())
                )).toList();
    }

    public List<PinResponse> findMyAllPins(AuthMember authMember) {
        Member member = findMemberById(authMember.getMemberId());

        return member.getCreatedPins()
                .stream()
                .map(PinResponse::from)
                .toList();
    }

}
