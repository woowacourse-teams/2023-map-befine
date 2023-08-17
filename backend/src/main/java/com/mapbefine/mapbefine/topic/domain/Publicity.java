package com.mapbefine.mapbefine.topic.domain;

import lombok.Getter;

@Getter
public enum Publicity {

    PUBLIC("같이 볼 지도"),
    PRIVATE("혼자 볼 지도");

    private final String title;

    Publicity(String title) {
        this.title = title;
    }

    public boolean isPublic() {
        return this == PUBLIC;
    }

    public boolean isPrivate() {
        return this == PRIVATE;
    }

}
