package com.mapbefine.mapbefine.permission.application;

import static com.mapbefine.mapbefine.permission.exception.PermissionErrorCode.PERMISSION_NOT_FOUND;

import com.mapbefine.mapbefine.permission.domain.Permission;
import com.mapbefine.mapbefine.permission.domain.PermissionRepository;
import com.mapbefine.mapbefine.permission.dto.response.PermissionMemberDetailResponse;
import com.mapbefine.mapbefine.permission.dto.response.PermissionedMemberResponse;
import com.mapbefine.mapbefine.permission.dto.response.TopicAccessDetailResponse;
import com.mapbefine.mapbefine.permission.exception.PermissionException.PermissionNotFoundException;
import com.mapbefine.mapbefine.topic.domain.Publicity;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import com.mapbefine.mapbefine.topic.domain.TopicStatus;
import com.mapbefine.mapbefine.topic.exception.TopicErrorCode;
import com.mapbefine.mapbefine.topic.exception.TopicException.TopicNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PermissionQueryService {

    private final PermissionRepository permissionRepository;
    private final TopicRepository topicRepository;

    public PermissionQueryService(PermissionRepository permissionRepository, TopicRepository topicRepository) {
        this.permissionRepository = permissionRepository;
        this.topicRepository = topicRepository;
    }

    public TopicAccessDetailResponse findTopicAccessDetailById(Long topicId) {
        Publicity publicity = findTopicPublicityById(topicId);
        /// TODO: 2023/09/15 이럴거면 topic.getPermissions 로 하는 게 나을 수도 있나? TopicController 에서 하는 게 더 자연스러운 것 같기도..
        List<PermissionedMemberResponse> permissionedMembers = permissionRepository.findAllByTopicId(topicId)
                .stream()
                .map(PermissionedMemberResponse::from)
                .toList();

        return new TopicAccessDetailResponse(publicity, permissionedMembers);
    }

    private Publicity findTopicPublicityById(Long topicId) {
        return topicRepository.findById(topicId)
                .map(Topic::getTopicStatus)
                .map(TopicStatus::getPublicity)
                .orElseThrow(() -> new TopicNotFoundException(TopicErrorCode.TOPIC_NOT_FOUND, topicId));
    }

    @Deprecated(since = "2023.10.06")
    public PermissionMemberDetailResponse findPermissionById(Long permissionId) {
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new PermissionNotFoundException(PERMISSION_NOT_FOUND, permissionId));

        return PermissionMemberDetailResponse.from(permission);
    }
}
