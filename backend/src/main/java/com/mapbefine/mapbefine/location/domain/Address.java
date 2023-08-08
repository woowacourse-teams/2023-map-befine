package com.mapbefine.mapbefine.location.domain;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Address {

    @Column(nullable = false)
    private String parcelBaseAddress;

    @Column(nullable = false)
    private String roadBaseAddress;

    @Column(nullable = false)
    private String legalDongCode;

    public Address(
            String parcelBaseAddress,
            String roadBaseAddress,
            String legalDongCode
    ) {
        this.parcelBaseAddress = parcelBaseAddress;
        this.roadBaseAddress = roadBaseAddress;
        this.legalDongCode = legalDongCode;
    }

    public boolean isSameAddress(String otherAddress) {
        return roadBaseAddress.equals(otherAddress) || parcelBaseAddress.equals(otherAddress);
    }

}
