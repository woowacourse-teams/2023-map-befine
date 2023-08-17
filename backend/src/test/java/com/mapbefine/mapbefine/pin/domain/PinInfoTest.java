package com.mapbefine.mapbefine.pin.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mapbefine.mapbefine.pin.exception.PinException.PinBadRequestException;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

class PinInfoTest {

    @ParameterizedTest
    @MethodSource(value = "validNameAndDescription")
    @DisplayName("유효한 이름, 설명으로 Pin Info를 생성할 수 있다")
    void create_Success(String name, String description) {
        // given, when
        PinInfo pinInfo = PinInfo.of(name, description);

        // then
        assertThat(pinInfo).isNotNull();
        assertThat(pinInfo.getName()).isEqualTo(name);
        assertThat(pinInfo.getDescription()).isEqualTo(description);
    }

    private static Stream<Arguments> validNameAndDescription() {
        return Stream.of(
                Arguments.of("1", "1"),
                Arguments.of("1", "1".repeat(1000)),
                Arguments.of("1".repeat(50), "1"),
                Arguments.of("1".repeat(50), "1".repeat(1000))
        );
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @DisplayName("null 또는 빈 값인 이름으로 Pin Info 생성하면 예외가 발생한다")
    void create_FailByNullOrEmptyName(String input) {
        // given
        String validDescription = "매튜가 엄마 몰래 찾는 산스장";

        // when, then
        assertThatThrownBy(() -> PinInfo.of(input, validDescription))
                .isInstanceOf(PinBadRequestException.class);
    }

    @Test
    @DisplayName("50자 초과하는 이름으로 Pin Info 생성하면 예외가 발생한다")
    void create_FailByNameOver50() {
        // given
        String invalidName = "a".repeat(51);
        String validDescription = "매튜가 엄마 몰래 찾는 산스장";

        // when, then
        assertThatThrownBy(() -> PinInfo.of(invalidName, validDescription))
                .isInstanceOf(PinBadRequestException.class);
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @DisplayName("null 또는 빈 값인 설명으로 Pin Info 생성하면 예외가 발생한다")
    void create_FailByNullOrEmptyDescription(String input) {
        //given
        String validName = "매튜의 산스장";

        // when, then
        assertThatThrownBy(() -> PinInfo.of(validName, input))
                .isInstanceOf(PinBadRequestException.class);
    }

    @Test
    @DisplayName("1000자 초과하는 설명으로 Pin Info 생성하면 예외가 발생한다")
    void create_FailByDescriptionOver1000() {
        //given
        String validName = "매튜의 산스장";
        String invalidDescription = "a".repeat(1001);

        //when, then
        assertThatThrownBy(() -> PinInfo.of(validName, invalidDescription))
                .isInstanceOf(PinBadRequestException.class);
    }

}
