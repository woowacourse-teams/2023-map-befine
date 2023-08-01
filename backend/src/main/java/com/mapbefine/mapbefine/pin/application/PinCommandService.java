package com.mapbefine.mapbefine.pin.application;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.auth.domain.AuthTopic;
import com.mapbefine.mapbefine.location.domain.Address;
import com.mapbefine.mapbefine.location.domain.Coordinate;
import com.mapbefine.mapbefine.location.domain.Location;
import com.mapbefine.mapbefine.location.domain.LocationRepository;
import com.mapbefine.mapbefine.pin.Domain.Pin;
import com.mapbefine.mapbefine.pin.Domain.PinImage;
import com.mapbefine.mapbefine.pin.Domain.PinRepository;
import com.mapbefine.mapbefine.pin.dto.request.PinCreateRequest;
import com.mapbefine.mapbefine.pin.dto.request.PinUpdateRequest;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import java.util.List;
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
        Coordinate coordinate = Coordinate.of(request.latitude(), request.longitude());
        Topic topic = topicRepository.findById(request.topicId())
                .orElseThrow(NoSuchElementException::new);
        member.canPinCreateOrUpdate(AuthTopic.from(topic));

        List<Location> all = locationRepository.findAllByCoordinateAndDistanceInMeters(
                coordinate, 10.0);

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
        Address address = new Address(
                pinCreateRequest.address(),
                pinCreateRequest.address(),
                pinCreateRequest.legalDongCode()
        );

        Location location = new Location(address, coordinate);

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

        pin.updatePinInfo(request.name(), request.description());

        pinRepository.save(pin);
    }

    public void removeById(AuthMember member, Long pinId) {
        Pin pin = pinRepository.findById(pinId)
                .orElseThrow(NoSuchElementException::new);
        member.canDelete(AuthTopic.from(pin.getTopic()));
        pinRepository.deleteById(pinId);
    }

}
