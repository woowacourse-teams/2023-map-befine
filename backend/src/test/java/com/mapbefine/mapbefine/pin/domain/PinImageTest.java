package com.mapbefine.mapbefine.pin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.common.entity.Image;
import com.mapbefine.mapbefine.location.LocationFixture;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.pin.Domain.Pin;
import com.mapbefine.mapbefine.pin.Domain.PinImage;
import com.mapbefine.mapbefine.pin.PinFixture;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PinImageTest {

    private Topic topic;
    private Pin pin;

    @BeforeEach
    void setUp() {
        topic = TopicFixture.createByName("아이크의 부잣집", MemberFixture.create(Role.USER));
        pin = PinFixture.create(LocationFixture.create(), topic);

    }


    @Test
    void createPinImageAssociatedWithPin() {
        //given
        String imageUrl = "https://example.com/image.jpg";

        //when
        PinImage pinImage = PinImage.createPinImageAssociatedWithPin(imageUrl, pin);
        Image image = pinImage.getImage();

        //then
        assertThat(pinImage).isNotNull();
        assertThat(image.getImageUrl()).isEqualTo(imageUrl);
        assertThat(pinImage.getPin()).isEqualTo(pin);
        assertThat(pin.getPinImages()).contains(pinImage);
    }
}
