package com.mapbefine.mapbefine.topic.domain;

import com.mapbefine.mapbefine.pin.domain.Pin;
import java.util.List;
import lombok.Getter;

@Getter
public class Cluster {

    private final double latitude;
    private final double longitude;
    private final List<Pin> pins;

    private Cluster(double latitude, double longitude, List<Pin> pins) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.pins = pins;
    }

    public static Cluster from(Pin representPin, List<Pin> pins) {
        return new Cluster(representPin.getLatitude(), representPin.getLongitude(), pins);
    }

}
