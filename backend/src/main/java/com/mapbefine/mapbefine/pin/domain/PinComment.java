package com.mapbefine.mapbefine.pin.domain;

import static com.mapbefine.mapbefine.pin.exception.PinCommentErrorCode.ILLEGAL_CONTENT_LENGTH;
import static com.mapbefine.mapbefine.pin.exception.PinCommentErrorCode.ILLEGAL_CONTENT_NULL;
import static lombok.AccessLevel.PROTECTED;

import com.mapbefine.mapbefine.common.entity.BaseTimeEntity;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.pin.exception.PinCommentException.PinCommentBadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class PinComment extends BaseTimeEntity {

    private static final int MAX_CONTENT_LENGTH = 1000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pin_id")
    private Pin pin;

    @ManyToOne
    @JoinColumn(name = "parent_pin_comment_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PinComment parentPinComment;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member creator;

    @Lob
    @Column(nullable = false, length = 1000)
    private String content;

    public PinComment(
            Pin pin,
            PinComment parentPinComment,
            Member creator,
            String content
    ) {
        this.pin = pin;
        this.parentPinComment = parentPinComment;
        this.creator = creator;
        this.content = content;
    }

    public static PinComment of(
            Pin pin,
            PinComment parentPinComment,
            Member creator,
            String content
    ) {
        validateContent(content);

        return new PinComment(pin, parentPinComment, creator, content);
    }

    private static void validateContent(String content) {
        if (Objects.isNull(content)) {
            throw new PinCommentBadRequestException(ILLEGAL_CONTENT_NULL);
        }
        if (content.isBlank() || content.length() > MAX_CONTENT_LENGTH) {
            throw new PinCommentBadRequestException(ILLEGAL_CONTENT_LENGTH);
        }
    }

    public void updateContent(String content) {
        validateContent(content);
        this.content = content;
    }

    public boolean isParentComment() {
        return Objects.isNull(parentPinComment);
    }

}
