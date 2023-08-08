package com.mapbefine.mapbefine.pin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.location.domain.Address;
import com.mapbefine.mapbefine.location.domain.Coordinate;
import com.mapbefine.mapbefine.location.domain.Location;
import com.mapbefine.mapbefine.location.domain.LocationRepository;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class PinRepositoryTest {
    private static final Coordinate DEFAULT_COORDINATE = Coordinate.of(
            37.5152933,
            127.1029866
    );

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private PinRepository pinRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("핀을 삭제하면 soft-deleting 된다.")
    void deleteById_Success() {
        // given
        Member member = memberRepository.save(
                MemberFixture.create(
                        "member",
                        "member@naver.com",
                        Role.ADMIN
                )
        );
        Topic topic = TopicFixture.createByName("name", member);
        Address address = new Address(
                "parcel",
                "road",
                "legalDongCode"
        );
        Location location = new Location(
                address,
                DEFAULT_COORDINATE
        );
        topicRepository.save(topic);
        locationRepository.save(location);

        Pin pin = Pin.createPinAssociatedWithLocationAndTopicAndMember(
                "name",
                "description",
                location,
                topic,
                member
        );
        pinRepository.save(pin);

        // when
        assertThat(pin.isDeleted()).isFalse();
        pinRepository.deleteById(pin.getId());

        // then
        Pin deletedPin = pinRepository.findById(pin.getId()).get();
        assertThat(deletedPin.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("토픽 ID로 핀을 삭제하면 soft-deleting 된다.")
    void deleteAllByTopicId_Success() {
        // given
        Member member = memberRepository.save(MemberFixture.create(
                "member",
                "member@naver.com",
                Role.ADMIN)
        );
        Topic topic = TopicFixture.createByName("name", member);
        Address address = new Address(
                "parcel",
                "road",
                "legalDongCode"
        );
        Location location = new Location(
                address,
                DEFAULT_COORDINATE
        );

        for (int i = 0; i < 10; i++) {
            Pin pin = Pin.createPinAssociatedWithLocationAndTopicAndMember(
                    "name",
                    "description",
                    location,
                    topic,
                    member
            );
        }

        locationRepository.save(location);
        topicRepository.save(topic);

        // when
        assertThat(topic.getPins()).extractingResultOf("isDeleted")
                .doesNotContain(true);
        pinRepository.deleteAllByTopicId(topic.getId());

        // then
        List<Pin> deletedPins = pinRepository.findAllByTopicId(topic.getId());
        assertThat(deletedPins).extractingResultOf("isDeleted")
                .doesNotContain(false);
    }

}
