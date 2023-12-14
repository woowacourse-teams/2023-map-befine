package com.mapbefine.mapbefine.permission.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PermissionTest {

    @Test
    @DisplayName("정상적인 값을 입력하면 객체가 생성된다.")
    void createPermission() {
        //given
        Long topicId = 1L;
        Long memberId = 2L;

        // when
        Permission permission = Permission.of(topicId, memberId);

        // then
        assertThat(permission.getTopicId()).isEqualTo(topicId);
        assertThat(permission.getMemberId()).isEqualTo(memberId);
    }

}
