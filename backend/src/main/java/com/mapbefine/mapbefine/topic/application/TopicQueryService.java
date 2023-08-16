package com.mapbefine.mapbefine.topic.application;

import com.mapbefine.mapbefine.atlas.domain.Atlas;
import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.bookmark.domain.Bookmark;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import com.mapbefine.mapbefine.topic.dto.response.TopicDetailResponse;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TopicQueryService {

    private final TopicRepository topicRepository;
    private final MemberRepository memberRepository;

    public TopicQueryService(TopicRepository topicRepository, MemberRepository memberRepository) {
        this.topicRepository = topicRepository;
        this.memberRepository = memberRepository;
    }

    public List<TopicResponse> findAllReadable(AuthMember authMember) {
        if (Objects.isNull(authMember.getMemberId())) {
            return getGuestTopicResponses(authMember);
        }
        return getUserTopicResponses(authMember);
    }

    private List<TopicResponse> getGuestTopicResponses(AuthMember authMember) {
        return topicRepository.findAll()
                .stream()
                .filter(authMember::canRead)
                .map(topic -> TopicResponse.from(topic, Boolean.FALSE, Boolean.FALSE))
                .toList();
    }

    private List<TopicResponse> getUserTopicResponses(AuthMember authMember) {
        Member member = findMemberById(authMember.getMemberId());

        List<Topic> topicsInAtlas = findTopicsInAtlas(member);
        List<Topic> topicsInBookMark = findBookMarkedTopics(member);

        return topicRepository.findAll().stream()
                .filter(authMember::canRead)
                .map(topic -> TopicResponse.from(
                        topic,
                        isInAtlas(topicsInAtlas, topic),
                        isBookMarked(topicsInBookMark, topic)
                ))
                .toList();
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
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

    private boolean isBookMarked(List<Topic> bookMarkedTopics, Topic topic) {
        return bookMarkedTopics.contains(topic);
    }


    public TopicDetailResponse findDetailById(AuthMember authMember, Long topicId) {
        Topic topic = findTopic(topicId);
        validateReadableTopic(authMember, topic);

        if (Objects.isNull(authMember.getMemberId())) {
            return TopicDetailResponse.from(topic, Boolean.FALSE, Boolean.FALSE);
        }

        Member member = findMemberById(authMember.getMemberId());

        List<Topic> topicsInAtlas = findTopicsInAtlas(member);
        List<Topic> topicsInBookMark = findBookMarkedTopics(member);

        return TopicDetailResponse.from(
                topic,
                isInAtlas(topicsInAtlas, topic),
                isBookMarked(topicsInBookMark, topic)
        );
    }

    private Topic findTopic(Long id) {
        return topicRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 Topic이 존재하지 않습니다."));
    }

    private void validateReadableTopic(AuthMember member, Topic topic) {
        if (member.canRead(topic)) {
            return;
        }

        throw new IllegalArgumentException("조회권한이 없는 Topic 입니다.");

    }

    public List<TopicDetailResponse> findDetailsByIds(AuthMember authMember, List<Long> ids) {
        List<Topic> topics = topicRepository.findByIdIn(ids);

        validateTopicsCount(ids, topics);
        validateReadableTopics(authMember, topics);

        if (Objects.isNull(authMember.getMemberId())) {
            return getGuestTopicDetailResponses(topics);
        }

        return getUserTopicDetailResponses(authMember, topics);
    }

    private static List<TopicDetailResponse> getGuestTopicDetailResponses(List<Topic> topics) {
        return topics.stream()
                .map(topic -> TopicDetailResponse.from(topic, Boolean.FALSE, Boolean.FALSE))
                .toList();
    }

    private List<TopicDetailResponse> getUserTopicDetailResponses(AuthMember authMember, List<Topic> topics) {
        Member member = findMemberById(authMember.getMemberId());

        List<Topic> topicsInAtlas = findTopicsInAtlas(member);
        List<Topic> topicsInBookMark = findBookMarkedTopics(member);

        return topics.stream()
                .map(topic -> TopicDetailResponse.from(
                        topic,
                        isInAtlas(topicsInAtlas, topic),
                        isBookMarked(topicsInBookMark, topic)
                ))
                .toList();
    }

    private void validateTopicsCount(List<Long> topicIds, List<Topic> topics) {
        if (topicIds.size() != topics.size()) {
            throw new IllegalArgumentException("존재하지 않는 토픽이 존재합니다");
        }
    }

    private void validateReadableTopics(AuthMember member, List<Topic> topics) {
        int readableCount = (int) topics.stream()
                .filter(member::canRead)
                .count();

        if (topics.size() != readableCount) {
            throw new IllegalArgumentException("읽을 수 없는 토픽이 존재합니다.");
        }
    }

    public List<TopicResponse> findAllTopicsByMemberId(AuthMember authMember, Long memberId) {

        if (Objects.isNull(authMember.getMemberId())) {
            return topicRepository.findByCreatorId(memberId)
                    .stream()
                    .filter(authMember::canRead)
                    .map(topic -> TopicResponse.from(topic, Boolean.FALSE, Boolean.FALSE))
                    .toList();
        }

        Member member = findMemberById(authMember.getMemberId());

        List<Topic> topicsInAtlas = findTopicsInAtlas(member);
        List<Topic> topicsInBookMark = findBookMarkedTopics(member);

        return topicRepository.findByCreatorId(memberId)
                .stream()
                .filter(authMember::canRead)
                .map(topic -> TopicResponse.from(
                        topic,
                        isInAtlas(topicsInAtlas, topic),
                        isBookMarked(topicsInBookMark, topic))
                ).toList();
    }

    public List<TopicResponse> findAllBestTopics(AuthMember authMember) {
        Member member = findMemberById(authMember.getMemberId());

        List<Topic> topicsInAtlas = findTopicsInAtlas(member);
        List<Topic> topicsInBookMark = findBookMarkedTopics(member);

        return topicRepository.findAll()
                .stream()
                .filter(authMember::canRead)
                .sorted(Comparator.comparing(Topic::countBookmarks).reversed())
                .map(topic -> TopicResponse.from(
                        topic,
                        isInAtlas(topicsInAtlas, topic),
                        isBookMarked((topicsInBookMark), topic))
                ).toList();
    }

}
