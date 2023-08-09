package com.mapbefine.mapbefine.member.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
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

        @Test
        @DisplayName("정확한 값을 입력하면 객체가 생성된다")
        void success() {
            //given when
            MemberInfo memberInfo = MemberInfo.of(
                    VALID_NICK_NAME,
                    VALID_EMAIL,
                    VALID_IMAGE_URL,
                    VALID_ROLE
            );

            //then
            assertThat(memberInfo).isNotNull();
            assertThat(memberInfo.getNickName()).isEqualTo(VALID_NICK_NAME);
            assertThat(memberInfo.getEmail()).isEqualTo(VALID_EMAIL);
            assertThat(memberInfo.getImageUrl()).isEqualTo(VALID_IMAGE_URL);
            assertThat(memberInfo.getRole()).isEqualTo(VALID_ROLE);
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"", "aaaaaaaaaaaaaaaaaaaaa"})
        @DisplayName("유효한 이름이 아닌 경우 예외가 발생한다")
        void whenNameIsInvalid_thenFail(String invalidNickName) {
            //given when then
            assertThatThrownBy(() -> MemberInfo.of(
                    invalidNickName,
                    VALID_EMAIL,
                    VALID_IMAGE_URL,
                    VALID_ROLE
            )).isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @NullSource
        @EmptySource
        @ValueSource(strings = "member")
        @DisplayName("유효한 이메일이 아닌 경우 예외가 발생한다")
        void whenEmailIsInvalid_thenFail(String invalidEmail) {
            //given when then
            assertThatThrownBy(() -> MemberInfo.of(
                    VALID_NICK_NAME,
                    invalidEmail,
                    VALID_IMAGE_URL,
                    VALID_ROLE
            )).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("올바르지 않은 형식의 Image Url 이 들어오는 경우 예외가 발생한다.")
        void whenImageUrlIsInvalid_thenFail() {
            String invalidImageUrl = "image.png";

            //given when then
            assertThatThrownBy(() -> MemberInfo.of(
                    VALID_NICK_NAME,
                    VALID_EMAIL,
                    invalidImageUrl,
                    VALID_ROLE
            )).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("유효하지 않은 Role 이 들어오는 경우 예외가 발생한다.")
        void whenRoleIsInvalid_thenFail() {
            //given when then
            assertThatThrownBy(() -> MemberInfo.of(
                    VALID_NICK_NAME,
                    VALID_EMAIL,
                    VALID_IMAGE_URL,
                    null
            )).isInstanceOf(IllegalArgumentException.class);
        }

    }

    @Nested
    class Update {

        private final String nickName = "member1";
        private final String email = "memberr@naver.com";
        private final String imageUrl = "https://map-befine-official.github.io/Matthew.png";

        private final String VALID_NAME = "member";
        private final String VALID_EMAIL = "member@naver.com";
        private final String VALID_IMAGE_URL = "https://map-befine-official.github.io/favicon.png";
        private final Role VALID_ROLE = Role.ADMIN;

        private MemberInfo memberInfo;

        @BeforeEach
        void setUp() {
            memberInfo = MemberInfo.of(
                    VALID_NAME,
                    VALID_EMAIL,
                    VALID_IMAGE_URL,
                    VALID_ROLE
            );
        }

        @Test
        @DisplayName("정확한 값을 입력하면 객체가 수정된다")
        void success() {
            //when
            memberInfo.update(nickName, email, imageUrl);

            //then
            assertThat(memberInfo).isNotNull();
            assertThat(memberInfo.getNickName()).isEqualTo(nickName);
            assertThat(memberInfo.getEmail()).isEqualTo(email);
            assertThat(memberInfo.getImageUrl()).isEqualTo(imageUrl);
        }


        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"", "aaaaaaaaaaaaaaaaaaaaa"})
        @DisplayName("유효한 이름이 아닌 경우 예외가 발생한다")
        void whenNameIsInvalid_thenFail(String invalidName) {
            // given when then
            assertThatThrownBy(
                    () -> memberInfo.update(invalidName, email, imageUrl)
            ).isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @NullSource
        @EmptySource
        @DisplayName("유효한 email 아닌 경우 예외가 발생한다")
        void whenEmailIsInvalid_thenFail(String invalidEmail) {
            // given when then
            assertThatThrownBy(
                    () -> memberInfo.update(nickName, invalidEmail, imageUrl)
            ).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("유효한 Image Url 이 아닌 경우 예외가 발생한다.")
        void whenImageUrlIsInvalid_thenFail() {
            // given
            String invalidImageUrl = "image.png";
            // when then
            assertThatThrownBy(
                    () -> memberInfo.update(nickName, email, invalidImageUrl)
            ).isInstanceOf(IllegalArgumentException.class);
        }

    }

}
