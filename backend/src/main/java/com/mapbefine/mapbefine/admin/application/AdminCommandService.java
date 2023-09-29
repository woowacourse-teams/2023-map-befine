package com.mapbefine.mapbefine.admin.application;

import static com.mapbefine.mapbefine.topic.exception.TopicErrorCode.TOPIC_NOT_FOUND;

import com.mapbefine.mapbefine.atlas.domain.AtlasRepository;
import com.mapbefine.mapbefine.bookmark.domain.BookmarkRepository;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Status;
import com.mapbefine.mapbefine.permission.domain.PermissionRepository;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.domain.PinImageRepository;
import com.mapbefine.mapbefine.pin.domain.PinRepository;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import com.mapbefine.mapbefine.topic.exception.TopicException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class AdminCommandService {

    private final MemberRepository memberRepository;
    private final TopicRepository topicRepository;
    private final PinRepository pinRepository;
    private final PinImageRepository pinImageRepository;
    private final PermissionRepository permissionRepository;
    private final AtlasRepository atlasRepository;
    private final BookmarkRepository bookmarkRepository;

    public AdminCommandService(
            MemberRepository memberRepository,
            TopicRepository topicRepository,
            PinRepository pinRepository,
            PinImageRepository pinImageRepository,
            PermissionRepository permissionRepository,
            AtlasRepository atlasRepository,
            BookmarkRepository bookmarkRepository
    ) {
        this.memberRepository = memberRepository;
        this.topicRepository = topicRepository;
        this.pinRepository = pinRepository;
        this.pinImageRepository = pinImageRepository;
        this.permissionRepository = permissionRepository;
        this.atlasRepository = atlasRepository;
        this.bookmarkRepository = bookmarkRepository;
    }

    public void blockMember(Long memberId) {
        Member member = findMemberById(memberId);
        member.updateStatus(Status.BLOCKED);

        deleteAllRelatedMember(member);
    }

    private void deleteAllRelatedMember(Member member) {
        List<Long> pinIds = extractPinIdsByMember(member);
        Long memberId = member.getId();

        pinImageRepository.deleteAllByPinIds(pinIds);
        topicRepository.deleteAllByMemberId(memberId);
        pinRepository.deleteAllByMemberId(memberId);
        permissionRepository.deleteAllByMemberId(memberId);
        atlasRepository.deleteAllByMemberId(memberId);
        bookmarkRepository.deleteAllByMemberId(memberId);
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("findMemberByAuthMember; member not found; id=" + id));
    }

    private List<Long> extractPinIdsByMember(Member member) {
        return member.getCreatedPins()
                .stream()
                .map(Pin::getId)
                .toList();
    }

    public void deleteTopic(Long topicId) {
        topicRepository.deleteById(topicId);
    }

    public void deleteTopicImage(Long topicId) {
        Topic topic = findTopicById(topicId);
        topic.removeImage();
    }

    private Topic findTopicById(Long topicId) {
        return topicRepository.findByIdAndIsDeletedFalse(topicId)
                .orElseThrow(() -> new TopicException.TopicNotFoundException(TOPIC_NOT_FOUND, List.of(topicId)));
    }

    public void deletePin(Long pinId) {
        pinRepository.deleteById(pinId);
    }

    public void deletePinImage(Long pinImageId) {
        pinImageRepository.deleteById(pinImageId);
    }

}
