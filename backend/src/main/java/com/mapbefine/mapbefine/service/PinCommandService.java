package com.mapbefine.mapbefine.service;

import com.mapbefine.mapbefine.dto.PinCreateRequest;
import com.mapbefine.mapbefine.dto.PinUpdateRequest;
import com.mapbefine.mapbefine.entity.Coordinate;
import com.mapbefine.mapbefine.entity.Location;
import com.mapbefine.mapbefine.entity.Pin;
import com.mapbefine.mapbefine.entity.Topic;
import com.mapbefine.mapbefine.repository.LocationRepository;
import com.mapbefine.mapbefine.repository.PinRepository;
import com.mapbefine.mapbefine.repository.TopicRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

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

    public Long save(PinCreateRequest request) {
        Topic topic = topicRepository.findById(request.topicId())
                .orElseThrow(NoSuchElementException::new);
        Location pinLocation = findDuplicateLocationFor(request);

        Pin pin = Pin.createPinAssociatedWithLocationAndTopic(request.name(), request.description(), pinLocation, topic);

        return pinRepository.save(pin).getId();
    }

    private Location findDuplicateLocationFor(PinCreateRequest request) {
        Coordinate coordinate = Coordinate.From(request.latitude(), request.longitude());
        List<Location> locationsInRectangle = locationRepository.findAllByRectangle(
                coordinate.getLatitude(),
                coordinate.getLongitude(),
                Coordinate.getDuplicateStandardDistance()
        );

        return locationsInRectangle.stream()
                .filter(location -> location.isDuplicateCoordinate(coordinate))
                .filter(location -> location.isSameAddress(request.address()))
                .findFirst()
                .orElseGet(() -> saveLocation(request, coordinate));
    }

    private Location saveLocation(PinCreateRequest request, Coordinate coordinate) {
        Location location = new Location(
                request.address(),
                request.address(),
                coordinate,
                request.legalDongCode()
        );

        return locationRepository.save(location);
    }

    public void update(Long pinId, PinUpdateRequest request) {
        Pin pin = pinRepository.findById(pinId)
                .orElseThrow(NoSuchElementException::new);

        pin.update(request.name(), request.description());

        pinRepository.save(pin);
    }

    public void removeById(Long pinId) {
        pinRepository.deleteById(pinId);
    }

    public void removeAllByTopicId(Long topicId) {
        pinRepository.deleteAllByTopicId(topicId);
    }
}
