package com.mapbefine.mapbefine.pin.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.common.entity.Image;
import com.mapbefine.mapbefine.location.LocationFixture;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.pin.PinFixture;
import com.mapbefine.mapbefine.topic.TopicFixture;
import org.junit.jupiter.api.Test;

class PinImageTest {

    private static final Pin PIN = PinFixture.create(
            LocationFixture.create(),
            TopicFixture.createByName("아이크의 부잣집", MemberFixture.create("topic creator", "topic@naver.com", Role.USER)),
            MemberFixture.create("pin creator", "pin@naver.com", Role.USER)
    );

    @Test
    void createPinImageAssociatedWithPin() {
        //given
        String imageUrl = "https://example.com/image.jpg";

        //when
        PinImage pinImage = PinImage.createPinImageAssociatedWithPin(Image.of(imageUrl), PIN);

        //then
        assertThat(pinImage).isNotNull();
        assertThat(pinImage.getImageUrl()).isEqualTo(imageUrl);
        assertThat(pinImage.getPin()).isEqualTo(PIN);
        assertThat(PIN.getPinImages()).contains(pinImage);
    }

}
