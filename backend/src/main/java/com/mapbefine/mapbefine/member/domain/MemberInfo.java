package com.mapbefine.mapbefine.member.domain;

import static com.mapbefine.mapbefine.member.exception.MemberErrorCode.ILLEGAL_EMAIL_NULL;
import static com.mapbefine.mapbefine.member.exception.MemberErrorCode.ILLEGAL_EMAIL_PATTERN;
import static com.mapbefine.mapbefine.member.exception.MemberErrorCode.ILLEGAL_NICKNAME_LENGTH;
import static com.mapbefine.mapbefine.member.exception.MemberErrorCode.ILLEGAL_NICKNAME_NULL;
import static lombok.AccessLevel.PROTECTED;

import com.mapbefine.mapbefine.common.entity.Image;
import com.mapbefine.mapbefine.common.util.RegexUtil;
import com.mapbefine.mapbefine.member.exception.MemberException.MemberBadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = PROTECTED)
@Getter
public class MemberInfo {

    private static final String VALID_EMAIL_URL_REGEX = "^[a-zA-Z0-9]+@[a-zA-Z]+\\.[a-zA-Z]{2,}$";
    private static final int NICKNAME_LEGTH = 20;

    @Column(nullable = false, length = 20, unique = true)
    private String nickName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Image imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    private MemberInfo(
            String nickName,
            String email,
            Image imageUrl,
            Role role,
            Status status
    ) {
        this.nickName = nickName;
        this.email = email;
        this.imageUrl = imageUrl;
        this.role = role;
        this.status = status;
    }

    public static MemberInfo of(
            String nickName,
            String email,
            String imageUrl,
            Role role,
            Status status
    ) {
        validateNickName(nickName);
        validateEmail(email);
        validateRole(role);
        validateStatus(status);

        return new MemberInfo(
                nickName,
                email,
                Image.from(imageUrl),
                role,
                status
        );
    }

    private static void validateNickName(String nickName) {
        if (Objects.isNull(nickName)) {
            throw new MemberBadRequestException(ILLEGAL_NICKNAME_NULL);
        }
        if (nickName.isBlank() || nickName.length() > NICKNAME_LEGTH) {
            throw new MemberBadRequestException(ILLEGAL_NICKNAME_LENGTH);
        }
    }

    private static void validateEmail(String email) {
        if (Objects.isNull(email)) {
            throw new MemberBadRequestException(ILLEGAL_EMAIL_NULL);
        }

        if (!RegexUtil.matches(VALID_EMAIL_URL_REGEX, email)) {
            throw new MemberBadRequestException(ILLEGAL_EMAIL_PATTERN);
        }
    }

    private static void validateRole(Role role) {
        if (Objects.isNull(role)) {
            throw new IllegalArgumentException("validateRole; member role is null;");
        }
    }

    private static void validateStatus(Status status) {
        if (Objects.isNull(status)) {
            throw new IllegalArgumentException("validateStatus; member status is null;");
        }
    }

    public MemberInfo createUpdatedMemberInfo(String nickName) {

        return MemberInfo.of(nickName, this.email, this.imageUrl.getImageUrl(), this.role, this.status);
    }

    public MemberInfo createUpdatedMemberInfo(Status status) {

        return MemberInfo.of(this.nickName, this.email, this.imageUrl.getImageUrl(), this.role, status);
    }

    public String getImageUrl() {
        return imageUrl.getImageUrl();
    }

}
