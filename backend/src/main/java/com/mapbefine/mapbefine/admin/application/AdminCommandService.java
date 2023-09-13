package com.mapbefine.mapbefine.admin.application;

import static com.mapbefine.mapbefine.permission.exception.PermissionErrorCode.PERMISSION_FORBIDDEN_BY_NOT_ADMIN;
import static com.mapbefine.mapbefine.topic.exception.TopicErrorCode.TOPIC_NOT_FOUND;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Status;
import com.mapbefine.mapbefine.member.exception.MemberErrorCode;
import com.mapbefine.mapbefine.member.exception.MemberException.MemberNotFoundException;
import com.mapbefine.mapbefine.permission.exception.PermissionException.PermissionForbiddenException;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.domain.PinImageRepository;
import com.mapbefine.mapbefine.pin.domain.PinRepository;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicInfo;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import com.mapbefine.mapbefine.topic.exception.TopicException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AdminCommandService {

    private final MemberRepository memberRepository;
    private final TopicRepository topicRepository;
    private final PinRepository pinRepository;
    private final PinImageRepository pinImageRepository;

    public AdminCommandService(
            MemberRepository memberRepository,
            TopicRepository topicRepository,
            PinRepository pinRepository,
            PinImageRepository pinImageRepository
    ) {
        this.memberRepository = memberRepository;
        this.topicRepository = topicRepository;
        this.pinRepository = pinRepository;
        this.pinImageRepository = pinImageRepository;
    }

    public void blockMember(AuthMember authMember, Long memberId) {
        Member admin = findMemberById(authMember.getMemberId());

        validateAdminPermission(admin);

        Member member = findMemberById(memberId);
        member.updateStatus(Status.BLOCKED);
        List<Long> pinIds = extractPinIdsByMember(member);

        topicRepository.deleteAllByMemberId(memberId);
        pinRepository.deleteAllByMemberId(memberId);
        pinImageRepository.deleteAllByPinIds(pinIds);
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(MemberErrorCode.MEMBER_NOT_FOUND, id));
    }

    private void validateAdminPermission(Member member) {
        if (member.isAdmin()) {
            return;
        }

        throw new PermissionForbiddenException(PERMISSION_FORBIDDEN_BY_NOT_ADMIN);
    }

    private List<Long> extractPinIdsByMember(Member member) {
        return member.getCreatedPins()
                .stream()
                .map(Pin::getId)
                .toList();
    }

    public void deleteTopic(AuthMember authMember, Long topicId) {
        Member member = findMemberById(authMember.getMemberId());

        validateAdminPermission(member);

        topicRepository.deleteById(topicId);
    }

    public void deleteTopicImage(AuthMember authMember, Long topicId) {
        Member member = findMemberById(authMember.getMemberId());

        validateAdminPermission(member);

        Topic topic = findTopicById(topicId);
        TopicInfo topicInfo = topic.getTopicInfo();

        topic.updateTopicInfo(topicInfo.getName(), topicInfo.getDescription(), "");
    }

    private Topic findTopicById(Long topicId) {
        return topicRepository.findById(topicId)
                .orElseThrow(() -> new TopicException.TopicNotFoundException(TOPIC_NOT_FOUND, List.of(topicId)));
    }

    public void deletePin(AuthMember authMember, Long pinId) {
        Member member = findMemberById(authMember.getMemberId());

        validateAdminPermission(member);

        pinRepository.deleteById(pinId);
    }

    public void deletePinImage(AuthMember authMember, Long pinImageId) {
        Member member = findMemberById(authMember.getMemberId());

        validateAdminPermission(member);

        pinImageRepository.deleteById(pinImageId);
    }

}
