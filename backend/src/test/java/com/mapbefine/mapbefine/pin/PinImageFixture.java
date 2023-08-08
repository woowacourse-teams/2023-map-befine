package com.mapbefine.mapbefine.pin;

import com.mapbefine.mapbefine.common.entity.Image;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.domain.PinImage;

public class PinImageFixture {

    public static PinImage create(Pin pin) {
        return PinImage.createPinImageAssociatedWithPin(Image.of("https://example.com/image.jpg"), pin);
    }
}
