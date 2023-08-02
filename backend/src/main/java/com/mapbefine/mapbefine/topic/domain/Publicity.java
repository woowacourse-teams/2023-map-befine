package com.mapbefine.mapbefine.topic.domain;

import lombok.Getter;

@Getter
public enum Publicity {

    PUBLIC("같이 볼 지도"),
    PRIVATE("혼자 볼 지도");

    private final String value;

    Publicity(String value) {
        this.value = value;
    }

    public boolean isPublic() {
        return this == PUBLIC;
    }

    public boolean isPrivate() {
        return this == PRIVATE;
    }

}
