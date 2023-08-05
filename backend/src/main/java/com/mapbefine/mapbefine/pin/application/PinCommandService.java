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
import com.mapbefine.mapbefine.pin.domain.PinInfo;
import com.mapbefine.mapbefine.pin.domain.PinRepository;
import com.mapbefine.mapbefine.pin.dto.request.PinCreateRequest;
import com.mapbefine.mapbefine.pin.dto.request.PinUpdateRequest;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class PinCommandService {

    private static final double DUPLICATE_LOCATION_DISTANCE = 10.0;

    private final PinRepository pinRepository;
    private final LocationRepository locationRepository;
    private final TopicRepository topicRepository;
    private final MemberRepository memberRepository;

    // TODO 예외처리 패턴 추상화
    public PinCommandService(
            PinRepository pinRepository,
            LocationRepository locationRepository,
            TopicRepository topicRepository,
            MemberRepository memberRepository
    ) {
        this.pinRepository = pinRepository;
        this.locationRepository = locationRepository;
        this.topicRepository = topicRepository;
        this.memberRepository = memberRepository;
    }


    public Long save(AuthMember authMember, PinCreateRequest request) {
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

            // TODO Pin 안에 있어야 하는 것 아닌가?
            for (String pinImage : request.images()) {
                PinImage.createPinImageAssociatedWithPin(Image.of(pinImage), pin);
            }

            return pinRepository.save(pin).getId();
        }
        throw new IllegalArgumentException("해당 토픽의 핀을 생성할 권한이 없습니다.");
    }

    private Location findDuplicateOrCreatePinLocation(PinCreateRequest request) {
        Coordinate coordinate = Coordinate.of(request.latitude(), request.longitude());

        return locationRepository.findAllByCoordinateAndDistanceInMeters(coordinate, DUPLICATE_LOCATION_DISTANCE)
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
            // TODO PinImage도 update
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
            // TODO PinImage는 어떻게 할 건지?
            return;
        }
        throw new IllegalArgumentException("해당 토픽의 핀을 삭제할 권한이 없습니다.");
    }

}
