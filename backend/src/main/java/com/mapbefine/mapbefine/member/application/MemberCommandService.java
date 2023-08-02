package com.mapbefine.mapbefine.member.application;

import com.mapbefine.mapbefine.common.entity.Image;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.dto.request.MemberCreateRequest;
import com.mapbefine.mapbefine.member.dto.response.MemberDetailResponse;
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

    public Long save(MemberCreateRequest request) {
        Member member = Member.of(
                request.name(),
                request.email(),
                request.imageUrl(),
                request.role()
        );

        return memberRepository.save(member)
                .getId();
    }

}
