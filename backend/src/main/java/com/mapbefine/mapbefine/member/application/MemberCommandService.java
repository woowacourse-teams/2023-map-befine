package com.mapbefine.mapbefine.member.application;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.dto.request.MemberUpdateRequest;
import com.mapbefine.mapbefine.member.exception.MemberErrorCode;
import com.mapbefine.mapbefine.member.exception.MemberException.MemberConflictException;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberCommandService {

    private final MemberRepository memberRepository;

    public MemberCommandService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void updateInfoById(AuthMember authMember, MemberUpdateRequest request) {
        Member member = findMemberById(authMember.getMemberId());
        String nickName = request.nickName();

        validateNicknameDuplicated(nickName);

        member.updateNickName(nickName);
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("findMemberById; memberId=" + memberId));
    }

    private void validateNicknameDuplicated(String nickName) {
        if (memberRepository.existsByMemberInfoNickName(nickName)) {
            throw new MemberConflictException(MemberErrorCode.ILLEGAL_NICKNAME_ALREADY_EXISTS, nickName);
        }
    }

}
