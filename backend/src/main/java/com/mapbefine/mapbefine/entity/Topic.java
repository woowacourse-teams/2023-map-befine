package com.mapbefine.mapbefine.entity;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Topic extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @Lob
    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "topic")
    private List<Pin> pins;

    @Column(nullable = false)
    private boolean isDeleted;

    public Topic(
            Long id,
            String name,
            String description,
            List<Pin> pins
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.pins = pins;
    }

}
