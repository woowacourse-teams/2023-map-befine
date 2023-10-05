package com.mapbefine.mapbefine.member.application;

import com.mapbefine.mapbefine.atlas.domain.Atlas;
import com.mapbefine.mapbefine.atlas.domain.AtlasRepository;
import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.bookmark.domain.BookmarkRepository;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.dto.response.MemberDetailResponse;
import com.mapbefine.mapbefine.member.dto.response.MemberResponse;
import com.mapbefine.mapbefine.member.exception.MemberErrorCode;
import com.mapbefine.mapbefine.member.exception.MemberException.MemberForbiddenException;
import com.mapbefine.mapbefine.member.exception.MemberException.MemberNotFoundException;
import com.mapbefine.mapbefine.pin.dto.response.PinResponse;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public MemberDetailResponse findById(AuthMember authMember, Long id) {
        if (authMember.isSameMember(id)) {
            Member member = findMemberById(id);
            return MemberDetailResponse.from(member);
        }
        throw new MemberForbiddenException(MemberErrorCode.FORBIDDEN_ACCESS, id);
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
        Member member = findMemberById(authMember.getMemberId());

        List<Topic> bookMarkedTopics = topicRepository.findTopicsByBookmarksMemberId(
                authMember.getMemberId());
        return bookMarkedTopics.stream()
                .map(topic -> TopicResponse.from(
                        topic,
                        isInAtlas(member.getId(), topic.getId()),
                        true
                ))
                .toList();
    }

    private List<Topic> findTopicsInAtlas(Member member) {
        return member.getAtlantes()
                .stream()
                .map(Atlas::getTopic)
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

    private boolean isInBookmark(Long memberId, Long topicId) {
        return bookmarkRepository.existsByMemberIdAndTopicId(memberId, topicId);
    }

    public List<TopicResponse> findMyAllTopics(AuthMember authMember) {
        Long memberId = authMember.getMemberId();
        Member member = findMemberById(memberId);

        return member.getCreatedTopics()
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
