package com.mapbefine.mapbefine.entity;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class GuGun extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @ManyToOne
    @JoinColumn(name = "city_do_id")
    private CityDo cityDo;
    @OneToMany(mappedBy = "guGun")
    private List<LegalDong> legalDong;

    public GuGun(
            Long id,
            String name,
            List<LegalDong> legalDong,
            CityDo cityDo
    ) {
        this.id = id;
        this.name = name;
        this.legalDong = legalDong;
        this.cityDo = cityDo;
    }

}
