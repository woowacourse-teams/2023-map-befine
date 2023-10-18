package com.mapbefine.mapbefine.pin.dto.response;

import com.mapbefine.mapbefine.member.domain.MemberInfo;
import com.mapbefine.mapbefine.pin.domain.PinComment;
import java.time.LocalDateTime;
import java.util.Optional;

public record PinCommentResponse(
        Long id,
        String content,
        String creator,
        String creatorImageUrl,
        Long parentPinCommentId,
        boolean canChange,
        LocalDateTime updatedAt
) {

    public static PinCommentResponse of(PinComment pinComment, boolean canChange) {
        MemberInfo memberInfo = pinComment.getCreator().getMemberInfo();
        Optional<Long> parentPinCommentId = pinComment.getParentPinCommentId();

        return new PinCommentResponse(
                 pinComment.getId(),
                 pinComment.getContent(),
                 memberInfo.getNickName(),
                 memberInfo.getImageUrl(),
                 parentPinCommentId.orElse(null),
                 canChange,
                 pinComment.getUpdatedAt()
         );
    }

}
