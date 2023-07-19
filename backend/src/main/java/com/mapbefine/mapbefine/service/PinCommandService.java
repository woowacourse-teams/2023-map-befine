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

    public Long save(PinCreateRequest request) {

        // 기존 Pin 이 등록된 위치가 아니면
        // Pin 등록
        // 기존 Pin 이 등록된 위치라면, 해당 Pin 을 가져옴
        // User Pin 에다가 Pin 을 넣어서 저장?

        // 이 위치에 있는 Pin 이 있는지 검사를 어떻게 할 것인지
        // 위도 경도, 10m (또 반경이고)
        // 또 위도 경도만 보는 것이 아니라, 주소도 봐야함 (일단 Road Address 로)
        // 위도 경도,
        // 위도 경도 계산
        // 위도 경도 계산했을 때, 남은 놈들 (Pin)
        // 얘내들 중 주소가 동일한 것이 있느냐... 를 봐야함
        // 일단 Pin 다 가져옴
        // 그리고 여기서 위도 경도 계산하고, 범위 내에 있는놈이 있으면 주소까지 동일한지 보고
        // 이때, 범위 내에 여러개의 핀이 있을 수 있기때문에 거리 순으로 정렬하고, 우선적으로 주소를 확인하고
        // 주소까지 동일하다면 해당 Pin 사용
        // select p from Pin p where p.longitude > :longitude - 0.0001 and p.longitude < longitude + 0.0001 and p.latitude - 0.0001 ㅅㅂ이따가해

        Coordinate coordinate = new Coordinate(request.latitude(), request.longitude());
        Topic topic = topicRepository.findById(request.topicId())
                .orElseThrow(NoSuchElementException::new);

        Location pinLocation = locationRepository.findAllByRectangle(
                        coordinate.getLatitude(),
                        coordinate.getLongitude(),
                        Coordinate.getDuplicateStandardDistance()
                )
                .stream()
                .filter(location -> location.getCoordinate().isDuplicateCoordinate(coordinate))
                .filter(location -> location.getRoadBaseAddress().equals(request.address()))
                .findFirst()
                .orElseGet(() -> saveLocation(request, coordinate));

        Pin pin = new Pin(request.name(), request.description(), pinLocation, topic);

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
        Pin pin = pinRepository.findById(pinId).orElseThrow(NoSuchElementException::new);

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
