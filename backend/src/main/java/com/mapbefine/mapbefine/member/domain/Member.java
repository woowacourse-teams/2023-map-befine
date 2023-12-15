package com.mapbefine.mapbefine.member.domain;

import com.mapbefine.mapbefine.atlas.domain.Atlas;
import com.mapbefine.mapbefine.common.entity.BaseTimeEntity;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.topic.domain.Topic;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static java.util.UUID.randomUUID;
import static lombok.AccessLevel.PROTECTED;

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
                .replace("-", "")
                .substring(0, DEFAULT_NICKNAME_SUFFIX_LENGTH);
    }

    public void updateNickName(
            String nickName
    ) {
        memberInfo = memberInfo.createUpdatedMemberInfo(nickName);
    }

    public void updateStatus(Status status) {
        memberInfo = memberInfo.createUpdatedMemberInfo(status);
    }

    public void addTopic(Topic topic) {
        createdTopics.add(topic);
    }

    public void addPin(Pin pin) {
        createdPins.add(pin);
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

    public boolean isNormalStatus() {
        return memberInfo.getStatus() == Status.NORMAL;
    }
}
