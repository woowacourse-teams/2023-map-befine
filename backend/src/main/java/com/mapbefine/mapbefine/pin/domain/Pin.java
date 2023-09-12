package com.mapbefine.mapbefine.pin.domain;

import static lombok.AccessLevel.PROTECTED;

import com.mapbefine.mapbefine.common.entity.BaseTimeEntity;
import com.mapbefine.mapbefine.location.domain.Address;
import com.mapbefine.mapbefine.location.domain.Location;
import com.mapbefine.mapbefine.member.domain.Member;
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
import org.hibernate.annotations.SQLDelete;

@Entity
@SQLDelete(sql = "UPDATE pin SET is_deleted = true WHERE id = ?")
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Pin extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private PinInfo pinInfo;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member creator;

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean isDeleted = false;

    @OneToMany(mappedBy = "pin", cascade = CascadeType.PERSIST)
    private List<PinImage> pinImages = new ArrayList<>();

    private Pin(
            PinInfo pinInfo,
            Location location,
            Topic topic,
            Member creator
    ) {
        this.pinInfo = pinInfo;
        this.location = location;
        this.topic = topic;
        this.creator = creator;
    }

    public static Pin createPinAssociatedWithLocationAndTopicAndMember(
            String name,
            String description,
            Location location,
            Topic topic,
            Member creator
    ) {
        Pin pin = new Pin(
                PinInfo.of(name, description),
                location,
                topic,
                creator
        );
        location.addPin(pin);
        topic.addPin(pin);
        creator.addPin(pin);

        return pin;
    }

    public void updatePinInfo(String name, String description) {
        pinInfo = PinInfo.of(name, description);
    }

    public void copyToTopic(Member creator, Topic topic) {
        Pin copiedPin = Pin.createPinAssociatedWithLocationAndTopicAndMember(
                pinInfo.getName(),
                pinInfo.getDescription(),
                location,
                topic,
                creator
        );

        copyPinImages(copiedPin);
    }

    private void copyPinImages(Pin pin) {
        for (PinImage pinImage : pinImages) {
            PinImage.createPinImageAssociatedWithPin(pinImage.getImageUrl(), pin);
        }
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
