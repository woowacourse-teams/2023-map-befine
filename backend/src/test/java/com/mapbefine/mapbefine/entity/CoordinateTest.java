package com.mapbefine.mapbefine.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CoordinateTest {

	@Nested
	@DisplayName("위도의 값이 33~43사이의 값")
	class validateLatitude {
		@ParameterizedTest
		@ValueSource(strings = {"33.1", "42.9"})
		@DisplayName("이면 통과한다.")
		void validateLatitude_Success(String input) {
			BigDecimal latitude = new BigDecimal(input);

			assertDoesNotThrow(() -> new Coordinate(latitude, BigDecimal.valueOf(127)));
		}

		@ParameterizedTest
		@ValueSource(strings = {"32.9", "43.1"})
		@DisplayName("이 아니면 실패한다.")
		void validateLatitude_Fail(String input) {
			BigDecimal latitude = new BigDecimal(input);

			Assertions.assertThatThrownBy(() -> new Coordinate(latitude, BigDecimal.valueOf(127)))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("한국 내의 좌표만 입력해주세요.");
		}
	}

	@Nested
	@DisplayName("경도의 값이 124~132사이의 값")
	class validateLongitude {
		@ParameterizedTest
		@ValueSource(strings = {"124.1", "131.9"})
		@DisplayName("이면 통과한다.")
		void validateLongitude_Success(String input) {
			BigDecimal longitude = new BigDecimal(input);

			assertDoesNotThrow(() -> new Coordinate(BigDecimal.valueOf(37), longitude));
		}

		@ParameterizedTest
		@ValueSource(strings = {"123.9", "132.1"})
		@DisplayName("이 아니면 실패한다.")
		void validateLongitude_Fail(String input) {
			BigDecimal longitude = new BigDecimal(input);

			Assertions.assertThatThrownBy(() -> new Coordinate(BigDecimal.valueOf(37), longitude))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("한국 내의 좌표만 입력해주세요.");
		}
	}

}
