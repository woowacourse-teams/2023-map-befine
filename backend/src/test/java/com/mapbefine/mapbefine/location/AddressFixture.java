package com.mapbefine.mapbefine.location;

import com.mapbefine.mapbefine.location.domain.Address;

public class AddressFixture {

    public static final String 지번_주소 = "지번 주소";
    public static final String 도로명_주소 = "도로명 주소";

    public static Address create() {
        return new Address(
                지번_주소,
                도로명_주소,
                "111000"
        );
    }
}
