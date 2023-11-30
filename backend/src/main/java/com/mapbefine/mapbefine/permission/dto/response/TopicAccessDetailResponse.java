package com.mapbefine.mapbefine.permission.dto.response;

import com.mapbefine.mapbefine.topic.domain.Publicity;
import java.util.List;

public record TopicAccessDetailResponse(
        Publicity publicity,
        List<permittedMemberResponse> permittedMembers
) {
}
