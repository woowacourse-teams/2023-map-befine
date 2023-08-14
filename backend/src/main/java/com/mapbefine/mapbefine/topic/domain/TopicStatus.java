package com.mapbefine.mapbefine.topic.domain;

import static lombok.AccessLevel.PROTECTED;

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
            throw new IllegalArgumentException("공개 범위는 null일 수 없습니다.");
        }

        if (Objects.isNull(permissionType)) {
            throw new IllegalArgumentException("권한 설정은 null일 수 없습니다.");
        }

        if (publicity.isPrivate() && permissionType.isAllMembers()) {
            throw new IllegalArgumentException("공개 범위가 혼자 볼 지도인 경우, 권한 설정이 소속 회원이어야합니다.");
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
            throw new IllegalArgumentException("권한 범위가 모든 멤버인 경우, 공개 범위를 혼자 볼 지도로 설정할 수 없습니다.");
        }
    }

    // TODO: 2023/08/09 해당 정책으로 인해, TopicStatus는 불변 객체로 만들 수 없을까 ?
    private void validatePermission(PermissionType permissionType) {
        if (this.permissionType.isAllMembers() && permissionType.isGroupOnly()) {
            throw new IllegalArgumentException("권한 범위는 줄어들 수 없습니다.");
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
