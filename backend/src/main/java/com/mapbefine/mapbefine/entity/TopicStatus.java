package com.mapbefine.mapbefine.entity;

import lombok.Getter;

@Getter
public enum TopicStatus {

    PUBLIC("같이 볼 지도"),
    PRIVATE("혼자 볼 지도");

    private final String title;

    TopicStatus(String title) {
        this.title = title;
    }

}
