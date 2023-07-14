package com.mapbefine.mapbefine.entity;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class Location extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String parcelBaseAddress; // 지번 주소
    @Column(nullable = false)
    private String roadBaseAddress; // 도로명 주소
    @Embedded
    private Coordinate coordinate; // 위도, 경도
    @ManyToOne
    @JoinColumn(name = "legal_dong_id", nullable = false)
    private LegalDong legalDong; // 법정동

    public Location(
            Long id,
            String parcelBaseAddress,
            String roadBaseAddress,
            Coordinate coordinate,
            LegalDong legalDong
    ) {
        this.id = id;
        this.parcelBaseAddress = parcelBaseAddress;
        this.roadBaseAddress = roadBaseAddress;
        this.coordinate = coordinate;
        this.legalDong = legalDong;
    }

}
