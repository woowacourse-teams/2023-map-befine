package com.mapbefine.mapbefine.topic.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class TopicInfoTest {

    @Nested
    class Validate {

        @Test
        @DisplayName("정확한 값을 입력하면 객체가 생성된다")
        void success() {
            //given
            String validName = "매튜의 산스장";
            String validDescription = "매튜가 엄마 몰래 찾는 산스장";
            String validImageUrl = "https://example.com/image.jpg";

            //when
            TopicInfo topicInfo = TopicInfo.of(
                    validName,
                    validDescription,
                    validImageUrl
            );

            //then
            assertThat(topicInfo).isNotNull();
            assertThat(topicInfo.getName()).isEqualTo(validName);
            assertThat(topicInfo.getDescription()).isEqualTo(validDescription);
            assertThat(topicInfo.getImageUrl()).isEqualTo(validImageUrl);
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"", "This nickName is definitely more than twenty characters long"})
        @DisplayName("유효한 이름이 아닌 경우 예외가 발생한다")
        void whenNameIsInvalid_thenFail(String input) {
            //given
            String validDescription = "매튜가 엄마 몰래 찾는 산스장";
            String validImageUrl = "https://example.com/image.jpg";

            //when
            //then
            assertThatThrownBy(
                    () -> TopicInfo.of(
                            input,
                            validDescription,
                            validImageUrl
                    )
            )
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @NullSource
        @EmptySource
        @DisplayName("유효한 설명이 아닌 경우 예외가 발생한다")
        void whenDescriptionIsInvalid_thenFail(String input) {
            //given
            String validName = "매튜의 산스장";
            String validImageUrl = "https://example.com/image.jpg";

            //when
            //then
            assertThatThrownBy(
                    () -> TopicInfo.of(
                            validName,
                            input,
                            validImageUrl
                    )
            )
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("설명의 길이가 1000자를 초과하는 경우 예외가 발생한다")
        void whenDescriptionIsLongerThanThousand_thenFail() {
            //given
            String validName = "매튜의 산스장";
            String validDescription = "a".repeat(1001);
            String validImageUrl = "https://example.com/image.jpg";

            //when
            //then
            assertThatThrownBy(
                    () -> TopicInfo.of(
                            validName,
                            validDescription,
                            validImageUrl
                    )
            )
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("null 값을 입력할 경우, 기본 사진을 저장한다")
        void whenImageNull_thenReturnDefault() {
            //given
            String validName = "매튜의 산스장";
            String validDescription = "매튜가 엄마 몰래 찾는 산스장";

            //when
            TopicInfo topicInfo = TopicInfo.of(
                    validName,
                    validDescription,
                    null
            );

            //then
            assertThat(topicInfo).isNotNull();
            assertThat(topicInfo.getName()).isEqualTo(validName);
            assertThat(topicInfo.getDescription()).isEqualTo(validDescription);
            assertThat(topicInfo.getImageUrl()).isEqualTo("https://map-befine-official.github.io/favicon.png");
        }

    }

    @Nested
    class update {

        private final String name = "쥬니의 안 갈 집";
        private final String description = "쥬니가 두 번은 안 갈 집";
        private final String imageUrl = "https://example.com/image.png";

        private TopicInfo topicInfo;

        @BeforeEach
        void setUp() {
            topicInfo = TopicInfo.of(
                    "매튜의 산스장",
                    "매튜가 엄마 몰래 찾는 산스장",
                    "https://example.com/image.jpg"
            );
        }

        @Test
        @DisplayName("정확한 값을 입력하면 객체가 수정된다")
        void success() {
            //when
            topicInfo.update(
                    name,
                    description,
                    imageUrl
            );

            //then
            assertThat(topicInfo).isNotNull();
            assertThat(topicInfo.getName()).isEqualTo(name);
            assertThat(topicInfo.getDescription()).isEqualTo(description);
            assertThat(topicInfo.getImageUrl()).isEqualTo(imageUrl);
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"", "This nickName is definitely more than twenty characters long"})
        @DisplayName("유효한 이름이 아닌 경우 예외가 발생한다")
        void whenNameIsInvalid_thenFail(String input) {
            //when
            //then
            assertThatThrownBy(
                    () -> topicInfo.update(
                            input,
                            description,
                            imageUrl
                    )
            )
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @NullSource
        @EmptySource
        @DisplayName("유효한 설명이 아닌 경우 예외가 발생한다")
        void whenDescriptionIsInvalid_thenFail(String input) {
            //when
            //then
            assertThatThrownBy(
                    () -> TopicInfo.of(
                            name,
                            input,
                            imageUrl
                    )
            )
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("설명의 길이가 1000자를 초과하는 경우 예외가 발생한다")
        void whenDescriptionIsLongerThanThousand_thenFail() {
            //given
            String updateDescription = "a".repeat(1001);

            //when
            //then
            assertThatThrownBy(
                    () -> TopicInfo.of(
                            name,
                            updateDescription,
                            imageUrl
                    )
            )
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("null 값을 입력할 경우, 기본 사진을 저장한다")
        void whenImageNull_thenReturnDefault() {
            //when
            topicInfo.update(
                    name,
                    description,
                    null
            );
            //then
            assertThat(topicInfo).isNotNull();
            assertThat(topicInfo.getName()).isEqualTo(name);
            assertThat(topicInfo.getDescription()).isEqualTo(description);
            assertThat(topicInfo.getImageUrl()).isEqualTo("https://map-befine-official.github.io/favicon.png");
        }

    }

}
