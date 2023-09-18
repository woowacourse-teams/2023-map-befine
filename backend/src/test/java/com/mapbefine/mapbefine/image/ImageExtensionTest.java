package com.mapbefine.mapbefine.image;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mapbefine.mapbefine.image.domain.ImageExtension;
import com.mapbefine.mapbefine.image.exception.S3Exception.S3BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ImageExtensionTest {

    @DisplayName("fromByImageFileName 를 통해 정상적으로 ImageExtension 을 생성한다.")
    @ParameterizedTest
    @ValueSource(strings = {"image.jpeg", "image.jpg", "image.jfif", "image.png", "image.svg"})
    void createImageExtensionByFileName_Success(String fileName) {
        // given when
        String extension = ImageExtension.from(fileName)
                .getExtension();

        // then
        assertThat(fileName).contains(extension);
    }

    @DisplayName("fromByImageFileName 을 통해 존재하지 않는 확장자라면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"image.pppng", "image.jpeeg", "image.gi"})
    void createImageExtensionByFileName_Fail(String fileName) {
        // given when then
        assertThatThrownBy(() -> ImageExtension.from(fileName))
                .isInstanceOf(S3BadRequestException.class);
    }

}
