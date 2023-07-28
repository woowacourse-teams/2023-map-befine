package com.mapbefine.mapbefine.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mapbefine.mapbefine.MemberFixture;
import com.mapbefine.mapbefine.annotation.ServiceTest;
import com.mapbefine.mapbefine.dto.PinDetailResponse;
import com.mapbefine.mapbefine.dto.PinResponse;
import com.mapbefine.mapbefine.entity.member.Member;
import com.mapbefine.mapbefine.entity.member.Role;
import com.mapbefine.mapbefine.entity.pin.Coordinate;
import com.mapbefine.mapbefine.entity.pin.Location;
import com.mapbefine.mapbefine.entity.pin.Pin;
import com.mapbefine.mapbefine.entity.pin.PinImage;
import com.mapbefine.mapbefine.entity.topic.Permission;
import com.mapbefine.mapbefine.entity.topic.Publicity;
import com.mapbefine.mapbefine.entity.topic.Topic;
import com.mapbefine.mapbefine.repository.LocationRepository;
import com.mapbefine.mapbefine.repository.MemberRepository;
import com.mapbefine.mapbefine.repository.PinRepository;
import com.mapbefine.mapbefine.repository.TopicRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@ServiceTest
class PinQueryServiceTest {
    private static final List<String> BASE_IMAGES = List.of("https://map-befine-official.github.io/favicon.png");

    @Autowired
    private PinQueryService pinQueryService;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private PinRepository pinRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Location location;

    private Coordinate coordinate;

    private Topic topic;
    private Member member;

    @BeforeEach
    void setUp() {
        BigDecimal latitude = BigDecimal.valueOf(37.123456);
        BigDecimal longitude = BigDecimal.valueOf(127.123456);
        coordinate = new Coordinate(latitude, longitude);
        location = saveLocation(coordinate);
        member = memberRepository.save(MemberFixture.create(Role.ADMIN));
        topic = topicRepository.save(
                new Topic(
                        "topicName",
                        "topicDescription",
                        "https://map-befine-official.github.io/favicon.png",
                        Publicity.PUBLIC,
                        Permission.ALL_MEMBERS,
                        member
                )
        );
    }

    @Test
    @DisplayName("핀 목록을 가져온다.")
    void findAll_Success() {
        // given
        List<PinResponse> expected = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Pin pin = Pin.createPinAssociatedWithLocationAndTopic("name", "description", location, topic);
            Long savedId = pinRepository.save(pin).getId();
            expected.add(new PinResponse(
                    savedId,
                    "name",
                    "road",
                    "description",
                    coordinate.getLatitude().toString(),
                    coordinate.getLongitude().toString()
            ));
        }

        // when
        List<PinResponse> responses = pinQueryService.findAll();

        // then
        assertThat(responses).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("핀의 Id 를 넘기면 핀을 가져온다.")
    void findById_Success() {
        // given
        Pin pin = Pin.createPinAssociatedWithLocationAndTopic("name", "description", location, topic);
        PinImage.createPinImageAssociatedWithPin(BASE_IMAGES.get(0), pin);
        Long savedId = pinRepository.save(pin).getId();

        // when
        PinDetailResponse expected = new PinDetailResponse(
                savedId,
                "name",
                "road",
                "description",
                coordinate.getLatitude().toString(),
                coordinate.getLongitude().toString(),
                null,
                BASE_IMAGES
        );
        PinDetailResponse actual = pinQueryService.findById(savedId);

        // then
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("updatedAt")
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("존재하지 않는 핀의 Id 를 넘기면 예외를 발생시킨다.")
    void findById_Fail() {
        // given when then
        assertThatThrownBy(() -> pinQueryService.findById(1L))
                .isInstanceOf(NoSuchElementException.class);
    }


    private Location saveLocation(Coordinate coordinate) {
        Location location = new Location(
                "parcel",
                "road",
                coordinate,
                "legalDongCode"
        );

        return locationRepository.save(location);
    }

}
