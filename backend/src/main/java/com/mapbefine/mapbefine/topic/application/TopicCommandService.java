package com.mapbefine.mapbefine.topic.application;

import static com.mapbefine.mapbefine.pin.exception.PinErrorCode.FORBIDDEN_PIN_CREATE_OR_UPDATE;
import static com.mapbefine.mapbefine.pin.exception.PinErrorCode.ILLEGAL_PIN_ID;
import static com.mapbefine.mapbefine.topic.exception.TopicErrorCode.FORBIDDEN_TOPIC_CREATE;
import static com.mapbefine.mapbefine.topic.exception.TopicErrorCode.FORBIDDEN_TOPIC_DELETE;
import static com.mapbefine.mapbefine.topic.exception.TopicErrorCode.FORBIDDEN_TOPIC_UPDATE;
import static com.mapbefine.mapbefine.topic.exception.TopicErrorCode.ILLEGAL_TOPIC_ID;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.domain.PinRepository;
import com.mapbefine.mapbefine.pin.exception.PinException.PinBadRequestException;
import com.mapbefine.mapbefine.pin.exception.PinException.PinForbiddenException;
import com.mapbefine.mapbefine.s3.application.S3Service;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import com.mapbefine.mapbefine.topic.dto.request.TopicCreateRequest;
import com.mapbefine.mapbefine.topic.dto.request.TopicMergeRequest;
import com.mapbefine.mapbefine.topic.dto.request.TopicUpdateRequest;
import com.mapbefine.mapbefine.topic.exception.TopicException.TopicBadRequestException;
import com.mapbefine.mapbefine.topic.exception.TopicException.TopicForbiddenException;
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
    private final S3Service s3Service;

    public TopicCommandService(
            TopicRepository topicRepository,
            PinRepository pinRepository,
            MemberRepository memberRepository,
            S3Service s3Service
    ) {
        this.topicRepository = topicRepository;
        this.pinRepository = pinRepository;
        this.memberRepository = memberRepository;
        this.s3Service = s3Service;
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
        String image = s3Service.upload(request.image());

        return Topic.createTopicAssociatedWithCreator(
                request.name(),
                request.description(),
                image,
                request.publicity(),
                request.permissionType(),
                creator
        );
    }

    private Member findCreatorByAuthMember(AuthMember member) {
        Long memberId = member.getMemberId();
        if (Objects.isNull(memberId)) {
            throw new TopicForbiddenException(FORBIDDEN_TOPIC_CREATE);
        }

        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("findCreatorByAuthMember; member not found; id=" + memberId));
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
            throw new PinBadRequestException(ILLEGAL_PIN_ID);
        }

        return findPins;
    }

    private void validateCopyablePins(AuthMember member, List<Pin> originalPins) {
        int copyablePinCount = (int) originalPins.stream()
                .filter(pin -> member.canRead(pin.getTopic()))
                .count();

        if (copyablePinCount != originalPins.size()) {
            throw new PinBadRequestException(ILLEGAL_PIN_ID);
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
            throw new TopicBadRequestException(ILLEGAL_TOPIC_ID);
        }

        return findTopics;
    }

    private void validateCopyableTopics(AuthMember member, List<Topic> originalTopics) {
        int copyablePinCount = (int) originalTopics.stream()
                .filter(member::canRead)
                .count();

        if (copyablePinCount != originalTopics.size()) {
            throw new TopicBadRequestException(ILLEGAL_TOPIC_ID);
        }
    }

    private List<Pin> getAllPinsFromTopics(List<Topic> topics) {
        return topics.stream()
                .map(Topic::getPins)
                .flatMap(Collection::stream)
                .toList();
    }

    public void copyPin(AuthMember member, Long topicId, List<Long> pinIds) {
        Topic topic = findTopic(topicId);
        validatePinCreateOrUpdateAuthInTopic(member, topic);

        validateCopyPinSelected(pinIds);

        copyPinsToTopic(member, topic, pinIds);
    }

    private Topic findTopic(Long topicId) {
        return topicRepository.findById(topicId)
                .orElseThrow(() -> new TopicBadRequestException(ILLEGAL_TOPIC_ID));
    }

    private void validatePinCreateOrUpdateAuthInTopic(AuthMember member, Topic topic) {
        if (member.canPinCreateOrUpdate(topic)) {
            return;
        }
        throw new PinForbiddenException(FORBIDDEN_PIN_CREATE_OR_UPDATE);
    }

    private void validateCopyPinSelected(List<Long> pinIds) {
        if (pinIds.isEmpty()) {
            throw new PinBadRequestException(ILLEGAL_PIN_ID);
        }
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

    private void validateUpdateAuth(AuthMember member, Topic topic) {
        if (member.canTopicUpdate(topic)) {
            return;
        }
        throw new TopicForbiddenException(FORBIDDEN_TOPIC_UPDATE);
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
        throw new TopicForbiddenException(FORBIDDEN_TOPIC_DELETE);
    }

}
