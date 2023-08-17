package com.mapbefine.mapbefine.permission.application;

import static com.mapbefine.mapbefine.permission.exception.PermissionErrorCode.PERMISSION_NOT_FOUND;

import com.mapbefine.mapbefine.permission.domain.Permission;
import com.mapbefine.mapbefine.permission.domain.PermissionRepository;
import com.mapbefine.mapbefine.permission.dto.response.PermissionDetailResponse;
import com.mapbefine.mapbefine.permission.dto.response.PermissionResponse;
import com.mapbefine.mapbefine.permission.exception.PermissionException.PermissionNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PermissionQueryService {

    private final PermissionRepository permissionRepository;

    public PermissionQueryService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public List<PermissionResponse> findAllTopicPermissions(Long topicId) {
        return permissionRepository.findAllByTopicId(topicId)
                .stream()
                .map(PermissionResponse::from)
                .toList();
    }

    public PermissionDetailResponse findPermissionById(Long permissionId) {
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new PermissionNotFoundException(PERMISSION_NOT_FOUND));

        return PermissionDetailResponse.from(permission);
    }
}
