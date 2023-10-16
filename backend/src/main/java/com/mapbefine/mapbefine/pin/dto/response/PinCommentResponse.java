package com.mapbefine.mapbefine.pin.dto.response;

import com.mapbefine.mapbefine.member.domain.MemberInfo;
import com.mapbefine.mapbefine.pin.domain.PinComment;
import java.time.LocalDateTime;

public record PinCommentResponse(
        Long id,
        String content,
        String creator,
        String creatorImageUrl,
        Long parentPinCommentId,
        boolean canChange,
        LocalDateTime updatedAt
) {

    public static PinCommentResponse ofParentComment(PinComment pinComment, boolean canChange) {
        MemberInfo memberInfo = pinComment.getCreator().getMemberInfo();

       return new PinCommentResponse(
                pinComment.getId(),
                pinComment.getContent(),
                memberInfo.getNickName(),
                memberInfo.getImageUrl(),
                null,
                canChange,
                pinComment.getUpdatedAt()
        );
    }

    public static PinCommentResponse ofChildComment(PinComment pinComment, boolean canChange) {
        MemberInfo memberInfo = pinComment.getCreator().getMemberInfo();

        return new PinCommentResponse(
                pinComment.getId(),
                pinComment.getContent(),
                memberInfo.getNickName(),
                memberInfo.getImageUrl(),
                pinComment.getParentPinComment().getId(),
                canChange,
                pinComment.getUpdatedAt()
        );
    }

}
