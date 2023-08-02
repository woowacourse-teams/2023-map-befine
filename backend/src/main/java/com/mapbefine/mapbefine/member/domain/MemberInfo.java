package com.mapbefine.mapbefine.member.domain;

import static lombok.AccessLevel.PROTECTED;

import com.mapbefine.mapbefine.common.entity.Image;
import com.mapbefine.mapbefine.common.util.RegexUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = PROTECTED)
@Getter
public class MemberInfo {

    private static final int MAX_NAME_LENGTH = 20;
    private static final String VALID_EMAIL_URL_REGEX = "^[a-zA-Z]+@[a-zA-Z]+\\.[a-zA-Z]{2,}$";

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Image imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private MemberInfo(
            String name,
            String email,
            Image imageUrl,
            Role role
    ) {
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.role = role;
    }

    public static MemberInfo of(
            String name,
            String email,
            String imageUrl,
            Role role
    ) {
        validateName(name);
        validateEmail(email);
        validateRole(role);

        return new MemberInfo(
                name,
                email,
                Image.of(imageUrl),
                role
        );
    }

    public void update(String name, String email, String imageUrl) {
        validateName(name);
        validateEmail(email);

        this.name = name;
        this.email = email;
        this.imageUrl = Image.of(imageUrl);
    }

    private static void validateName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name null");
        }
        if (name.isBlank() || name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("이름 길이 이상");
        }
    }

    private static void validateEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("email null");
        }

        if (!RegexUtil.matches(VALID_EMAIL_URL_REGEX, email)) {
            throw new IllegalArgumentException("올바르지 않은 이메일 형식입니다.");
        }
    }

    private static void validateRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("role null");
        }
    }

    public String getImageUrl() {
        return imageUrl.getImageUrl();
    }

}
