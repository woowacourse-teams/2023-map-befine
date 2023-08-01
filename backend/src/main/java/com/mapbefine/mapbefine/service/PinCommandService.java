package com.mapbefine.mapbefine.service;

import com.mapbefine.mapbefine.dto.PinCreateRequest;
import com.mapbefine.mapbefine.dto.PinUpdateRequest;
import com.mapbefine.mapbefine.entity.Coordinate;
import com.mapbefine.mapbefine.entity.Location;
import com.mapbefine.mapbefine.entity.Pin;
import com.mapbefine.mapbefine.entity.PinImage;
import com.mapbefine.mapbefine.entity.Topic;
import com.mapbefine.mapbefine.repository.LocationRepository;
import com.mapbefine.mapbefine.repository.PinRepository;
import com.mapbefine.mapbefine.repository.TopicRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Coordinate coordinate = Coordinate.from(request.latitude(), request.longitude());
        Topic topic = topicRepository.findById(request.topicId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 토픽입니다."));

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

        Pin pin = Pin.createPinAssociatedWithLocationAndTopic(request.name(), request.description(), pinLocation, topic);

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

    public void update(Long pinId, PinUpdateRequest request) {
        Pin pin = pinRepository.findById(pinId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 핀입니다."));
        pin.update(request.name(), request.description());

        // TODO save 호출 필요?
        pinRepository.save(pin);

    }

    public void removeById(Long pinId) {
        if (pinRepository.existsById(pinId)) {
            pinRepository.deleteById(pinId);
            return;
        }
        throw new NoSuchElementException("존재하지 않는 핀입니다.");
    }

    public void removeAllByTopicId(Long topicId) {
        if (topicRepository.existsById(topicId)) {
            pinRepository.deleteAllByTopicId(topicId);
            return;
        }
        throw new NoSuchElementException("존재하지 않는 토픽입니다.");
    }
}
