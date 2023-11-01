package com.mapbefine.mapbefine.topic.application;

import static com.mapbefine.mapbefine.pin.exception.PinErrorCode.FORBIDDEN_PIN_CREATE_OR_UPDATE;
import static com.mapbefine.mapbefine.pin.exception.PinErrorCode.ILLEGAL_PIN_ID;
import static com.mapbefine.mapbefine.topic.exception.TopicErrorCode.FORBIDDEN_TOPIC_CREATE;
import static com.mapbefine.mapbefine.topic.exception.TopicErrorCode.FORBIDDEN_TOPIC_DELETE;
import static com.mapbefine.mapbefine.topic.exception.TopicErrorCode.FORBIDDEN_TOPIC_UPDATE;
import static com.mapbefine.mapbefine.topic.exception.TopicErrorCode.ILLEGAL_TOPIC_ID;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.image.application.ImageService;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.domain.PinRepository;
import com.mapbefine.mapbefine.pin.exception.PinException.PinBadRequestException;
import com.mapbefine.mapbefine.pin.exception.PinException.PinForbiddenException;
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
import org.springframework.web.multipart.MultipartFile;

@Transactional
@Service
public class TopicCommandService {

    private final TopicRepository topicRepository;
    private final PinRepository pinRepository;
    private final MemberRepository memberRepository;
    private final ImageService imageService;

    public TopicCommandService(
            TopicRepository topicRepository,
            PinRepository pinRepository,
            MemberRepository memberRepository,
            ImageService imageService
    ) {
        this.topicRepository = topicRepository;
        this.pinRepository = pinRepository;
        this.memberRepository = memberRepository;
        this.imageService = imageService;
    }

    public Long saveTopic(AuthMember member, TopicCreateRequest request) {
        Topic topic = convertToTopic(member, request);
        List<Long> pinIds = request.pins();

        topicRepository.save(topic);
        topicRepository.flush();
        if (!pinIds.isEmpty()) {
            copyPinsToTopic(member, topic, pinIds);
        }

        return topic.getId();
    }

    private Topic convertToTopic(AuthMember member, TopicCreateRequest request) {
        Member creator = findCreatorByAuthMember(member);
        String image = createImageUrl(request.image());

        return Topic.createTopicAssociatedWithCreator(
                request.name(),
                request.description(),
                image,
                request.publicity(),
                request.permissionType(),
                creator
        );
    }

    private String createImageUrl(MultipartFile image) {
        if (Objects.isNull(image)) {
            return null;
        }

        return imageService.upload(image);
    }

    private Member findCreatorByAuthMember(AuthMember member) {
        Long memberId = member.getMemberId();
        if (Objects.isNull(memberId)) {
            throw new TopicForbiddenException(FORBIDDEN_TOPIC_CREATE);
        }

        return memberRepository.findById(memberId)
                .orElseThrow(
                        () -> new NoSuchElementException("findCreatorByAuthMember; member not found; id=" + memberId)
                );
    }

    private void copyPinsToTopic(
            AuthMember member,
            Topic topic,
            List<Long> pinIds
    ) {
        List<Pin> originalPins = findAllPins(pinIds);
        validateCopyablePins(member, originalPins);

        pinRepository.saveAllToTopic(topic, originalPins);
    }

    private List<Pin> findAllPins(List<Long> pinIds) {
        List<Pin> findPins = pinRepository.findAllByIdIn(pinIds);

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

        List<Pin> originalPins = getAllPinsFromTopics(originalTopics);

        topicRepository.save(topic);
        pinRepository.saveAllToTopic(topic, originalPins);
        return topic.getId();
    }

    private Topic convertToTopic(AuthMember member, TopicMergeRequest request) {
        Member creator = findCreatorByAuthMember(member);
        String imageUrl = createImageUrl(request.image());

        return Topic.createTopicAssociatedWithCreator(
                request.name(),
                request.description(),
                imageUrl,
                request.publicity(),
                request.permissionType(),
                creator
        );
    }

    private List<Topic> findAllTopics(List<Long> topicIds) {
        List<Topic> findTopics = topicRepository.findByIdIn(topicIds);

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

        topic.updateTopicInfo(request.name(), request.description());
        topic.updateTopicStatus(request.publicity(), request.permissionType());
    }

    private void validateUpdateAuth(AuthMember member, Topic topic) {
        if (member.canTopicUpdate(topic)) {
            return;
        }
        throw new TopicForbiddenException(FORBIDDEN_TOPIC_UPDATE);
    }

    @Deprecated(since = "2023.10.06")
    public void delete(AuthMember member, Long topicId) {
        Topic topic = findTopic(topicId);
        validateDeleteAuth(member, topic);

        /// TODO: 2023/10/06 연관관계 다 삭제해야 하는데, 관리자 API와 중복 로직이며 관리자 API에서만 사용됨
        pinRepository.deleteAllByTopicId(topicId);
        topicRepository.deleteById(topicId);
    }

    @Deprecated(since = "2023.10.06")
    private void validateDeleteAuth(AuthMember member, Topic topic) {
        if (member.canDelete(topic)) {
            return;
        }
        throw new TopicForbiddenException(FORBIDDEN_TOPIC_DELETE);
    }

    public void updateTopicImage(AuthMember member, Long topicId, MultipartFile image) {
        Topic topic = findTopic(topicId);

        validateUpdateAuth(member, topic);

        String imageUrl = imageService.upload(image);
        topic.updateTopicImageUrl(imageUrl);
    }

}
