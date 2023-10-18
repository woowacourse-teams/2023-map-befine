package com.mapbefine.mapbefine.pin;

import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.domain.PinComment;

public class PinCommentFixture {

    public static PinComment createParentComment(
            Pin pin,
            Member creator
    ) {
        return PinComment.ofParentPinComment(
                pin,
                creator,
                "댓글"
        );
    }

    public static PinComment createChildComment(
            Pin pin,
            Member creator,
            PinComment savedParentPinComment
    ) {
        return PinComment.ofChildPinComment(
                pin,
                savedParentPinComment,
                creator,
                "댓글"
        );
    }

}
