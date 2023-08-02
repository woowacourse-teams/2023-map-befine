package com.mapbefine.mapbefine.location.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.mapbefine.mapbefine.location.LocationFixture;
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
            Address address = LocationFixture.ADDRESS;

            //when
            boolean parcelAddress = address.isSameAddress(LocationFixture.PARCEL_ADDRESS);
            boolean roadAddress = address.isSameAddress(LocationFixture.ROAD_ADDRESS);

            //then
            assertThat(parcelAddress).isTrue();
            assertThat(roadAddress).isTrue();
        }

        @Test
        @DisplayName("지번 주소나 도로명 주소가 동일한 true를 반환한다.")
        void fail() {
            //given
            Address address = LocationFixture.ADDRESS;

            //when
            boolean otherParcelAddress = address.isSameAddress("다른 지번 주소");
            boolean otherRoadAddress = address.isSameAddress("다른 도로명 주소");
            //then
            assertThat(otherParcelAddress).isFalse();
            assertThat(otherRoadAddress).isFalse();
        }

    }

}
