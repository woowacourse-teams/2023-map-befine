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

    private static final int MAX_NAME_LENGTH = 20;
    private static final String VALID_EMAIL_URL_REGEX = "^[a-zA-Z]+@[a-zA-Z]+\\.[a-zA-Z]{2,}$";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private MemberInfo memberInfo;

    @OneToMany(mappedBy = "creator")
    private List<Topic> createdTopic = new ArrayList<>();

    @OneToMany(mappedBy = "creator")
    private List<Pin> createdPin = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<MemberTopicPermission> topicsWithPermission = new ArrayList<>();

    private Member(
            MemberInfo memberInfo
    ) {
        this.memberInfo = memberInfo;
    }

    public static Member of(
            String name,
            String email,
            String imageUrl,
            Role role
    ) {
        MemberInfo memberInfo = MemberInfo.of(name, email, imageUrl, role);
        return new Member(memberInfo);
    }

    public void update(String name, String email, String imageUrl) {
        memberInfo.update(name, email, imageUrl);
    }

    public void addTopic(Topic topic) {
        createdTopic.add(topic);
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

    public List<Topic> getTopicsWithPermission() {
        return topicsWithPermission.stream()
                .map(MemberTopicPermission::getTopic)
                .toList();
    }

}
