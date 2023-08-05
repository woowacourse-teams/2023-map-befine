package com.mapbefine.mapbefine.member.application;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
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

    public MemberQueryService(
            MemberRepository memberRepository,
            TopicRepository topicRepository,
            PinRepository pinRepository
    ) {
        this.memberRepository = memberRepository;
        this.topicRepository = topicRepository;
        this.pinRepository = pinRepository;
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

    public List<TopicResponse> findTopicsByMember(final AuthMember authMember) {
        Long memberId = authMember.getMemberId();
        List<Topic> topicsByCreator = topicRepository.findByCreatorId(memberId);

        return topicsByCreator.stream()
                .map(TopicResponse::from)
                .toList();
    }

    public List<PinResponse> findPinsByMember(final AuthMember authMember) {
        Long memberId = authMember.getMemberId();
        List<Pin> pinsByCreator = pinRepository.findByCreatorId(memberId);

        return pinsByCreator.stream()
                .map(PinResponse::from)
                .toList();
    }
}
