package com.mapbefine.mapbefine.pin.application;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.location.domain.Address;
import com.mapbefine.mapbefine.location.domain.Coordinate;
import com.mapbefine.mapbefine.location.domain.Location;
import com.mapbefine.mapbefine.location.domain.LocationRepository;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.domain.PinImage;
import com.mapbefine.mapbefine.pin.domain.PinImageRepository;
import com.mapbefine.mapbefine.pin.domain.PinRepository;
import com.mapbefine.mapbefine.pin.dto.request.PinCreateRequest;
import com.mapbefine.mapbefine.pin.dto.request.PinImageCreateRequest;
import com.mapbefine.mapbefine.pin.dto.request.PinUpdateRequest;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import java.util.NoSuchElementException;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class PinCommandService {

    private static final double DUPLICATE_LOCATION_DISTANCE_METERS = 10.0;

    private final PinRepository pinRepository;
    private final LocationRepository locationRepository;
    private final TopicRepository topicRepository;
    private final MemberRepository memberRepository;
    private final PinImageRepository pinImageRepository;

    public PinCommandService(
            PinRepository pinRepository,
            LocationRepository locationRepository,
            TopicRepository topicRepository,
            MemberRepository memberRepository,
            PinImageRepository pinImageRepository
    ) {
        this.pinRepository = pinRepository;
        this.locationRepository = locationRepository;
        this.topicRepository = topicRepository;
        this.memberRepository = memberRepository;
        this.pinImageRepository = pinImageRepository;
    }


    public long save(AuthMember authMember, PinCreateRequest request) {
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
        pinRepository.save(pin);

        return pin.getId();
    }

    private Topic findTopic(Long topicId) {
        if (Objects.isNull(topicId)) {
            throw new NoSuchElementException("토픽을 찾을 수 없습니다.");
        }
        return topicRepository.findById(topicId)
                .orElseThrow(() -> new NoSuchElementException("토픽을 찾을 수 없습니다."));
    }

    private Member findMember(Long memberId) {
        if (Objects.isNull(memberId)) {
            throw new NoSuchElementException("회원 정보를 찾을 수 없습니다.");
        }
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("회원 정보를 찾을 수 없습니다."));
    }

    private Location findDuplicateOrCreatePinLocation(PinCreateRequest request) {
        Coordinate coordinate = Coordinate.of(request.latitude(), request.longitude());

        return locationRepository.findAllByCoordinateAndDistanceInMeters(coordinate,
                        DUPLICATE_LOCATION_DISTANCE_METERS)
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
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 핀입니다."));
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

        PinImage pinImage = PinImage.createPinImageAssociatedWithPin(request.imageUrl(), pin);
        pinImageRepository.save(pinImage);
    }

    public void removeImageById(AuthMember authMember, Long pinImageId) {
        PinImage pinImage = findPinImage(pinImageId);
        Pin pin = pinImage.getPin();
        validatePinCreateOrUpdate(authMember, pin.getTopic());

        pinImageRepository.deleteById(pinImageId);
    }

    private PinImage findPinImage(Long pinImageId) {
        return pinImageRepository.findById(pinImageId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 핀 이미지입니다."));
    }

    private void validatePinCreateOrUpdate(AuthMember authMember, Topic topic) {
        if (authMember.canPinCreateOrUpdate(topic)) {
            return;
        }

        throw new IllegalArgumentException("핀 생성 및 수정 권한이 없습니다.");
    }
}
