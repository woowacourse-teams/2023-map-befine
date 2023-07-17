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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Location extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String parcelBaseAddress;
    @Column(nullable = false)
    private String roadBaseAddress;
    @Embedded
    private Coordinate coordinate;
    @ManyToOne
    @JoinColumn(name = "legal_dong_id", nullable = false)
    private LegalDong legalDong;

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
