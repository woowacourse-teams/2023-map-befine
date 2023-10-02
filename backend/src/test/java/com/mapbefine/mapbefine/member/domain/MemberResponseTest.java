package com.mapbefine.mapbefine.member.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.dto.response.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberResponseTest {

    @Test
    @DisplayName("Member 를 이용하여 MemberResponse 가 정상적으로 생성된다.")
    void createMemberResponse_success() {
        // given
        Member member = MemberFixture.create(
                "member",
                "member@naver.com",
                Role.ADMIN
        );

        // when
        MemberResponse memberResponse = MemberResponse.from(member);

        // then
        assertThat(memberResponse.nickName())
                .isEqualTo(member.getMemberInfo().getNickName());
    }

}
