package com.mapbefine.mapbefine.pin.dto.response;

import com.mapbefine.mapbefine.pin.domain.PinComment;
import java.time.LocalDateTime;

public record PinCommentResponse(
        Long id,
        String content,
        String creator,
        Long parentPinCommentId,
        boolean canChange,
        LocalDateTime updatedAt
) {

    public static PinCommentResponse of(PinComment pinComment, boolean canChange) {
        return new PinCommentResponse(
                pinComment.getId(),
                pinComment.getContent(),
                pinComment.getCreator().getMemberInfo().getNickName(),
                pinComment.getParentPinComment().getId(),
                canChange,
                pinComment.getUpdatedAt()
        );
    }

}
