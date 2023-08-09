package com.mapbefine.mapbefine.pin;

import com.mapbefine.mapbefine.location.domain.Location;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicInfo;

public class PinFixture {

    public static Pin create(Location location, Topic topic, Member member) {
        TopicInfo topicInfo = topic.getTopicInfo();
        return Pin.createPinAssociatedWithLocationAndTopicAndMember(
                topicInfo.getName() + "의 핀",
                "위도: " + location.getCoordinate().getLatitude() + ", 경도: " + location.getCoordinate().getLongitude(),
                location,
                topic,
                member
        );
    }

}
