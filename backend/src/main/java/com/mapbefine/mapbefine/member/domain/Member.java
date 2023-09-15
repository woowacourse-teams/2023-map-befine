package com.mapbefine.mapbefine.member.domain;

import static java.util.UUID.randomUUID;
import static lombok.AccessLevel.PROTECTED;

import com.mapbefine.mapbefine.atlas.domain.Atlas;
import com.mapbefine.mapbefine.bookmark.domain.Bookmark;
import com.mapbefine.mapbefine.common.entity.BaseTimeEntity;
import com.mapbefine.mapbefine.permission.domain.Permission;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.topic.domain.Topic;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
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

    private static final String DEFAULT_NICKNAME_PREFIX = "모험가";
    private static final int DEFAULT_NICKNAME_SUFFIX_LENGTH = 7;
    private static final int NICKNAME_MAX_LENGTH = 20;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private MemberInfo memberInfo;

    @Embedded
    private OauthId oauthId;

    @OneToMany(mappedBy = "creator")
    private List<Topic> createdTopics = new ArrayList<>();

    @OneToMany(mappedBy = "creator")
    private List<Pin> createdPins = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Permission> topicsWithPermissions = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Bookmark> bookmarks = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Atlas> atlantes = new ArrayList<>();

    private Member(MemberInfo memberInfo, OauthId oauthId) {
        this.memberInfo = memberInfo;
        this.oauthId = oauthId;
    }

    public static Member of(
            String nickName,
            String email,
            String imageUrl,
            Role role,
            Status status,
            OauthId oauthId
    ) {
        MemberInfo memberInfo = MemberInfo.of(
                nickName,
                email,
                imageUrl,
                role,
                status
        );

        return new Member(memberInfo, oauthId);
    }

    public static Member ofRandomNickname(
            String nickname,
            String email,
            String imageUrl,
            Role role,
            OauthId oauthId
    ) {
        String nickName = createNickname(nickname);

        return Member.of(nickName, email, imageUrl, role, Status.NORMAL, oauthId);
    }

    private static String createNickname(String nickname) {
        if (nickname.length() > NICKNAME_MAX_LENGTH - DEFAULT_NICKNAME_SUFFIX_LENGTH) {
            return DEFAULT_NICKNAME_PREFIX + createNicknameSuffix();
        }
        return nickname + createNicknameSuffix();
    }

    private static String createNicknameSuffix() {
        return randomUUID()
                .toString()
                .replaceAll("-", "")
                .substring(0, DEFAULT_NICKNAME_SUFFIX_LENGTH);
    }

    public void update(
            String nickName,
            String email,
            String imageUrl
    ) {
        memberInfo = MemberInfo.of(
                nickName,
                email,
                imageUrl,
                memberInfo.getRole(),
                memberInfo.getStatus()
        );
    }

    public void addTopic(Topic topic) {
        createdTopics.add(topic);
    }

    public void addPin(Pin pin) {
        createdPins.add(pin);
    }

    public void addPermission(Permission permission) {
        topicsWithPermissions.add(permission);
    }

    public void addBookmark(Bookmark bookmark) {
        bookmarks.add(bookmark);
    }

    public void addAtlas(Atlas atlas) {
        atlantes.add(atlas);
    }

    public boolean isAdmin() {
        return memberInfo.getRole() == Role.ADMIN;
    }

    public boolean isUser() {
        return memberInfo.getRole() == Role.USER;
    }

    public List<Topic> getTopicsWithPermissions() {
        return topicsWithPermissions.stream()
                .map(Permission::getTopic)
                .toList();
    }

    public boolean isNormalStatus() {
        return memberInfo.getStatus() == Status.NORMAL;
    }

    public void updateStatus(Status status) {
        memberInfo = MemberInfo.of(
                memberInfo.getNickName(),
                memberInfo.getEmail(),
                memberInfo.getImageUrl(),
                memberInfo.getRole(),
                status
        );
    }
}
