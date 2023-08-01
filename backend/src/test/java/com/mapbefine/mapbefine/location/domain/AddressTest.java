package com.mapbefine.mapbefine.location.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.mapbefine.mapbefine.location.AddressFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class AddressTest {

    @Nested
    @DisplayName("isSameAddress에")
    class IsSameAddress {

        @Test
        @DisplayName("지번 주소나 도로명 주소가 동일한 true를 반환한다.")
        void success() {
            //given
            Address address = AddressFixture.create();

            //when
            boolean 지번_주소 = address.isSameAddress(AddressFixture.지번_주소);
            boolean 도로명_주소 = address.isSameAddress(AddressFixture.도로명_주소);

            //then
            assertThat(지번_주소).isTrue();
            assertThat(도로명_주소).isTrue();
        }

        @Test
        @DisplayName("지번 주소나 도로명 주소가 동일한 true를 반환한다.")
        void fail() {
            //given
            Address address = AddressFixture.create();

            //when
            boolean 다른_지번_주소 = address.isSameAddress("다른 지번 주소");
            boolean 다른_도로명_주소 = address.isSameAddress("다른 도로명 주소");
            //then
            assertThat(다른_지번_주소).isFalse();
            assertThat(다른_도로명_주소).isFalse();
        }

    }

}
