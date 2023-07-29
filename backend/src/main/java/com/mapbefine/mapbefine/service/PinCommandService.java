package com.mapbefine.mapbefine.service;

import com.mapbefine.mapbefine.config.auth.AuthMember;
import com.mapbefine.mapbefine.config.auth.AuthTopic;
import com.mapbefine.mapbefine.dto.PinCreateRequest;
import com.mapbefine.mapbefine.dto.PinUpdateRequest;
import com.mapbefine.mapbefine.entity.pin.Coordinate;
import com.mapbefine.mapbefine.entity.pin.Location;
import com.mapbefine.mapbefine.entity.pin.Pin;
import com.mapbefine.mapbefine.entity.pin.PinImage;
import com.mapbefine.mapbefine.entity.topic.Topic;
import com.mapbefine.mapbefine.repository.LocationRepository;
import com.mapbefine.mapbefine.repository.PinRepository;
import com.mapbefine.mapbefine.repository.TopicRepository;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class PinCommandService {

    private final PinRepository pinRepository;
    private final LocationRepository locationRepository;
    private final TopicRepository topicRepository;

    public PinCommandService(
            PinRepository pinRepository,
            LocationRepository locationRepository,
            TopicRepository topicRepository
    ) {
        this.pinRepository = pinRepository;
        this.locationRepository = locationRepository;
        this.topicRepository = topicRepository;
    }

    public Long save(AuthMember member, PinCreateRequest request) {
        Coordinate coordinate = Coordinate.from(request.latitude(), request.longitude());
        Topic topic = topicRepository.findById(request.topicId())
                .orElseThrow(NoSuchElementException::new);
        member.canPinCreateOrUpdate(AuthTopic.from(topic));

        Location pinLocation = locationRepository.findAllByRectangle(
                        coordinate.getLatitude(),
                        coordinate.getLongitude(),
                        Coordinate.getDuplicateStandardDistance()
                )
                .stream()
                .filter(location -> location.isDuplicateCoordinate(coordinate))
                .filter(location -> location.isSameAddress(request.address()))
                .findFirst()
                .orElseGet(() -> saveLocation(request, coordinate));

        Pin pin = Pin.createPinAssociatedWithLocationAndTopic(
                request.name(),
                request.description(),
                pinLocation,
                topic
        );

        for (String pinImage : request.images()) {
            PinImage.createPinImageAssociatedWithPin(pinImage, pin);
        }

        return pinRepository.save(pin).getId();
    }

    private Location saveLocation(PinCreateRequest pinCreateRequest, Coordinate coordinate) {
        Location location = new Location(
                pinCreateRequest.address(),
                pinCreateRequest.address(),
                coordinate,
                pinCreateRequest.legalDongCode()
        );

        return locationRepository.save(location);
    }

    public void update(
            AuthMember member,
            Long pinId,
            PinUpdateRequest request
    ) {
        Pin pin = pinRepository.findById(pinId)
                .orElseThrow(NoSuchElementException::new);
        member.canPinCreateOrUpdate(AuthTopic.from(pin.getTopic()));

        pin.update(request.name(), request.description());

        pinRepository.save(pin);
    }

    public void removeById(AuthMember member, Long pinId) {
        Pin pin = pinRepository.findById(pinId)
                .orElseThrow(NoSuchElementException::new);
        member.canDelete(AuthTopic.from(pin.getTopic()));
        pinRepository.deleteById(pinId);
    }

    public void removeAllByTopicId(Long topicId) { // TODO : pinCommandService 에는 필요없는 메서드 인 것 같음
//        pinRepository.deleteAllByTopicId(topicId);
    }

}
