package com.mapbefine.mapbefine.topic.domain;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    private Permission permission;

    private TopicStatus(Publicity publicity, Permission permission) {
        this.publicity = publicity;
        this.permission = permission;
    }

    public static TopicStatus of(Publicity publicity, Permission permission) {
        validateTopicStatus(publicity, permission);

        return new TopicStatus(publicity, permission);
    }

    private static void validateTopicStatus(Publicity publicity, Permission permission) {
        if (publicity.isPrivate() && permission.isAllMembers()) {
            throw new IllegalArgumentException("멤버 공개일 때는, 권한 설정이 소속 회원이어야합니다.");
        }
    }

    public void update(Publicity publicity, Permission permission) {
        validatePublicity(publicity, permission);
        validatePermission(permission);

        this.publicity = publicity;
        this.permission = permission;
    }

    private void validatePublicity(Publicity publicity, Permission permission) {
        if (permission.isAllMembers() && publicity.isPrivate()) {
            throw new IllegalArgumentException("권한 범위가 모든 멤버인 경우, 공개 범위를 혼자 볼 지도로 설정할 수 없습니다.");
        }
    }

    private void validatePermission(Permission permission) {
        if (permission.isGroupOnly()) {
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
        return permission.isAllMembers();
    }

    public boolean isGroupOnly() {
        return permission.isGroupOnly();
    }
}
