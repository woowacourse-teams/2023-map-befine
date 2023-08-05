package com.mapbefine.mapbefine.pin.domain;

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

class PinInfoTest {

    @Nested
    class Validate {

        @Test
        @DisplayName("정확한 값을 입력하면 객체가 생성된다")
        void success() {
            //given
            String validName = "매튜의 산스장";
            String validDescription = "매튜가 엄마 몰래 찾는 산스장";

            //when
            PinInfo pinInfo = PinInfo.of(validName, validDescription);

            //then
            assertThat(pinInfo).isNotNull();
            assertThat(pinInfo.getName()).isEqualTo(validName);
            assertThat(pinInfo.getDescription()).isEqualTo(validDescription);
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"", "ExampleOf50CharStringToDemonstrateLengthCheck123456"})
        @DisplayName("유효한 이름이 아닌 경우 예외가 발생한다")
        void whenNameIsInvalid_thenFail(String input) {
            //given
            String validDescription = "매튜가 엄마 몰래 찾는 산스장";

            //when
            //then
            assertThatThrownBy(() -> PinInfo.of(input, validDescription))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @NullSource
        @EmptySource
        @DisplayName("유효한 설명이 아닌 경우 예외가 발생한다")
        void whenDescriptionIsInvalid_thenFail(String input) {
            //given
            String validName = "매튜의 산스장";

            //when
            //then
            assertThatThrownBy(() -> PinInfo.of(validName, input))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("설명의 길이가 1000자를 초과하는 경우 예외가 발생한다")
        void whenDescriptionIsLongerThanThousand_thenFail() {
            //given
            String validName = "매튜의 산스장";
            String validDescription = "a".repeat(1001);

            //when
            //then
            assertThatThrownBy(() -> PinInfo.of(validName, validDescription))
                    .isInstanceOf(IllegalArgumentException.class);
        }

    }

}
