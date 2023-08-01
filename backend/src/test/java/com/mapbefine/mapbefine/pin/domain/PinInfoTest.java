package com.mapbefine.mapbefine.pin.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mapbefine.mapbefine.pin.Domain.PinInfo;
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
        @ValueSource(strings = {"",
                "ExampleOf50CharStringToDemonstrateLengthCheck123456"})
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

    @Nested
    class Update {

        private final String name = "쥬니의 안 갈 집";
        private final String description = "쥬니가 두 번은 안 갈 집";

        private PinInfo pinInfo;

        @BeforeEach
        void setUp() {
            pinInfo = PinInfo.of("매튜의 산스장", "매튜가 엄마 몰래 찾는 산스장");
        }

        @Test
        @DisplayName("정확한 값을 입력하면 객체가 수정된다")
        void success() {
            //when
            pinInfo.update(name, description);

            //then
            assertThat(pinInfo).isNotNull();
            assertThat(pinInfo.getName()).isEqualTo(name);
            assertThat(pinInfo.getDescription()).isEqualTo(description);
        }


        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"",
                "ExampleOf50CharStringToDemonstrateLengthCheck123456"})
        @DisplayName("유효한 이름이 아닌 경우 예외가 발생한다")
        void whenNameIsInvalid_thenFail(String input) {
            //when
            //then
            assertThatThrownBy(() -> pinInfo.update(input, description))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @NullSource
        @EmptySource
        @DisplayName("유효한 설명이 아닌 경우 예외가 발생한다")
        void whenDescriptionIsInvalid_thenFail(String input) {
            //when
            //then
            assertThatThrownBy(() -> pinInfo.update(name, input))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("설명의 길이가 1000자를 초과하는 경우 예외가 발생한다")
        void whenDescriptionIsLongerThanThousand_thenFail() {
            //given
            String validDescription = "a".repeat(1001);

            //when
            //then
            assertThatThrownBy(() -> pinInfo.update(name, validDescription))
                    .isInstanceOf(IllegalArgumentException.class);
        }

    }

}
