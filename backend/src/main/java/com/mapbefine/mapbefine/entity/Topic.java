package com.mapbefine.mapbefine.entity;

import static lombok.AccessLevel.*;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
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
    private List<UserPin> userPins = new ArrayList<>();

    @Column(nullable = false)
    private boolean isDeleted;

    public Topic(
        Long id,
        String name,
        String description,
        List<UserPin> userPins
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.userPins = userPins;
    }

    public Topic(
        Long id,
        String name,
        String description
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Topic(
        String name,
        String description
    ) {
        this(null, name, description);
    }

}
