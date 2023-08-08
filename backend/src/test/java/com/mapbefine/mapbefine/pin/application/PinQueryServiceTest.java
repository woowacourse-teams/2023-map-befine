package com.mapbefine.mapbefine.pin.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.auth.domain.member.Admin;
import com.mapbefine.mapbefine.common.annotation.ServiceTest;
import com.mapbefine.mapbefine.location.domain.Address;
import com.mapbefine.mapbefine.location.domain.Coordinate;
import com.mapbefine.mapbefine.location.domain.Location;
import com.mapbefine.mapbefine.location.domain.LocationRepository;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.domain.PinImage;
import com.mapbefine.mapbefine.pin.domain.PinRepository;
import com.mapbefine.mapbefine.pin.dto.response.PinDetailResponse;
import com.mapbefine.mapbefine.pin.dto.response.PinResponse;
import com.mapbefine.mapbefine.topic.domain.Permission;
import com.mapbefine.mapbefine.topic.domain.Publicity;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
    private AuthMember authMember;

    @BeforeEach
    void setUp() {
        double latitude = 37.123456;
        double longitude = 127.123456;
        coordinate = Coordinate.of(latitude, longitude);
        location = saveLocation(coordinate);
        member = memberRepository.save(MemberFixture.create("member", "member@naver.com", Role.ADMIN));
        authMember = new Admin(member.getId());
        topic = topicRepository.save(
                Topic.createTopicAssociatedWithMember(
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
            Pin pin = Pin.createPinAssociatedWithLocationAndTopicAndMember("name", "description", location, topic, member);
            Long savedId = pinRepository.save(pin).getId();
            expected.add(new PinResponse(
                    savedId,
                    "name",
                    "road",
                    "description",
                    coordinate.getLatitude(),
                    coordinate.getLongitude()
            ));
        }

        // when
        List<PinResponse> responses = pinQueryService.findAll(authMember);

        // then
        assertThat(responses).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("핀의 Id 를 넘기면 핀을 가져온다.")
    void findById_Success() {
        // given
        Pin pin = Pin.createPinAssociatedWithLocationAndTopicAndMember("name", "description", location, topic, member);
        PinImage.createPinImageAssociatedWithPin(BASE_IMAGES.get(0), pin);
        Long savedId = pinRepository.save(pin).getId();

        // when
        PinDetailResponse expected = new PinDetailResponse(
                savedId,
                "name",
                "road",
                "description",
                coordinate.getLatitude(),
                coordinate.getLongitude(),
                null,
                BASE_IMAGES
        );
        PinDetailResponse actual = pinQueryService.findById(authMember, savedId);

        // then
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("updatedAt")
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("존재하지 않는 핀의 Id 를 넘기면 예외를 발생시킨다.")
    void findById_Fail() {
        // given when then
        assertThatThrownBy(() -> pinQueryService.findById(authMember, 1L))
                .isInstanceOf(NoSuchElementException.class);
    }


    private Location saveLocation(Coordinate coordinate) {
        Address address = new Address(
                "parcel",
                "road",
                "legalDongCode"
        );
        Location location = new Location(
                address,
                coordinate
        );

        return locationRepository.save(location);
    }

}
