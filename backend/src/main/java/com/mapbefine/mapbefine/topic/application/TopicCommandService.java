package com.mapbefine.mapbefine.topic.application;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.pin.Domain.Pin;
import com.mapbefine.mapbefine.pin.Domain.PinRepository;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import com.mapbefine.mapbefine.topic.dto.request.TopicCreateRequest;
import com.mapbefine.mapbefine.topic.dto.request.TopicMergeRequest;
import com.mapbefine.mapbefine.topic.dto.request.TopicUpdateRequest;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TopicCommandService {

    private final TopicRepository topicRepository;
    private final PinRepository pinRepository;
    private final MemberRepository memberRepository;

    public TopicCommandService(
            TopicRepository topicRepository,
            PinRepository pinRepository,
            MemberRepository memberRepository
    ) {
        this.topicRepository = topicRepository;
        this.pinRepository = pinRepository;
        this.memberRepository = memberRepository;
    }

    public long createNew(AuthMember member, TopicCreateRequest request) {
        Topic topic = createNewTopic(member, request);

        List<Long> pinIds = request.pins();
        List<Pin> original = pinRepository.findAllById(pinIds);

        validateExist(pinIds.size(), original.size());
        pinRepository.saveAll(copyPins(original, topic));

        return topic.getId();
    }

    public long createMerge(AuthMember member, TopicMergeRequest request) {
        List<Long> topicIds = request.topics();
        List<Topic> topics = findTopicsByIds(member, topicIds);

        validateExist(topicIds.size(), topics.size());
        Topic topic = createMergeTopic(member, request);

        List<Pin> original = getPinFromTopics(topics);
        pinRepository.saveAll(copyPins(original, topic));

        return topic.getId();
    }

    public void updateTopicInfo(
            AuthMember member,
            Long id,
            TopicUpdateRequest request
    ) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Topic입니다."));
        member.canTopicUpdate(topic);

        topic.updateTopicInfo(
                request.name(),
                request.description(),
                request.image()
        );
    }

    public void delete(AuthMember member, Long id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Topic입니다."));
        member.canDelete(topic);

        pinRepository.deleteAllByTopicId(id);
        topicRepository.deleteById(id);
    }

    private List<Topic> findTopicsByIds(AuthMember member, List<Long> topicIds) {
        return topicRepository.findAllById(topicIds)
                .stream()
                .filter(member::canRead)
                .toList();
    }

    private List<Pin> getPinFromTopics(List<Topic> topics) {
        return topics.stream()
                .map(Topic::getPins)
                .flatMap(Collection::stream)
                .toList();
    }

    private Topic createMergeTopic(AuthMember member, TopicMergeRequest request) {
        Member creator = findCreatorByAuthMember(member);
        Topic topic = Topic.of(
                request.name(),
                request.description(),
                request.image(),
                request.publicity(),
                request.permission(),
                creator
        );
        return topicRepository.save(topic);

    }

    private Topic createNewTopic(AuthMember member, TopicCreateRequest request) {
        Member creator = findCreatorByAuthMember(member);
        Topic topic = Topic.of(
                request.name(),
                request.description(),
                request.image(),
                request.publicity(),
                request.permission(),
                creator
        );
        return topicRepository.save(topic);
    }

    private Member findCreatorByAuthMember(AuthMember member) {
        return memberRepository.findById(member.getMemberId())
                .orElseThrow(NoSuchElementException::new);
    }

    private void validateExist(int idCount, int existCount) {
        if (idCount != existCount) {
            throw new IllegalArgumentException("찾을 수 없는 ID가 포함되어 있습니다.");
        }
    }

    private List<Pin> copyPins(List<Pin> pins, Topic topic) {
        return pins.stream()
                .map(original -> original.copy(topic))
                .toList();
    }

}