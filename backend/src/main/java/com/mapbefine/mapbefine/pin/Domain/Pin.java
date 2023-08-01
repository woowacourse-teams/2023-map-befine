package com.mapbefine.mapbefine.pin.Domain;

import static lombok.AccessLevel.PROTECTED;

import com.mapbefine.mapbefine.common.entity.BaseTimeEntity;
import com.mapbefine.mapbefine.location.domain.Address;
import com.mapbefine.mapbefine.location.domain.Location;
import com.mapbefine.mapbefine.topic.domain.Topic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
public class Pin extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private PinInfo pinInfo;

    @OneToMany(mappedBy = "pin", cascade = CascadeType.PERSIST)
    private List<PinImage> pinImages = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean isDeleted = false;

    private Pin(
            PinInfo pinInfo,
            Location location,
            Topic topic
    ) {
        this.pinInfo = pinInfo;
        this.location = location;
        this.topic = topic;
    }

    public static Pin createPinAssociatedWithLocationAndTopic(
            String name,
            String description,
            Location location,
            Topic topic
    ) {
        PinInfo pinInfo = PinInfo.of(name, description);
        Pin pin = new Pin(
                pinInfo,
                location,
                topic
        );
        location.addPin(pin);
        topic.addPin(pin);
        return pin;
    }

    public void updatePinInfo(String name, String description) {
        pinInfo.update(name, description);
    }

    public Pin copy(Topic topic) {
        return Pin.createPinAssociatedWithLocationAndTopic(
                pinInfo.getName(),
                pinInfo.getDescription(),
                location,
                topic
        );
    }

    public void addPinImage(PinImage pinImage) {
        pinImages.add(pinImage);
    }

    public double getLatitude() {
        return location.getLatitude();
    }

    public double getLongitude() {
        return location.getLongitude();
    }

    public String getRoadBaseAddress() {
        Address address = location.getAddress();
        return address.getRoadBaseAddress();
    }

}
