package com.mapbefine.mapbefine.topic.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class PublicityTest {


    @Nested
    @DisplayName("isPublic 메서드를 호출했을 때 pulic이 ")
    class IsPublic {

        @Test
        @DisplayName("아니면, false를 반환한다.")
        void fail() {
            //given
            Publicity aPrivate = Publicity.PRIVATE;

            //when
            //then
            assertThat(aPrivate.isPublic()).isFalse();
        }

        @Test
        @DisplayName("맞으면 true를 반환한다.")
        void success() {
            //given
            Publicity aPublic = Publicity.PUBLIC;

            //when
            //then
            assertThat(aPublic.isPublic()).isTrue();
        }

    }

    @Nested
    @DisplayName("isPrivate 메서드를 호출했을 때, private가 ")
    class IsPrivate {

        @Test
        @DisplayName("아니면, false를 반환한다.")
        void fail() {
            //given
            Publicity aPublic = Publicity.PUBLIC;

            //when
            //then
            assertThat(aPublic.isPrivate()).isFalse();
        }

        @Test
        @DisplayName("맞으면 true를 반환한다.")
        void success() {
            //given
            Publicity aPrivate = Publicity.PRIVATE;

            //when
            //then
            assertThat(aPrivate.isPrivate()).isTrue();
        }

    }

}
