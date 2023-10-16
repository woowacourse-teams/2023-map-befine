package com.mapbefine.mapbefine.pin.application;

import static com.mapbefine.mapbefine.image.exception.ImageErrorCode.IMAGE_FILE_IS_NULL;
import static com.mapbefine.mapbefine.pin.exception.PinCommentErrorCode.FORBIDDEN_PIN_COMMENT_CREATE;
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
import com.mapbefine.mapbefine.pin.dto.request.PinCreateRequest;
import com.mapbefine.mapbefine.pin.dto.request.PinImageCreateRequest;
import com.mapbefine.mapbefine.pin.dto.request.PinUpdateRequest;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Transactional
@Service
public class PinCommandService {

    private static final double DUPLICATE_LOCATION_DISTANCE_METERS = 10.0;

    private final PinRepository pinRepository;
    private final LocationRepository locationRepository;
    private final TopicRepository topicRepository;
    private final MemberRepository memberRepository;
    private final PinImageRepository pinImageRepository;
    private final ImageService imageService;
    private final PinCommentRepository pinCommentRepository;

    public PinCommandService(
            PinRepository pinRepository,
            LocationRepository locationRepository,
            TopicRepository topicRepository,
            MemberRepository memberRepository,
            PinImageRepository pinImageRepository,
            ImageService imageService,
            PinCommentRepository pinCommentRepository
    ) {
        this.pinRepository = pinRepository;
        this.locationRepository = locationRepository;
        this.topicRepository = topicRepository;
        this.memberRepository = memberRepository;
        this.pinImageRepository = pinImageRepository;
        this.imageService = imageService;
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
        Pin pin = findPin(pinId);
        validatePinCreateOrUpdate(authMember, pin.getTopic());

        pin.updatePinInfo(request.name(), request.description());
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

    public Long addComment(AuthMember authMember, PinCommentCreateRequest request) {
        Pin pin = findPin(request.pinId());
        validatePinCommentCreate(authMember, pin);
        Member member = findMember(authMember.getMemberId());
        PinComment parentPinComment = findPinComment(request.pinId());

        PinComment pinComment = PinComment.of(pin, parentPinComment, member, request.content());
        pinCommentRepository.save(pinComment);

        return pinComment.getId();
    }

    private void validatePinCommentCreate(AuthMember authMember, Pin pin) {
        if (authMember.canPinCommentCreate(pin.getTopic())) {
            return;
        }

        throw new PinCommentForbiddenException(FORBIDDEN_PIN_COMMENT_CREATE);
    }

    private PinComment findPinComment(Long pinCommentId) {
        if (Objects.isNull(pinCommentId)) {
            return null;
        }

        return pinCommentRepository.findById(pinCommentId)
                .orElseThrow(() -> new PinCommentNotFoundException(PIN_COMMENT_NOT_FOUND, pinCommentId));
    }

}
