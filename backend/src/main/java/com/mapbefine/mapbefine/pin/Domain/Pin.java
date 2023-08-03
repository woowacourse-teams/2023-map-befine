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

    /**
     * createPinAssociatedWithLocationAndTopic을 재사용하지 않은 이유
     * 내부적으로 copy를 처리하면, 반환값이 필요 없음 -> 외부에서 반환값을 무시함
     * 반환값이 있을 경우, topic과의 연관관계를 맺어주지 않은 상태로 반환하고,
     * 외부에서 해당 연관관계를 맺어주어도 됨 -> 이 방법은 객체가 불안정한 상태라고 판단 됨.
     */

    public void copyToTopic(Topic otherTopic) {
        PinInfo copiedPinInfo = PinInfo.of(pinInfo.getName(), pinInfo.getDescription());
        Pin copiedPin = new Pin(copiedPinInfo, location, otherTopic);
        location.addPin(copiedPin);

        otherTopic.addPin(copiedPin);
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
