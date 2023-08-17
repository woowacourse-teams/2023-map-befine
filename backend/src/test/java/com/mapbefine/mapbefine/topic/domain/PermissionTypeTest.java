package com.mapbefine.mapbefine.topic.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class PermissionTypeTest {

    @Nested
    @DisplayName("isAllMembers 메서드를 호출했을 때 ALL_MEMBER가 ")
    class IsAllMembers {

        @Test
        @DisplayName("아니면, false를 반환한다.")
        void fail() {
            //given
            PermissionType groupOnly = PermissionType.GROUP_ONLY;

            //when
            //then
            assertThat(groupOnly.isAllMembers()).isFalse();
        }

        @Test
        @DisplayName("맞으면 true를 반환한다.")
        void success() {
            //given
            PermissionType allMembers = PermissionType.ALL_MEMBERS;

            //when
            //then
            assertThat(allMembers.isAllMembers()).isTrue();
        }

    }

    @Nested
    @DisplayName("isGroupOnly 메서드를 호출했을 때, GROUP_ONLY가 ")
    class IsGroupOnly {

        @Test
        @DisplayName("아니면, false를 반환한다.")
        void fail() {
            //given
            PermissionType allMembers = PermissionType.ALL_MEMBERS;

            //when
            //then
            assertThat(allMembers.isGroupOnly()).isFalse();
        }

        @Test
        @DisplayName("맞으면 true를 반환한다.")
        void success() {
            //given
            PermissionType groupOnly = PermissionType.GROUP_ONLY;

            //when
            //then
            assertThat(groupOnly.isGroupOnly()).isTrue();
        }

    }

}
