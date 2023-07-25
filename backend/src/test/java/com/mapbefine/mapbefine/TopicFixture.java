package com.mapbefine.mapbefine;

import com.mapbefine.mapbefine.entity.Topic;

public class TopicFixture {

    public static Topic createByName(String name) {
        return new Topic(name, "설명", null);
    }

}
