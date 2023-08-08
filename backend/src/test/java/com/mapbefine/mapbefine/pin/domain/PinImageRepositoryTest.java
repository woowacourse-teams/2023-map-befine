package com.mapbefine.mapbefine.pin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.common.entity.Image;
import com.mapbefine.mapbefine.location.LocationFixture;
import com.mapbefine.mapbefine.location.domain.Location;
import com.mapbefine.mapbefine.location.domain.LocationRepository;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.pin.PinFixture;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class PinImageRepositoryTest {

    @Autowired
    private PinImageRepository pinImageRepository;
    @Autowired
    private PinRepository pinRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private MemberRepository memberRepository;

    private Pin pin;
    private Location location;
    private Topic topic;
    private Member member;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(MemberFixture.create("member", "member@gmail.com", Role.USER));
        topic = topicRepository.save(TopicFixture.createByName("topic", member));
        location = locationRepository.save(LocationFixture.create());
        pin = pinRepository.save(PinFixture.create(location, topic, member));
    }

    @Test
    @DisplayName("핀 이미지를 삭제하면, soft-deleting 된다.")
    void deleteById_Success() {
        //given
        PinImage pinImage = PinImage.createPinImageAssociatedWithPin(Image.of("https://example.com/image.jpg"), pin);
        pinImageRepository.save(pinImage);
        Long pinImageId = pinImage.getId();

        //when
        assertThat(pinImage.isDeleted()).isFalse();
        pinImageRepository.deleteById(pinImageId);

        //then
        pinImageRepository.findById(pinImageId)
                .ifPresentOrElse(
                        found -> assertThat(found.isDeleted()).isTrue(),
                        Assertions::fail
                );
    }

    @Test
    @DisplayName("특정 핀의 모든 핀 아미지를 삭제하면, soft-deleting 된다.")
    void deleteAllByPinId_Success() {
        //given
        PinImage pinImage1 = PinImage.createPinImageAssociatedWithPin(Image.of("https://example.com/image.jpg"), pin);
        PinImage pinImage2 = PinImage.createPinImageAssociatedWithPin(Image.of("https://example.com/image2.jpg"), pin);
        pinImageRepository.save(pinImage1);
        pinImageRepository.save(pinImage2);

        //when
        assertThat(pinImage1.isDeleted()).isFalse();
        pinImageRepository.deleteAllByPinId(pin.getId());

        //then
        assertThat(pinImageRepository.findAllByPinId(pin.getId()))
                .extractingResultOf("isDeleted", Boolean.class)
                .containsOnly(true);
    }
}
