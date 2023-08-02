package com.mapbefine.mapbefine.pin.Domain;

import com.mapbefine.mapbefine.common.entity.Image;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @Embedded
    private Image image;

    @ManyToOne
    @JoinColumn(name = "pin_id")
    private Pin pin;

    
    private PinImage(Image image, Pin pin) {
        this.image = image;
        this.pin = pin;
    }

    public static PinImage createPinImageAssociatedWithPin(String imageUrl, Pin pin) {
        PinImage pinImage = new PinImage(Image.from(imageUrl), pin);
        pin.addPinImage(pinImage);

        return pinImage;
    }

}
