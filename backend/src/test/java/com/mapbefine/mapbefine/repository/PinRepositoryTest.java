package com.mapbefine.mapbefine.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.MemberFixture;
import com.mapbefine.mapbefine.TopicFixture;
import com.mapbefine.mapbefine.entity.member.Member;
import com.mapbefine.mapbefine.entity.member.Role;
import com.mapbefine.mapbefine.entity.pin.Coordinate;
import com.mapbefine.mapbefine.entity.pin.Location;
import com.mapbefine.mapbefine.entity.pin.Pin;
import com.mapbefine.mapbefine.entity.topic.Permission;
import com.mapbefine.mapbefine.entity.topic.Publicity;
import com.mapbefine.mapbefine.entity.topic.Topic;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class PinRepositoryTest {
    private static final Coordinate DEFAULT_COORDINATE = new Coordinate(
            BigDecimal.valueOf(37.5152933),
            BigDecimal.valueOf(127.1029866)
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
        Member member = memberRepository.save(MemberFixture.create(Role.ADMIN));
        Topic topic = TopicFixture.createByName("name", member);
        Location location = new Location(
                "parcel",
                "road",
                DEFAULT_COORDINATE,
                "legalDongCode"
        );
        topicRepository.save(topic);
        locationRepository.save(location);

        Pin pin = Pin.createPinAssociatedWithLocationAndTopic("name", "description", location, topic);
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
        Member member = memberRepository.save(MemberFixture.create(Role.ADMIN));
        Topic topic = TopicFixture.createByName("name", member);
        Location location = new Location(
                "parcel",
                "road",
                DEFAULT_COORDINATE,
                "legalDongCode"
        );

        for (int i = 0; i < 10; i++) {
            Pin.createPinAssociatedWithLocationAndTopic("name", "description", location, topic);
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
