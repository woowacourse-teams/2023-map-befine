package com.mapbefine.mapbefine.pin;

import com.mapbefine.mapbefine.location.domain.Location;
import com.mapbefine.mapbefine.pin.Domain.Pin;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicInfo;

public class PinFixture {

    public static Pin create(Location location, Topic topic) {
        TopicInfo topicInfo = topic.getTopicInfo();
        return Pin.createPinAssociatedWithLocationAndTopic(
                topicInfo.getName() + "의 핀",
                "위도: " + location.getCoordinate().getLatitude() + ", 경도: " + location.getCoordinate().getLongitude(),
                location,
                topic
        );
    }

}
