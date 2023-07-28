package com.mapbefine.mapbefine.entity;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
@Table(name = "member")
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
    private List<TopicPermissionMember> topicsWithPermission = new ArrayList<>();

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
        return role.getKey();
    }

    public List<Topic> getAllTopicsWithPermission() {
        List<Topic> allTopicsWithPermission = topicsWithPermission.stream()
                .map(TopicPermissionMember::getTopic)
                .collect(Collectors.toList());

        allTopicsWithPermission.addAll(createdTopic);
        return allTopicsWithPermission;
    }

    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", role=" + role +
                ", createdTopic=" + createdTopic +
                ", topicsWithPermission=" + topicsWithPermission +
                '}';
    }
}
