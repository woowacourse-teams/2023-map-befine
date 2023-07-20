package com.mapbefine.mapbefine;

import com.mapbefine.mapbefine.entity.Location;
import com.mapbefine.mapbefine.entity.Pin;
import com.mapbefine.mapbefine.entity.Topic;

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
