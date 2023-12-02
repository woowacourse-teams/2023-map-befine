package com.mapbefine.mapbefine.permission.application;

import com.mapbefine.mapbefine.member.dto.response.MemberResponse;
import com.mapbefine.mapbefine.permission.domain.PermissionRepository;
import com.mapbefine.mapbefine.permission.dto.response.TopicAccessDetailResponse;
import com.mapbefine.mapbefine.topic.domain.Publicity;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import com.mapbefine.mapbefine.topic.domain.TopicStatus;
import com.mapbefine.mapbefine.topic.exception.TopicErrorCode;
import com.mapbefine.mapbefine.topic.exception.TopicException.TopicNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PermissionQueryService {

    private final PermissionRepository permissionRepository;
    private final TopicRepository topicRepository;

    public PermissionQueryService(
            PermissionRepository permissionRepository,
            TopicRepository topicRepository
    ) {
        this.permissionRepository = permissionRepository;
        this.topicRepository = topicRepository;
    }

    public TopicAccessDetailResponse findTopicAccessDetailById(Long topicId) {
        Publicity publicity = findTopicPublicityById(topicId);
        List<MemberResponse> permittedMembers = permissionRepository.findAllPermittedMembersByTopicId(topicId);

        return new TopicAccessDetailResponse(publicity, permittedMembers);
    }

    private Publicity findTopicPublicityById(Long topicId) {
        return topicRepository.findById(topicId)
                .map(Topic::getTopicStatus)
                .map(TopicStatus::getPublicity)
                .orElseThrow(() -> new TopicNotFoundException(TopicErrorCode.TOPIC_NOT_FOUND, topicId));
    }

}
