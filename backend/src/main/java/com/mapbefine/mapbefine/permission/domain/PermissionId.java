package com.mapbefine.mapbefine.permission.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

// TODO: 2023/12/02 EqualsAndHashCode 롬복 쓸까요 말까요 !! 
@Embeddable
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PermissionId that = (PermissionId) o;
        return Objects.equals(getTopicId(), that.getTopicId()) && Objects.equals(getMemberId(), that.getMemberId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTopicId(), getMemberId());
    }

}
