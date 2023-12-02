package com.mapbefine.mapbefine.permission.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

// TODO: 2023/12/02 EqualsAndHashCode 롬복 쓸까요 말까요 !! 
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PermissionId implements Serializable {

    @Column(nullable = false, updatable = false)
    private Long topicId;

    @Column(nullable = false, updatable = false)
    private Long memberId;

    public PermissionId(Long topicId, Long memberId) {
        this.topicId = topicId;
        this.memberId = memberId;
    }

    public static PermissionId of(Long topicId, Long memberId) {
        return new PermissionId(topicId, memberId);
    }

}
