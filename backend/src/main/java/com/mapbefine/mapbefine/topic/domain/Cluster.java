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

    public static Cluster from(List<Pin> pins) {
        double[] average = averageCoordinateOfPins(pins);

        return new Cluster(average[0], average[1], pins);
    }

    private static double[] averageCoordinateOfPins(List<Pin> pins) {
        double latitudeSum = 0;
        double longitudeSum = 0;

        for (Pin pin : pins) {
            latitudeSum += pin.getLatitude();
            longitudeSum += pin.getLongitude();
        }

        return new double[] {latitudeSum / pins.size(), longitudeSum / pins.size()};
    }

}
