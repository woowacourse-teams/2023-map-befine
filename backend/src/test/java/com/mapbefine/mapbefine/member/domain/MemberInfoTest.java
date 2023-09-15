package com.mapbefine.mapbefine.member.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.mapbefine.mapbefine.common.exception.BadRequestException.ImageBadRequestException;
import com.mapbefine.mapbefine.member.exception.MemberException.MemberBadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

public class MemberInfoTest {

    @Nested
    class Validate {

        private final String VALID_NICK_NAME = "member";
        private final String VALID_EMAIL = "member@naver.com";
        private final String VALID_IMAGE_URL = "https://map-befine-official.github.io/favicon.png";
        private final Role VALID_ROLE = Role.ADMIN;
        private final Status VALID_STATUS = Status.NORMAL;

        @Test
        @DisplayName("유효한 정보를 입력했을 때 객체를 생성할 수 있다.")
        void create_Success() {
            //given when
            MemberInfo memberInfo = MemberInfo.of(
                    VALID_NICK_NAME,
                    VALID_EMAIL,
                    VALID_IMAGE_URL,
                    VALID_ROLE,
                    VALID_STATUS
            );

            //then
            assertThat(memberInfo).isNotNull();
            assertThat(memberInfo.getNickName()).isEqualTo(VALID_NICK_NAME);
            assertThat(memberInfo.getEmail()).isEqualTo(VALID_EMAIL);
            assertThat(memberInfo.getImageUrl()).isEqualTo(VALID_IMAGE_URL);
            assertThat(memberInfo.getRole()).isEqualTo(VALID_ROLE);
        }

        @Test
        @DisplayName("일부 정보(닉네임)만 변경한 회원 정보를 생성할 수 있다.")
        void createUpdatedMemberInfo() {
            // given
            MemberInfo before = MemberInfo.of(
                    "member",
                    "member@naver.com",
                    "https://map-befine-official.github.io/favicon.png",
                    Role.ADMIN,
                    Status.NORMAL);

            // when
            String expected = "newNickName";
            MemberInfo patched = before.createUpdatedMemberInfo(expected);

            // then
            assertSoftly(softly -> {
                assertThat(patched).usingRecursiveComparison()
                        .ignoringFields("nickName")
                        .isEqualTo(before);
                assertThat(patched.getNickName()).isEqualTo(expected);
            });
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"", "aaaaaaaaaaaaaaaaaaaaa"})
        @DisplayName("유효한 닉네임이 아닌 경우 예외가 발생한다")
        void validateNickName(String invalidNickName) {
            //given when then
            assertThatThrownBy(() -> MemberInfo.of(
                    invalidNickName,
                    VALID_EMAIL,
                    VALID_IMAGE_URL,
                    VALID_ROLE,
                    VALID_STATUS
            )).isInstanceOf(MemberBadRequestException.class);
        }

        @ParameterizedTest
        @NullSource
        @EmptySource
        @ValueSource(strings = "member")
        @DisplayName("유효한 이메일이 아닌 경우 예외가 발생한다")
        void validateEmail(String invalidEmail) {
            //given when then
            assertThatThrownBy(() -> MemberInfo.of(
                    VALID_NICK_NAME,
                    invalidEmail,
                    VALID_IMAGE_URL,
                    VALID_ROLE,
                    VALID_STATUS
            )).isInstanceOf(MemberBadRequestException.class);
        }

        @Test
        @DisplayName("올바르지 않은 형식의 Image Url 이 들어오는 경우 예외가 발생한다.")
        void validateImageUrl() {
            String invalidImageUrl = "image.png";

            //given when then
            assertThatThrownBy(() -> MemberInfo.of(
                    VALID_NICK_NAME,
                    VALID_EMAIL,
                    invalidImageUrl,
                    VALID_ROLE,
                    VALID_STATUS
            )).isInstanceOf(ImageBadRequestException.class);
        }

        @Test
        @DisplayName("유효하지 않은 Role 이 들어오는 경우 예외가 발생한다.")
        void validateRole() {
            //given when then
            assertThatThrownBy(() -> MemberInfo.of(
                    VALID_NICK_NAME,
                    VALID_EMAIL,
                    VALID_IMAGE_URL,
                    null
                    ,VALID_STATUS
            )).isInstanceOf(IllegalArgumentException.class);
        }

    }

}
