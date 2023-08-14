package com.mapbefine.mapbefine.topic.application;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.domain.PinRepository;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import com.mapbefine.mapbefine.topic.dto.request.TopicCreateRequest;
import com.mapbefine.mapbefine.topic.dto.request.TopicMergeRequest;
import com.mapbefine.mapbefine.topic.dto.request.TopicUpdateRequest;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
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

    public Long saveTopic(AuthMember member, TopicCreateRequest request) {
        Topic topic = convertToTopic(member, request);
        List<Long> pinIds = request.pins();

        if (pinIds.size() > 0) {
            copyPinsToTopic(member, topic, pinIds);
        }

        topicRepository.save(topic);

        return topic.getId();
    }

    private Topic convertToTopic(AuthMember member, TopicCreateRequest request) {
        Member creator = findCreatorByAuthMember(member);

        return Topic.createTopicAssociatedWithCreator(
                request.name(),
                request.description(),
                request.image(),
                request.publicity(),
                request.permissionType(),
                creator
        );
    }

    private Member findCreatorByAuthMember(AuthMember member) {
        if (Objects.isNull(member.getMemberId())) {
            throw new IllegalArgumentException("Guest는 토픽을 생성할 수 없습니다.");
        }

        return memberRepository.findById(member.getMemberId())
                .orElseThrow(NoSuchElementException::new);
    }

    private void copyPinsToTopic(
            AuthMember member,
            Topic topic,
            List<Long> pinIds
    ) {
        List<Pin> originalPins = findAllPins(pinIds);
        validateCopyablePins(member, originalPins);

        Member creator = findCreatorByAuthMember(member);

        originalPins.forEach(pin -> pin.copyToTopic(creator, topic));
    }

    private List<Pin> findAllPins(List<Long> pinIds) {
        List<Pin> findPins = pinRepository.findAllById(pinIds);

        if (pinIds.size() != findPins.size()) {
            throw new IllegalArgumentException("존재하지 않는 핀 Id가 존재합니다.");
        }

        return findPins;
    }

    private void validateCopyablePins(AuthMember member, List<Pin> originalPins) {
        int copyablePinCount = (int) originalPins.stream()
                .filter(pin -> member.canRead(pin.getTopic()))
                .count();

        if (copyablePinCount != originalPins.size()) {
            throw new IllegalArgumentException("복사할 수 없는 pin이 존재합니다.");
        }
    }

    public Long merge(AuthMember member, TopicMergeRequest request) {
        Topic topic = convertToTopic(member, request);
        List<Topic> originalTopics = findAllTopics(request.topics());

        validateCopyableTopics(member, originalTopics);

        Member creator = findCreatorByAuthMember(member);
        List<Pin> originalPins = getAllPinsFromTopics(originalTopics);
        originalPins.forEach(pin -> pin.copyToTopic(creator, topic));

        topicRepository.save(topic);

        return topic.getId();
    }

    private Topic convertToTopic(AuthMember member, TopicMergeRequest request) {
        Member creator = findCreatorByAuthMember(member);

        return Topic.createTopicAssociatedWithCreator(
                request.name(),
                request.description(),
                request.image(),
                request.publicity(),
                request.permissionType(),
                creator
        );
    }

    private List<Topic> findAllTopics(List<Long> topicIds) {
        List<Topic> findTopics = topicRepository.findAllById(topicIds);

        if (topicIds.size() != findTopics.size()) {
            throw new IllegalArgumentException("존재하지 않는 토픽 Id가 존재합니다.");
        }

        return findTopics;
    }

    private void validateCopyableTopics(AuthMember member, List<Topic> originalTopics) {
        int copyablePinCount = (int) originalTopics.stream()
                .filter(member::canRead)
                .count();

        if (copyablePinCount != originalTopics.size()) {
            throw new IllegalArgumentException("복사할 수 없는 토픽이 존재합니다.");
        }
    }

    private List<Pin> getAllPinsFromTopics(List<Topic> topics) {
        return topics.stream()
                .map(Topic::getPins)
                .flatMap(Collection::stream)
                .toList();
    }

    public void updateTopicInfo(
            AuthMember member,
            Long topicId,
            TopicUpdateRequest request
    ) {
        Topic topic = findTopic(topicId);

        validateUpdateAuth(member, topic);

        topic.updateTopicInfo(request.name(), request.description(), request.image());
        topic.updateTopicStatus(request.publicity(), request.permissionType());
    }

    private Topic findTopic(Long topicId) {
        return topicRepository.findById(topicId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Topic입니다."));
    }

    private void validateUpdateAuth(AuthMember member, Topic topic) {
        if (member.canTopicUpdate(topic)) {
            return;
        }

        throw new IllegalArgumentException("업데이트 권한이 없습니다.");
    }

    public void delete(AuthMember member, Long topicId) {
        Topic topic = findTopic(topicId);

        validateDeleteAuth(member, topic);

        pinRepository.deleteAllByTopicId(topicId);
        topicRepository.deleteById(topicId);
    }

    private void validateDeleteAuth(AuthMember member, Topic topic) {
        if (member.canDelete(topic)) {
            return;
        }

        throw new IllegalArgumentException("삭제 권한이 없습니다.");
    }

}
