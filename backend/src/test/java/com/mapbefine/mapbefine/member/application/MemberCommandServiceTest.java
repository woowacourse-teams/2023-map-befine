package com.mapbefine.mapbefine.member.application;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.auth.domain.member.User;
import com.mapbefine.mapbefine.common.annotation.ServiceTest;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.member.dto.request.MemberUpdateRequest;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class MemberCommandServiceTest {

    @Autowired
    private MemberCommandService memberCommandService;
    @Autowired
    private MemberRepository memberRepository;
    private Member member;
    private AuthMember authMember;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(MemberFixture.create("member", "member@gmail.com", Role.USER));
        authMember = new User(member.getId(), emptyList(), emptyList());
    }

    @Test
    @DisplayName("회원 정보를 수정한다.")
    void updateInfoById_Success() {
        // given
        String expected = "new nickname";
        MemberUpdateRequest request = new MemberUpdateRequest(expected);

        // when
        memberCommandService.updateInfoById(authMember, request);
        memberRepository.flush();

        // then
        memberRepository.findById(member.getId())
                .map(Member::getMemberInfo)
                .ifPresentOrElse(
                        actual -> {
                            assertThat(actual).usingRecursiveComparison()
                                    .ignoringFields("nickName")
                                    .isEqualTo(member.getMemberInfo());
                            assertThat(actual.getNickName()).isEqualTo(expected);
                        },
                        Assertions::fail
                );
    }

    @Test
    @DisplayName("존재하지 않는 회원의 정보를 수정하면 예외가 발생한다.")
    void updateInfoById_FailByNotExistingMember() {
        // given
        MemberUpdateRequest request = new MemberUpdateRequest("new nickname");
        User notExistingMember = new User(100L, emptyList(), emptyList());

        // when, then
        assertThatThrownBy(() -> memberCommandService.updateInfoById(notExistingMember, request))
                .isInstanceOf(NoSuchElementException.class);
    }

}
