package com.mapbefine.mapbefine.admin.application;

import com.mapbefine.mapbefine.admin.dto.AdminMemberDetailResponse;
import com.mapbefine.mapbefine.admin.dto.AdminMemberResponse;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.topic.domain.Topic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class AdminQueryService {

    private final MemberRepository memberRepository;

    public AdminQueryService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<AdminMemberResponse> findAllMemberDetails() {
        List<Member> members = memberRepository.findAllByMemberInfoRole(Role.USER);

        return members.stream()
                .map(AdminMemberResponse::from)
                .toList();
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("findMemberByAuthMember; member not found; id=" + id));
    }

    public AdminMemberDetailResponse findMemberDetail(Long memberId) {
        Member findMember = findMemberById(memberId);
        List<Topic> topics = findMember.getCreatedTopics();
        List<Pin> pins = findMember.getCreatedPins();

        return AdminMemberDetailResponse.of(findMember, topics, pins);
    }

}
