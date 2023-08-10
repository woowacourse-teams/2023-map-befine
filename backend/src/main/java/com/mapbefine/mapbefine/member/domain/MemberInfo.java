package com.mapbefine.mapbefine.member.domain;

import static lombok.AccessLevel.PROTECTED;

import com.mapbefine.mapbefine.common.entity.Image;
import com.mapbefine.mapbefine.common.util.RegexUtil;
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

    private static final int MAX_NICK_NAME_LENGTH = 20;
    private static final String VALID_EMAIL_URL_REGEX = "^[a-zA-Z0-9]+@[a-zA-Z]+\\.[a-zA-Z]{2,}$";

    @Column(nullable = false, length = 20, unique = true) // Nick Name
    private String nickName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private Image imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private MemberInfo(
            String nickName,
            String email,
            Image imageUrl,
            Role role
    ) {
        this.nickName = nickName;
        this.email = email;
        this.imageUrl = imageUrl;
        this.role = role;
    }

    public static MemberInfo of(
            String nickName,
            String email,
            String imageUrl,
            Role role
    ) {
        validateNickName(nickName);
        validateEmail(email);
        validateRole(role);

        return new MemberInfo(
                nickName,
                email,
                Image.from(imageUrl),
                role
        );
    }

    private static void validateNickName(String nickName) {
        if (Objects.isNull(nickName)) {
            throw new IllegalArgumentException("닉네임은 필수로 입력해야합니다.");
        }
        if (nickName.isBlank() || nickName.length() > MAX_NICK_NAME_LENGTH) {
            throw new IllegalArgumentException("닉네임 길이는 최소 1 자에서 " + MAX_NICK_NAME_LENGTH + " 자여야 합니다.");
        }
    }

    private static void validateEmail(String email) {
        if (Objects.isNull(email)) {
            throw new IllegalArgumentException("이메일은 필수로 입력해야합니다.");
        }

        if (!RegexUtil.matches(VALID_EMAIL_URL_REGEX, email)) {
            throw new IllegalArgumentException("올바르지 않은 이메일 형식입니다.");
        }
    }

    private static void validateRole(Role role) {
        if (Objects.isNull(role)) {
            throw new IllegalArgumentException("역할은 필수로 입력해야합니다.");
        }
    }

    public String getImageUrl() {
        return imageUrl.getImageUrl();
    }

}
