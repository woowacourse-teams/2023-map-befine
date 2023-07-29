package com.mapbefine.mapbefine.entity.member;

import static lombok.AccessLevel.PROTECTED;

import com.mapbefine.mapbefine.entity.BaseEntity;
import com.mapbefine.mapbefine.entity.topic.Topic;
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
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "creator")
    private List<Topic> createdTopic = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<MemberTopicPermission> topicsWithPermission = new ArrayList<>();

    public Member(
            String name,
            String email,
            String imageUrl,
            Role role
    ) {
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.role = role;
    }

    public void update(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public void addTopic(Topic topic) {
        createdTopic.add(topic);
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    public boolean isUser() {
        return role == Role.USER;
    }

}
