package com.mapbefine.mapbefine.member.application;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.dto.response.MemberDetailResponse;
import com.mapbefine.mapbefine.member.dto.response.MemberResponse;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.domain.PinRepository;
import com.mapbefine.mapbefine.pin.dto.response.PinResponse;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import com.mapbefine.mapbefine.topic.domain.TopicWithBookmarkStatus;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
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

    // TODO: 2023/08/14 해당 메서드는 ADMIN만 접근 가능하게 리팩터링 하기
    public List<MemberResponse> findAll() {
        return memberRepository.findAll()
                .stream()
                .map(MemberResponse::from)
                .toList();
    }

    // TODO: 2023/08/14 해당 메서드는 TopicQueryService로 옮기기
    public List<TopicResponse> findTopicsByMember(AuthMember authMember) {
        validateNonExistsMember(authMember);
        List<TopicWithBookmarkStatus> topicsWithBookmarkStatus =
                topicRepository.findAllWithBookmarkStatusByMemberId(authMember.getMemberId());

        return topicsWithBookmarkStatus.stream()
                .map(topicWithBookmarkStatus -> TopicResponse.from(
                        topicWithBookmarkStatus.getTopic(),
                        topicWithBookmarkStatus.getIsBookmarked()
                ))
                .toList();
    }

    // TODO: 2023/08/14 해당 메서드는 PinQueryService로 옮기기
    public List<PinResponse> findPinsByMember(AuthMember authMember) {
        validateNonExistsMember(authMember);
        List<Pin> pinsByCreator = pinRepository.findByCreatorId(authMember.getMemberId());

        return pinsByCreator.stream()
                .map(PinResponse::from)
                .toList();
    }

    private void validateNonExistsMember(AuthMember authMember) {
        if (Objects.isNull(authMember.getMemberId())) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }
    }

}
