package com.mapbefine.mapbefine.member.domain;

import static lombok.AccessLevel.PROTECTED;

import com.mapbefine.mapbefine.common.entity.BaseTimeEntity;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private MemberInfo memberInfo;

    @OneToMany(mappedBy = "creator")
    private List<Topic> createdTopics = new ArrayList<>();

    @OneToMany(mappedBy = "creator")
    private List<Pin> createdPins = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<MemberTopicPermission> topicsWithPermissions = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<MemberTopicBookmark> topicsInBookmark = new ArrayList<>();


    private Member(MemberInfo memberInfo) {
        this.memberInfo = memberInfo;
    }

    public static Member of(
            String nickName,
            String email,
            String imageUrl,
            Role role
    ) {
        MemberInfo memberInfo = MemberInfo.of(
                nickName,
                email,
                imageUrl,
                role
        );

        return new Member(memberInfo);
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
                memberInfo.getRole()
        );
    }

    public void addTopic(Topic topic) {
        createdTopics.add(topic);
    }

    public void addPin(Pin pin) {
        createdPins.add(pin);
    }

    public void addMemberTopicPermission(MemberTopicPermission memberTopicPermission) {
        topicsWithPermissions.add(memberTopicPermission);
    }

    public void addTopicInBookmark(MemberTopicBookmark bookmarkedTopic) {
        topicsInBookmark.add(bookmarkedTopic);
    }

    public String getRoleKey() {
        return memberInfo.getRole().getKey();
    }

    public boolean isAdmin() {
        return memberInfo.getRole() == Role.ADMIN;
    }

    public boolean isUser() {
        return memberInfo.getRole() == Role.USER;
    }

    public List<Topic> getTopicsWithPermissions() {
        return topicsWithPermissions.stream()
                .map(MemberTopicPermission::getTopic)
                .toList();
    }

}
