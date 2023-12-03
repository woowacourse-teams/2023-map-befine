package com.mapbefine.mapbefine.permission.domain;

import com.mapbefine.mapbefine.common.entity.BaseTimeEntity;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Permission extends BaseTimeEntity {

    @EmbeddedId
    private PermissionId id;

    private Permission(PermissionId permissionId) {
        this.id = permissionId;
    }

    public static Permission of(Long topicId, Long memberId) {
        return new Permission(PermissionId.of(topicId, memberId));
    }

    public Long getTopicId() {
        return id.getTopicId();
    }

    public Long getMemberId() {
        return id.getMemberId();
    }

}
