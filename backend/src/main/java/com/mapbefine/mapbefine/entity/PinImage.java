package com.mapbefine.mapbefine.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PinImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2048)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "pin_id")
    private Pin pin;

    private PinImage(String imageUrl, Pin pin) {
        this.imageUrl = imageUrl;
        this.pin = pin;
    }

    public static PinImage createPinImageAssociatedWithPin(String imageUrl, Pin pin) {
        PinImage pinImage = new PinImage(imageUrl, pin);
        pin.addPinImage(pinImage);

        return pinImage;
    }

}
