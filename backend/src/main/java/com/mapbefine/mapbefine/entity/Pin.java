package com.mapbefine.mapbefine.entity;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Pin extends BaseEntity {

    private static final int MAX_DESCRIPTION_LENGTH = 1000;
    private static final int MAX_NAME_LENGTH = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Lob
    @Column(nullable = false, length = 1000)
    private String description;

    @OneToMany(mappedBy = "pin", cascade = CascadeType.PERSIST)
    private List<PinImage> pinImages = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    private Pin(
            String name,
            String description,
            Location location,
            Topic topic
    ) {
        validateName(name);
        validateDescription(description);

        this.name = name;
        this.description = description;
        this.location = location;
        this.topic = topic;
    }

    public static Pin createPinAssociatedWithLocationAndTopic(
            String name,
            String description,
            Location location,
            Topic topic
    ) {
        Pin pin = new Pin(name, description, location, topic);
        location.addPin(pin);
        topic.addPin(pin);
        return pin;
    }

    private void validateName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name null");
        }
        if (name.isBlank() || name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("이름 길이 이상");
        }
    }

    private void validateDescription(String description) {
        if (description == null) {
            throw new IllegalArgumentException("description null");
        }
        if (description.isBlank() || description.length() > MAX_DESCRIPTION_LENGTH) {
            throw new IllegalArgumentException("description 길이 이상");
        }
    }

    public Pin copy(Topic topic) {
        return Pin.createPinAssociatedWithLocationAndTopic(name, description, location, topic);
    }

    public void update(String name, String description) {
        validateName(name);
        validateDescription(description);

        this.name = name;
        this.description = description;
    }

    public void addPinImage(PinImage pinImage) {
        pinImages.add(pinImage);
    }

    public BigDecimal getLatitude() {
        return location.getLatitude();
    }

    public BigDecimal getLongitude() {
        return location.getLongitude();
    }

    public String getRoadBaseAddress() {
        return location.getRoadBaseAddress();
    }

}
