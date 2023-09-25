package com.mapbefine.mapbefine.member.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.oauth.domain.OauthServerType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

    @Test
    @DisplayName("유효한 정보를 입력했을 때 객체가 정상 생성 된다.")
    void createMember_success() {
        // given
        String nickName = "member";
        String email = "member@naver.com";
        String imageUrl = "https://map-befine-official.github.io/favicon.png";
        Role role = Role.ADMIN;
        Status status = Status.NORMAL;

        // when
        Member member = Member.of(
                nickName,
                email,
                imageUrl,
                role,
                status,
                new OauthId(1L, OauthServerType.KAKAO)
        );

        // then
        assertThat(member).isNotNull();
        assertThat(member.getMemberInfo().getNickName()).isEqualTo(nickName);
        assertThat(member.getMemberInfo().getEmail()).isEqualTo(email);
        assertThat(member.getMemberInfo().getImageUrl()).isEqualTo(imageUrl);
        assertThat(member.getMemberInfo().getRole()).isEqualTo(role);
    }

}
