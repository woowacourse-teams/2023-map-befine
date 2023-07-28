package com.mapbefine.mapbefine;

import com.mapbefine.mapbefine.entity.pin.Location;
import com.mapbefine.mapbefine.entity.pin.Pin;
import com.mapbefine.mapbefine.entity.topic.Topic;

public class PinFixture {

    public static Pin create(Location location, Topic topic) {
        return Pin.createPinAssociatedWithLocationAndTopic(
                topic.getName() + "의 핀",
                "위도: " + location.getCoordinate().getLatitude() + ", 경도: " + location.getCoordinate().getLongitude(),
                location,
                topic
        );
    }

}
