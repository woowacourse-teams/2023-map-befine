package com.mapbefine.mapbefine.member.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

    @Test
    @DisplayName("유효한 정보를 입력했을 때 객체가 정상 생성 된다.")
    void createMember_success() {
        // given
        String name = "member";
        String email = "member@naver.com";
        String imageUrl = "https://map-befine-official.github.io/favicon.png";
        Role role = Role.ADMIN;

        // then
        Member member = Member.of(
                "member",
                "member@naver.com",
                "https://map-befine-official.github.io/favicon.png",
                Role.ADMIN
        );

        // then
        assertThat(member).isNotNull();
        assertThat(member.getMemberInfo().getName()).isEqualTo(name);
        assertThat(member.getMemberInfo().getEmail()).isEqualTo(email);
        assertThat(member.getMemberInfo().getImageUrl()).isEqualTo(imageUrl);
        assertThat(member.getMemberInfo().getRole()).isEqualTo(role);
    }

}
