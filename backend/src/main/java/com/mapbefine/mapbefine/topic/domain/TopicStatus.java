package com.mapbefine.mapbefine.topic.domain;

import static com.mapbefine.mapbefine.topic.exception.TopicErrorCode.ILLEGAL_PERMISSION_FOR_PUBLICITY_PRIVATE;
import static com.mapbefine.mapbefine.topic.exception.TopicErrorCode.ILLEGAL_PERMISSION_NULL;
import static com.mapbefine.mapbefine.topic.exception.TopicErrorCode.ILLEGAL_PERMISSION_UPDATE;
import static com.mapbefine.mapbefine.topic.exception.TopicErrorCode.ILLEGAL_PUBLICITY_FOR_PERMISSION_ALL_MEMBERS;
import static com.mapbefine.mapbefine.topic.exception.TopicErrorCode.ILLEGAL_PUBLICITY_NULL;
import static lombok.AccessLevel.PROTECTED;

import com.mapbefine.mapbefine.topic.exception.TopicException.TopicBadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = PROTECTED)
@Getter
public class TopicStatus {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Publicity publicity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PermissionType permissionType;

    private TopicStatus(Publicity publicity, PermissionType permissionType) {
        this.publicity = publicity;
        this.permissionType = permissionType;
    }

    public static TopicStatus of(Publicity publicity, PermissionType permissionType) {
        validateTopicStatus(publicity, permissionType);

        return new TopicStatus(publicity, permissionType);
    }

    private static void validateTopicStatus(Publicity publicity, PermissionType permissionType) {
        if (Objects.isNull(publicity)) {
            throw new TopicBadRequestException(ILLEGAL_PUBLICITY_NULL);
        }

        if (Objects.isNull(permissionType)) {
            throw new TopicBadRequestException(ILLEGAL_PERMISSION_NULL);
        }

        if (publicity.isPrivate() && permissionType.isAllMembers()) {
            throw new TopicBadRequestException(ILLEGAL_PERMISSION_FOR_PUBLICITY_PRIVATE);
        }
    }

    public void update(Publicity publicity, PermissionType permissionType) {
        validatePublicity(publicity, permissionType);
        validatePermission(permissionType);

        this.publicity = publicity;
        this.permissionType = permissionType;
    }

    private void validatePublicity(Publicity publicity, PermissionType permissionType) {
        if (publicity.isPrivate() && permissionType.isAllMembers()) {
            throw new TopicBadRequestException(ILLEGAL_PUBLICITY_FOR_PERMISSION_ALL_MEMBERS);
        }
    }

    // TODO: 2023/08/09 해당 정책으로 인해, TopicStatus는 불변 객체로 만들 수 없을까 ?
    private void validatePermission(PermissionType permissionType) {
        if (this.permissionType.isAllMembers() && permissionType.isGroupOnly()) {
            throw new TopicBadRequestException(ILLEGAL_PERMISSION_UPDATE);
        }
    }

    public boolean isPublic() {
        return publicity.isPublic();
    }

    public boolean isPrivate() {
        return publicity.isPrivate();
    }

    public boolean isAllMembers() {
        return permissionType.isAllMembers();
    }

    public boolean isGroupOnly() {
        return permissionType.isGroupOnly();
    }

}
