package com.mapbefine.mapbefine.pin.domain;

import com.mapbefine.mapbefine.common.entity.BaseTimeEntity;
import com.mapbefine.mapbefine.image.domain.Image;
import jakarta.persistence.Column;
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
import org.hibernate.annotations.ColumnDefault;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PinImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Image image;

    @ManyToOne
    @JoinColumn(name = "pin_id")
    private Pin pin;

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean isDeleted = false;

    private PinImage(Image image, Pin pin) {
        this.image = image;
        this.pin = pin;
    }

    public static PinImage createPinImageAssociatedWithPin(String imageUrl, Pin pin) {
        PinImage pinImage = new PinImage(Image.from(imageUrl), pin);
        pin.addPinImage(pinImage);

        return pinImage;
    }

    public String getImageUrl() {
        return image.getImageUrl();
    }

}
