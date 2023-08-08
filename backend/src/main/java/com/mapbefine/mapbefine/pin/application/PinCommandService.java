package com.mapbefine.mapbefine.pin.application;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.common.entity.Image;
import com.mapbefine.mapbefine.location.domain.Address;
import com.mapbefine.mapbefine.location.domain.Coordinate;
import com.mapbefine.mapbefine.location.domain.Location;
import com.mapbefine.mapbefine.location.domain.LocationRepository;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.domain.PinImage;
import com.mapbefine.mapbefine.pin.domain.PinImageRepository;
import com.mapbefine.mapbefine.pin.domain.PinInfo;
import com.mapbefine.mapbefine.pin.domain.PinRepository;
import com.mapbefine.mapbefine.pin.dto.request.PinCreateRequest;
import com.mapbefine.mapbefine.pin.dto.request.PinImageCreateRequest;
import com.mapbefine.mapbefine.pin.dto.request.PinUpdateRequest;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import java.util.NoSuchElementException;
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

    // TODO 예외처리 패턴 추상화
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
        Topic topic = topicRepository.findById(request.topicId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 토픽입니다."));

        if (authMember.canPinCreateOrUpdate(topic)) {
            Member member = memberRepository.findById(authMember.getMemberId())
                    .orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다."));
            Pin pin = Pin.createPinAssociatedWithLocationAndTopicAndMember(
                    PinInfo.of(request.name(), request.description()),
                    findDuplicateOrCreatePinLocation(request),
                    topic,
                    member
            );
            pinRepository.save(pin);

            return pin.getId();
        }
        throw new IllegalArgumentException("해당 토픽의 핀을 생성할 권한이 없습니다.");
    }

    private Location findDuplicateOrCreatePinLocation(PinCreateRequest request) {
        Coordinate coordinate = Coordinate.of(request.latitude(), request.longitude());

        return locationRepository.findAllByCoordinateAndDistanceInMeters(coordinate, DUPLICATE_LOCATION_DISTANCE_METERS)
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

        return locationRepository.save(new Location(address, coordinate));
    }

    public void update(
            AuthMember authMember,
            Long pinId,
            PinUpdateRequest request
    ) {
        Pin pin = pinRepository.findById(pinId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 핀입니다."));

        if (authMember.canPinCreateOrUpdate(pin.getTopic())) {
            pin.updatePinInfo(request.name(), request.description());
            pinRepository.save(pin);
            return;
        }
        throw new IllegalArgumentException("해당 토픽의 핀을 수정할 권한이 없습니다.");
    }

    public void removeById(AuthMember authMember, Long pinId) {
        Pin pin = pinRepository.findById(pinId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 핀입니다."));

        if (authMember.canDelete(pin.getTopic())) {
            pinRepository.deleteById(pinId);
            pinImageRepository.deleteAllByPinId(pinId);
            return;
        }
        throw new IllegalArgumentException("해당 토픽의 핀을 삭제할 권한이 없습니다.");
    }

    public void addImage(AuthMember authMember, PinImageCreateRequest request) {
        Pin pin = pinRepository.findById(request.pinId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 핀입니다."));

        if (authMember.canPinCreateOrUpdate(pin.getTopic())) {
            // TODO pin.addImage()가 특히 자연스러운 로직인거 같은데 setter 안쓰는 일관성 유지를 위해 일단 이렇게 했습니다.. 계속 이렇게 할지 논의 필요해보여요
            PinImage pinImage = PinImage.createPinImageAssociatedWithPin(Image.of(request.imageUrl()), pin);
            pinImageRepository.save(pinImage);
            return;
        }
        throw new IllegalArgumentException("해당 토픽의 핀을 수정할 권한이 없습니다.");
    }

    public void removeImage(AuthMember authMember, Long pinImageId) {
        PinImage pinImage = pinImageRepository.findById(pinImageId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 핀 이미지입니다."));

        Pin pin = pinImage.getPin();
        if (authMember.canDelete(pin.getTopic())) {
            pinImageRepository.deleteById(pinImageId);
            return;
        }
        throw new IllegalArgumentException("해당 토픽의 핀을 수정할 권한이 없습니다.");
    }
}
