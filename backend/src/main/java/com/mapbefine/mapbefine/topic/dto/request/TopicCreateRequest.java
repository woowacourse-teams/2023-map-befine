package com.mapbefine.mapbefine.topic.dto.request;

import com.mapbefine.mapbefine.topic.domain.PermissionType;
import com.mapbefine.mapbefine.topic.domain.Publicity;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record TopicCreateRequest(
        String name,
        MultipartFile image,
        String description,
        Publicity publicity,
        PermissionType permissionType,
        List<Long> pins
) {

    public static TopicCreateRequest of(
            TopicCreateRequestWithoutImage request,
            MultipartFile image
    ) {
        return new TopicCreateRequest(
                request.name(),
                image,
                request.description(),
                request.publicity(),
                request.permissionType(),
                request.pins()
        );
    }

}
