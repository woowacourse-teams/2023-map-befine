package com.mapbefine.mapbefine.member.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.dto.response.MemberDetailResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberDetailResponseTest {

    @Test
    @DisplayName("Member 를 이용하여 MemberDetailResponse 가 정상적으로 생성된다.")
    void createMemberResponse_success() {
        // given
        Member member = MemberFixture.create(
                "member",
                "member@naver.com",
                Role.ADMIN
        );

        // when
        MemberDetailResponse memberDetailResponse = MemberDetailResponse.from(member);

        // then
        assertThat(memberDetailResponse.nickName())
                .isEqualTo(member.getMemberInfo().getNickName());
        assertThat(memberDetailResponse.email())
                .isEqualTo(member.getMemberInfo().getEmail());
        assertThat(memberDetailResponse.imageUrl())
                .isEqualTo(member.getMemberInfo().getImageUrl());
    }

}
