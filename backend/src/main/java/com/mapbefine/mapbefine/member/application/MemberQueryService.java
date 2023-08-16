package com.mapbefine.mapbefine.member.application;

import com.mapbefine.mapbefine.atlas.domain.Atlas;
import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.bookmark.domain.Bookmark;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.dto.response.MemberDetailResponse;
import com.mapbefine.mapbefine.member.dto.response.MemberResponse;
import com.mapbefine.mapbefine.pin.dto.response.PinResponse;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberQueryService {

    private final MemberRepository memberRepository;

    public MemberQueryService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberDetailResponse findById(Long id) {
        Member member = findMemberById(id);

        return MemberDetailResponse.from(member);
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    public List<MemberResponse> findAll() {
        return memberRepository.findAll()
                .stream()
                .map(MemberResponse::from)
                .toList();
    }

    public List<TopicResponse> findAllTopicsInBookmark(AuthMember authMember) {
        Member member = findMemberById(authMember.getMemberId());

        List<Topic> bookMarkedTopics = findBookMarkedTopics(member);
        List<Topic> topicsInAtlas = findTopicsInAtlas(member);

        return bookMarkedTopics.stream()
                .map(topic -> TopicResponse.from(
                        topic,
                        isInAtlas(topicsInAtlas, topic),
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

    private List<Topic> findBookMarkedTopics(Member member) {
        return member.getBookmarks()
                .stream()
                .map(Bookmark::getTopic)
                .toList();
    }

    private boolean isInAtlas(List<Topic> topicsInAtlas, Topic topic) {
        return topicsInAtlas.contains(topic);
    }

    public List<TopicResponse> findAllTopicsInAtlas(AuthMember authMember) {
        Member member = findMemberById(authMember.getMemberId());

        List<Topic> bookMarkedTopics = findBookMarkedTopics(member);
        List<Topic> topicsInAtlas = findTopicsInAtlas(member);

        return topicsInAtlas.stream()
                .map(topic -> TopicResponse.from(
                        topic,
                        true,
                        isBookMarked(bookMarkedTopics, topic)
                ))
                .toList();
    }

    private boolean isBookMarked(List<Topic> bookMarkedTopics, Topic topic) {
        return bookMarkedTopics.contains(topic);
    }

    public List<TopicResponse> findMyAllTopics(AuthMember authMember) {
        Member member = findMemberById(authMember.getMemberId());

        List<Topic> bookMarkedTopics = findBookMarkedTopics(member);
        List<Topic> topicsInAtlas = findTopicsInAtlas(member);

        return member.getCreatedTopics()
                .stream()
                .map(topic -> TopicResponse.from(
                        topic,
                        isInAtlas(topicsInAtlas, topic),
                        isBookMarked(bookMarkedTopics, topic)
                ))
                .toList();
    }

    public List<PinResponse> findMyAllPins(AuthMember authMember) {
        Member member = findMemberById(authMember.getMemberId());

        return member.getCreatedPins()
                .stream()
                .map(PinResponse::from)
                .toList();
    }

}
