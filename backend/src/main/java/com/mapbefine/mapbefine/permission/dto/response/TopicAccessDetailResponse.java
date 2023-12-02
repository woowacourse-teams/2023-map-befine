package com.mapbefine.mapbefine.permission.dto.response;

import com.mapbefine.mapbefine.member.dto.response.MemberResponse;
import com.mapbefine.mapbefine.topic.domain.Publicity;
import java.util.List;

public record TopicAccessDetailResponse(
        Publicity publicity,
        List<MemberResponse> permittedMembers
) {
}
