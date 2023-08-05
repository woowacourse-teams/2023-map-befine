package com.mapbefine.mapbefine.pin;

import com.mapbefine.mapbefine.location.domain.Location;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.domain.PinInfo;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicInfo;

public class PinFixture {

    public static Pin create(Location location, Topic topic, Member member) {
        TopicInfo topicInfo = topic.getTopicInfo();
        return Pin.createPinAssociatedWithLocationAndTopicAndMember(
                PinInfo.of(
                        topicInfo.getName() + "의 핀",
                        String.format("위도: %f 경도: %f", location.getLatitude(), location.getLongitude())
                ),
                location,
                topic,
                member
        );
    }

}
