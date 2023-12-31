package com.mapbefine.mapbefine.pin.application;

import static com.mapbefine.mapbefine.image.exception.ImageErrorCode.IMAGE_FILE_IS_NULL;
import static com.mapbefine.mapbefine.pin.exception.PinCommentErrorCode.FORBIDDEN_PIN_COMMENT_CREATE;
import static com.mapbefine.mapbefine.pin.exception.PinCommentErrorCode.FORBIDDEN_PIN_COMMENT_DELETE;
import static com.mapbefine.mapbefine.pin.exception.PinCommentErrorCode.FORBIDDEN_PIN_COMMENT_UPDATE;
import static com.mapbefine.mapbefine.pin.exception.PinCommentErrorCode.PIN_COMMENT_NOT_FOUND;
import static com.mapbefine.mapbefine.pin.exception.PinErrorCode.FORBIDDEN_PIN_CREATE_OR_UPDATE;
import static com.mapbefine.mapbefine.pin.exception.PinErrorCode.ILLEGAL_PIN_ID;
import static com.mapbefine.mapbefine.pin.exception.PinErrorCode.ILLEGAL_PIN_IMAGE_ID;
import static com.mapbefine.mapbefine.topic.exception.TopicErrorCode.ILLEGAL_TOPIC_ID;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.image.application.ImageService;
import com.mapbefine.mapbefine.image.exception.ImageException.ImageBadRequestException;
import com.mapbefine.mapbefine.location.domain.Address;
import com.mapbefine.mapbefine.location.domain.Coordinate;
import com.mapbefine.mapbefine.location.domain.Location;
import com.mapbefine.mapbefine.location.domain.LocationRepository;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.domain.PinComment;
import com.mapbefine.mapbefine.pin.domain.PinCommentRepository;
import com.mapbefine.mapbefine.pin.domain.PinImage;
import com.mapbefine.mapbefine.pin.domain.PinImageRepository;
import com.mapbefine.mapbefine.pin.domain.PinRepository;
import com.mapbefine.mapbefine.pin.dto.request.PinCommentCreateRequest;
import com.mapbefine.mapbefine.pin.dto.request.PinCommentUpdateRequest;
import com.mapbefine.mapbefine.pin.dto.request.PinCreateRequest;
import com.mapbefine.mapbefine.pin.dto.request.PinImageCreateRequest;
import com.mapbefine.mapbefine.pin.dto.request.PinUpdateRequest;
import com.mapbefine.mapbefine.pin.event.PinUpdateEvent;
import com.mapbefine.mapbefine.pin.exception.PinCommentException.PinCommentForbiddenException;
import com.mapbefine.mapbefine.pin.exception.PinCommentException.PinCommentNotFoundException;
import com.mapbefine.mapbefine.pin.exception.PinException.PinBadRequestException;
import com.mapbefine.mapbefine.pin.exception.PinException.PinForbiddenException;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import com.mapbefine.mapbefine.topic.exception.TopicException.TopicBadRequestException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Transactional
@Service
public class PinCommandService {

    private static final double DUPLICATE_LOCATION_DISTANCE_METERS = 10.0;

    private final ApplicationEventPublisher eventPublisher;
    private final ImageService imageService;
    private final PinRepository pinRepository;
    private final LocationRepository locationRepository;
    private final TopicRepository topicRepository;
    private final MemberRepository memberRepository;
    private final PinImageRepository pinImageRepository;
    private final PinCommentRepository pinCommentRepository;

    public PinCommandService(
            ApplicationEventPublisher eventPublisher,
            ImageService imageService,
            PinRepository pinRepository,
            LocationRepository locationRepository,
            TopicRepository topicRepository,
            MemberRepository memberRepository,
            PinImageRepository pinImageRepository,
            PinCommentRepository pinCommentRepository
    ) {
        this.imageService = imageService;
        this.eventPublisher = eventPublisher;
        this.pinRepository = pinRepository;
        this.locationRepository = locationRepository;
        this.topicRepository = topicRepository;
        this.memberRepository = memberRepository;
        this.pinImageRepository = pinImageRepository;
        this.pinCommentRepository = pinCommentRepository;
    }

    public long save(
            AuthMember authMember,
            List<MultipartFile> images,
            PinCreateRequest request
    ) {
        Topic topic = findTopic(request.topicId());
        validatePinCreateOrUpdate(authMember, topic);

        Member member = findMember(authMember.getMemberId());
        Pin pin = Pin.createPinAssociatedWithLocationAndTopicAndMember(
                request.name(),
                request.description(),
                findDuplicateOrCreatePinLocation(request),
                topic,
                member
        );

        addPinImagesToPin(images, pin);
        pinRepository.save(pin);
        eventPublisher.publishEvent(new PinUpdateEvent(pin, member));

        return pin.getId();
    }

    private void addPinImagesToPin(final List<MultipartFile> images, final Pin pin) {
        if (Objects.isNull(images)) {
            return;
        }

        images.forEach(image -> addImageToPin(image, pin));
    }

    private Topic findTopic(Long topicId) {
        if (Objects.isNull(topicId)) {
            throw new TopicBadRequestException(ILLEGAL_TOPIC_ID);
        }
        return topicRepository.findById(topicId)
                .orElseThrow(() -> new TopicBadRequestException(ILLEGAL_TOPIC_ID));
    }

    private Member findMember(Long memberId) {
        if (Objects.isNull(memberId)) {
            throw new PinForbiddenException(FORBIDDEN_PIN_CREATE_OR_UPDATE);
        }
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("findMember; memberId= " + memberId));
    }

    private Location findDuplicateOrCreatePinLocation(PinCreateRequest request) {
        Coordinate coordinate = Coordinate.of(request.latitude(), request.longitude());

        return locationRepository.findAllByCoordinateAndDistanceInMeters(
                        coordinate.getCoordinate(), DUPLICATE_LOCATION_DISTANCE_METERS
                )
                .stream()
                .filter(location -> location.isSameAddress(request.address()))
                .findFirst()
                .orElseGet(() -> saveLocation(request, coordinate));
    }

    private Location saveLocation(PinCreateRequest pinCreateRequest, Coordinate coordinate) {
        Address address = new Address(
                pinCreateRequest.address(),
                pinCreateRequest.address(),
                pinCreateRequest.legalDongCode()
        );
        Location location = new Location(address, coordinate);

        return locationRepository.save(location);
    }

    public void update(
            AuthMember authMember,
            Long pinId,
            PinUpdateRequest request
    ) {
        Member member = findMember(authMember.getMemberId());
        Pin pin = findPin(pinId);
        validatePinCreateOrUpdate(authMember, pin.getTopic());

        pin.updatePinInfo(request.name(), request.description());
        eventPublisher.publishEvent(new PinUpdateEvent(pin, member));
    }

    private Pin findPin(Long pinId) {
        return pinRepository.findById(pinId)
                .orElseThrow(() -> new PinBadRequestException(ILLEGAL_PIN_ID));
    }

    public void removeById(AuthMember authMember, Long pinId) {
        Pin pin = findPin(pinId);
        validatePinCreateOrUpdate(authMember, pin.getTopic());

        pinRepository.deleteById(pinId);
        pinImageRepository.deleteAllByPinId(pinId);
    }

    public void addImage(AuthMember authMember, PinImageCreateRequest request) {
        Pin pin = findPin(request.pinId());
        validatePinCreateOrUpdate(authMember, pin.getTopic());
        addImageToPin(request.image(), pin);
    }

    private void addImageToPin(MultipartFile image, Pin pin) {
        if (Objects.isNull(image)) {
            throw new ImageBadRequestException(IMAGE_FILE_IS_NULL);
        }

        String imageUrl = imageService.upload(image);
        PinImage.createPinImageAssociatedWithPin(imageUrl, pin);
    }

    public void removeImageById(AuthMember authMember, Long pinImageId) {
        PinImage pinImage = findPinImage(pinImageId);
        Pin pin = pinImage.getPin();
        validatePinCreateOrUpdate(authMember, pin.getTopic());

        pinImageRepository.deleteById(pinImageId);
    }

    private PinImage findPinImage(Long pinImageId) {
        return pinImageRepository.findById(pinImageId)
                .orElseThrow(() -> new PinBadRequestException(ILLEGAL_PIN_IMAGE_ID));
    }

    private void validatePinCreateOrUpdate(AuthMember authMember, Topic topic) {
        if (authMember.canPinCreateOrUpdate(topic)) {
            return;
        }

        throw new PinForbiddenException(FORBIDDEN_PIN_CREATE_OR_UPDATE);
    }

    public Long savePinComment(AuthMember authMember, PinCommentCreateRequest request) {
        Pin pin = findPin(request.pinId());
        validatePinCommentCreate(authMember, pin);
        Member member = findMember(authMember.getMemberId());
        PinComment pinComment = createPinComment(
                pin,
                member,
                request.parentPinCommentId(),
                request.content()
        );
        pinCommentRepository.save(pinComment);

        return pinComment.getId();
    }

    private void validatePinCommentCreate(AuthMember authMember, Pin pin) {
        if (authMember.canPinCommentCreate(pin.getTopic())) {
            return;
        }

        throw new PinCommentForbiddenException(FORBIDDEN_PIN_COMMENT_CREATE);
    }

    private PinComment createPinComment(
            Pin pin,
            Member member,
            Long parentPinCommentId,
            String content
    ) {
        if (Objects.isNull(parentPinCommentId)) {
            return PinComment.ofParentPinComment(pin, member, content);
        }

        PinComment parentPinComment = findPinComment(parentPinCommentId);

        return PinComment.ofChildPinComment(pin, parentPinComment, member, content);
    }

    public void updatePinComment(
            AuthMember member,
            Long pinCommentId,
            PinCommentUpdateRequest request
    ) {
        PinComment pinComment = findPinComment(pinCommentId);

        validatePinCommentUpdate(member, pinComment);

        pinComment.updateContent(request.content());
    }

    private void validatePinCommentUpdate(AuthMember authMember, PinComment pinComment) {
        if (isPinCommentCreatorOrAdmin(authMember, pinComment)) {
            return;
        }

        throw new PinCommentForbiddenException(FORBIDDEN_PIN_COMMENT_UPDATE);
    }

    private boolean isPinCommentCreatorOrAdmin(AuthMember authMember, PinComment pinComment) {
        Long creatorId = pinComment.getCreator().getId();

        return authMember.isSameMember(creatorId) || authMember.isAdmin();
    }

    private PinComment findPinComment(Long pinCommentId) {
        return pinCommentRepository.findById(pinCommentId)
                .orElseThrow(() -> new PinCommentNotFoundException(PIN_COMMENT_NOT_FOUND, pinCommentId));
    }

    public void deletePinComment(AuthMember member, Long pinCommentId) {
        PinComment pinComment = findPinComment(pinCommentId);

        validatePinCommentDelete(member, pinComment);

        pinCommentRepository.delete(pinComment);
    }

    private void validatePinCommentDelete(AuthMember authMember, PinComment pinComment) {
        if (isPinCommentCreatorOrAdmin(authMember, pinComment)) {
            return;
        }

        throw new PinCommentForbiddenException(FORBIDDEN_PIN_COMMENT_DELETE);
    }

}
