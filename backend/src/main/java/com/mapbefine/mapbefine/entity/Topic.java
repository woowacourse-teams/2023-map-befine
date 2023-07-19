package com.mapbefine.mapbefine.entity;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

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

    @OneToMany(mappedBy = "topic", cascade = CascadeType.PERSIST)
    private List<Pin> pins = new ArrayList<>();

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean isDeleted = false;

    public Topic(
            String name,
            String description
    ) {
        // TODO 이름, 설명 길이 검증
        this.name = name;
        this.description = description;
    }

    public void update(String name, String description) {
        // TODO 이름, 설명 길이 검증
        this.name = name;
        this.description = description;
    }

    public int countPins() {
        return pins.size();
    }

    public void addPin(Pin pin) {
        pins.add(pin);
    }

    public void delete() {
        this.isDeleted = true;
    }

}
