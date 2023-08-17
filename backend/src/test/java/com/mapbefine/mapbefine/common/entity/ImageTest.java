package com.mapbefine.mapbefine.common.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mapbefine.mapbefine.common.exception.BadRequestException.ImageBadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ImageTest {

    @Nested
    class validate {

        @Test
        @DisplayName("잘못된 형식의 URL을 입력하면 예외가 발생한다.")
        void fail() {
            //given
            String validImageUrl = "https://example.com/image.jpg";

            //when
            Image image = Image.from(validImageUrl);

            //then
            assertThat(image).isNotNull();
            assertThat(image.getImageUrl()).isEqualTo(validImageUrl);
        }

        @Test
        @DisplayName("정확한 URL을 입력하면 객체가 정상적으로 생성된다.")
        void success() {
            //given
            String invalidImageUrl = "h://example.com/notimage";

            //when
            //then
            assertThatThrownBy(() -> Image.from(invalidImageUrl))
                    .isInstanceOf(ImageBadRequestException.class);
        }

    }

}
