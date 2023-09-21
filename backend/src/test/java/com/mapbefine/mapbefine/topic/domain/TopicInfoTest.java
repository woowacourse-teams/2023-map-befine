package com.mapbefine.mapbefine.topic.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mapbefine.mapbefine.topic.exception.TopicException.TopicBadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class TopicInfoTest {

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
    @ValueSource(strings = {"", "This name is definitely more than twenty characters long"})
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
                .isInstanceOf(TopicBadRequestException.class);
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
                .isInstanceOf(TopicBadRequestException.class);
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
                .isInstanceOf(TopicBadRequestException.class);
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
        assertThat(topicInfo.getImageUrl()).isEqualTo(
                "https://velog.velcdn.com/images/semnil5202/post/37f3bcb9-0b07-4100-85f6-f1d5ad037c14/image.svg"
        );
    }

}
