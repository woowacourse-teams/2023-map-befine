package com.mapbefine.mapbefine.member.application;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.MemberTopicPermission;
import com.mapbefine.mapbefine.member.domain.MemberTopicPermissionRepository;
import com.mapbefine.mapbefine.member.dto.response.MemberDetailResponse;
import com.mapbefine.mapbefine.member.dto.response.MemberResponse;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.domain.PinRepository;
import com.mapbefine.mapbefine.pin.dto.response.PinResponse;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberQueryService {

    private final MemberRepository memberRepository;
    private final TopicRepository topicRepository;
    private final PinRepository pinRepository;
    private final MemberTopicPermissionRepository memberTopicPermissionRepository;


    public MemberQueryService(
            MemberRepository memberRepository,
            TopicRepository topicRepository,
            PinRepository pinRepository,
            MemberTopicPermissionRepository memberTopicPermissionRepository
    ) {
        this.memberRepository = memberRepository;
        this.topicRepository = topicRepository;
        this.pinRepository = pinRepository;
        this.memberTopicPermissionRepository = memberTopicPermissionRepository;
    }

    public MemberDetailResponse findById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);

        return MemberDetailResponse.from(member);
    }

    public List<MemberResponse> findAll() {
        return memberRepository.findAll()
                .stream()
                .map(MemberResponse::from)
                .toList();
    }

    public List<TopicResponse> findTopicsByMember(AuthMember authMember) {
        validateNull(authMember.getMemberId());
        Member member = memberRepository.findById(authMember.getMemberId())
                .orElseThrow(NoSuchElementException::new);
        List<Topic> topicsByCreator = topicRepository.findByCreator(member);

        return topicsByCreator.stream()
                .map(TopicResponse::from)
                .toList();
    }

    public List<PinResponse> findPinsByMember(AuthMember authMember) {
        validateNull(authMember.getMemberId());
        Member creator = memberRepository.findById(authMember.getMemberId())
                .orElseThrow(NoSuchElementException::new);
        List<Pin> pinsByCreator = pinRepository.findByCreator(creator);

        return pinsByCreator.stream()
                .map(PinResponse::from)
                .toList();
    }

    private void validateNull(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id 는 null 일 수 없습니다.");
        }
    }

    public List<MemberResponse> findAllWithPermission(Long topicId) {
        return memberTopicPermissionRepository.findByTopicId(topicId)
                .stream()
                .map(MemberTopicPermission::getMember)
                .map(MemberResponse::from)
                .toList();
    }

    public MemberDetailResponse findMemberTopicPermissionById(Long permissionId) {
        MemberTopicPermission memberTopicPermission = memberTopicPermissionRepository.findById(permissionId)
                .orElseThrow(NoSuchElementException::new);

        return MemberDetailResponse.from(memberTopicPermission.getMember());
    }
}
