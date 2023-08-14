package com.mapbefine.mapbefine.topic.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TopicStatusTest {

    @Nested
    class Validate {

        @Test
        @DisplayName("공개 범위가  혼자 볼 지도일 때, 권한 설정이 전체 회원이면 예외가 발생한다.")
        void fail() {
            //given
            Publicity publicity = Publicity.PRIVATE;
            PermissionType permissionType = PermissionType.ALL_MEMBERS;

            //when
            //then
            assertThatThrownBy(() -> TopicStatus.of(publicity, permissionType))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("공개 범위가 혼자 볼 지도인 경우, 권한 설정이 소속 회원이어야합니다.");
        }

        @Test
        @DisplayName("공개 범위가 혼자 볼 지도일 때, 권한 설정이 소속 회원이면 정상적으로 동작한다")
        void whenPrivateAndGroupOnly_thenSuccess() {
            //given
            Publicity publicity = Publicity.PRIVATE;
            PermissionType permissionType = PermissionType.GROUP_ONLY;

            //when
            TopicStatus topicStatus = TopicStatus.of(publicity, permissionType);

            //then
            assertThat(topicStatus).isNotNull();
            assertThat(topicStatus.isPrivate()).isTrue();
            assertThat(topicStatus.isGroupOnly()).isTrue();
        }

        @Test
        @DisplayName("공개 범위가 같이 볼 지도일 때, 권한 설정이 소속 회원이면 정상적으로 동작한다")
        void whenPublicAndGroupOnly_thenSuccess() {
            //given
            Publicity publicity = Publicity.PUBLIC;
            PermissionType permissionType = PermissionType.GROUP_ONLY;

            //when
            TopicStatus topicStatus = TopicStatus.of(publicity, permissionType);

            //then
            assertThat(topicStatus).isNotNull();
            assertThat(topicStatus.isPublic()).isTrue();
            assertThat(topicStatus.isGroupOnly()).isTrue();
        }

        @Test
        @DisplayName("공개 범위가 같이 볼 지도일 때, 권한 설정이 소속 회원이면 정상적으로 동작한다")
        void whenPublicAndAllMembers_thenSuccess() {
            //given
            Publicity publicity = Publicity.PUBLIC;
            PermissionType permissionType = PermissionType.ALL_MEMBERS;

            //when
            TopicStatus topicStatus = TopicStatus.of(publicity, permissionType);

            //then
            assertThat(topicStatus).isNotNull();
            assertThat(topicStatus.isPublic()).isTrue();
            assertThat(topicStatus.isAllMembers()).isTrue();
        }

        @Test
        @DisplayName("공개 범위가 null인 경우, 예외가 발생한다.")
        public void whenPublicityIsNull_Fail() {
            //given
            Publicity publicity = null;
            PermissionType permissionType = PermissionType.ALL_MEMBERS;

            //when //then
            assertThatThrownBy(() -> TopicStatus.of(publicity, permissionType))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("공개 범위는 null일 수 없습니다.");
        }

        @Test
        @DisplayName("권한 설정이 null인 경우, 예외가 발생한다.")
        public void whenPermissionIsNull_Fail() {
            //given
            Publicity publicity = Publicity.PUBLIC;
            PermissionType permissionType = null;

            //when //then
            assertThatThrownBy(() -> TopicStatus.of(publicity, permissionType))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("권한 설정은 null일 수 없습니다.");
        }

    }

    @Nested
    class Update {

        @Test
        @DisplayName("권한 범위가 모든 멤버이면, 공개 범위를 혼자 볼 지도로 설정할 때 예외가 발생한다")
        void whenAllMembersAndPrivate_thenFail() {
            //given
            TopicStatus topicStatus = TopicStatus.of(Publicity.PUBLIC, PermissionType.ALL_MEMBERS);

            //when
            //then
            assertThatThrownBy(() -> topicStatus.update(Publicity.PRIVATE, PermissionType.ALL_MEMBERS))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("권한 범위가 모든 멤버인 경우, 공개 범위를 혼자 볼 지도로 설정할 수 없습니다.");
        }

        @Test
        @DisplayName("권한 범위를 GroupOnly로 변경할 경우, 예외가 발생한다")
        void whenUpdateGroupOnly_thenFail() {
            TopicStatus topicStatus = TopicStatus.of(Publicity.PUBLIC, PermissionType.ALL_MEMBERS);

            //when
            //then
            assertThatThrownBy(() -> topicStatus.update(Publicity.PUBLIC, PermissionType.GROUP_ONLY))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("권한 범위는 줄어들 수 없습니다.");
        }

        @Test
        @DisplayName("권한 범위가 GroupOnly일 때 private을 public으로 변경 가능하다")
        void whenUpdatePublicityToPublic_thenSuccess() {
            TopicStatus topicStatus = TopicStatus.of(Publicity.PRIVATE, PermissionType.GROUP_ONLY);

            //when
            topicStatus.update(Publicity.PUBLIC, PermissionType.GROUP_ONLY);

            //then
            assertThat(topicStatus).isNotNull();
            assertThat(topicStatus.isPublic()).isTrue();
            assertThat(topicStatus.isGroupOnly()).isTrue();
        }

        @Test
        @DisplayName("권한 범위가 GroupOnly일 때 public을 private으로 변경 가능하다")
        void whenUpdatePublicityToPrivate_thenSuccess() {
            TopicStatus topicStatus = TopicStatus.of(Publicity.PUBLIC, PermissionType.GROUP_ONLY);

            //when
            topicStatus.update(Publicity.PRIVATE, PermissionType.GROUP_ONLY);

            //then
            assertThat(topicStatus).isNotNull();
            assertThat(topicStatus.isPrivate()).isTrue();
            assertThat(topicStatus.isGroupOnly()).isTrue();
        }

    }

}
