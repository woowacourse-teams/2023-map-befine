package com.mapbefine.mapbefine.permission.domain;

import com.mapbefine.mapbefine.common.entity.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Permission extends BaseTimeEntity {

    // TODO 매핑 테이블인데 Id를 가져야 할까?
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long topicId;

    @NotNull
    private Long memberId;

    private Permission(Long topicId, Long memberId) {
        this.topicId = topicId;
        this.memberId = memberId;
    }

    public static Permission of(Long topicId, Long memberId) {
        return new Permission(topicId, memberId);
    }

}
