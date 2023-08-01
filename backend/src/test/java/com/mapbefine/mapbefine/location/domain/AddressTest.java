package com.mapbefine.mapbefine.location.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class AddressTest {

    private static final String 지번_주소 = "지번 주소";
    private static final String 도로명_주소 = "도로명 주소";
    private static final String 법정동_코드 = "법정동 코드";

    private Address address;

    @BeforeEach
    void setUp() {
        address = new Address(
                지번_주소,
                도로명_주소,
                법정동_코드
        );
    }

    @Nested
    @DisplayName("isSameAddress에")
    class IsSameAddress {

        @Test
        @DisplayName("지번 주소나 도로명 주소가 동일한 true를 반환한다.")
        void success() {
            assertThat(address.isSameAddress(지번_주소)).isTrue();
            assertThat(address.isSameAddress(도로명_주소)).isTrue();
        }

        @Test
        @DisplayName("지번 주소나 도로명 주소가 동일한 true를 반환한다.")
        void fail() {
            assertThat(address.isSameAddress("다른 지번 주소")).isFalse();
            assertThat(address.isSameAddress("다른 도로명 주소")).isFalse();
        }

    }
}
