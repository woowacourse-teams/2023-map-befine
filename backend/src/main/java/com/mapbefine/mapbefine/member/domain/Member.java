package com.mapbefine.mapbefine.member.domain;

import static lombok.AccessLevel.PROTECTED;

import com.mapbefine.mapbefine.common.entity.BaseTimeEntity;
import com.mapbefine.mapbefine.common.entity.Image;
import com.mapbefine.mapbefine.common.util.RegexUtil;
import com.mapbefine.mapbefine.topic.domain.Topic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Member extends BaseTimeEntity {

    private static final int MAX_NAME_LENGTH = 20;
    private static final String VALID_EMAIL_URL_REGEX = "^[a-zA-Z]+@[a-zA-Z]+\\.[a-zA-Z]{2,}$";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Image imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "creator")
    private List<Topic> createdTopic = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<MemberTopicPermission> topicsWithPermission = new ArrayList<>();

    private Member(
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

    public static Member of(
            String name,
            String email,
            String imageUrl,
            Role role
    ) {
        validateName(name);
        validateEmail(email);
        validateRole(role);

        return new Member(
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
        if (!RegexUtil.matches(VALID_EMAIL_URL_REGEX, email)) {
            throw new IllegalArgumentException("올바르지 않은 이메일 형식입니다.");
        }
    }

    private static void validateRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("role null");
        }
    }

    public void addTopic(Topic topic) {
        createdTopic.add(topic);
    }

    public String getRoleKey() {
        return role.getKey();
    }

    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    public boolean isUser() {
        return role == Role.USER;
    }

    public String getImageUrl() {
        return imageUrl.getImageUrl();
    }

    public List<Topic> getTopicsWithPermission() {
        return topicsWithPermission.stream()
                .map(MemberTopicPermission::getTopic)
                .toList();
    }

}
