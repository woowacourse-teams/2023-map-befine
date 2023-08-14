package com.mapbefine.mapbefine.topic.domain;

import static lombok.AccessLevel.PROTECTED;

import com.mapbefine.mapbefine.common.entity.BaseTimeEntity;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberTopicPermission;
import com.mapbefine.mapbefine.pin.domain.Pin;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Topic extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private TopicInfo topicInfo;

    @Embedded
    private TopicStatus topicStatus;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member creator;

    @OneToMany(mappedBy = "topic")
    private List<MemberTopicPermission> memberTopicPermissions = new ArrayList<>();

    @OneToMany(mappedBy = "topic", cascade = CascadeType.PERSIST)
    private List<Pin> pins = new ArrayList<>();

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean isDeleted = false;

    private Topic(
            TopicInfo topicInfo,
            TopicStatus topicStatus,
            Member creator
    ) {
        this.topicInfo = topicInfo;
        this.topicStatus = topicStatus;
        this.creator = creator;
    }

    public static Topic createTopicAssociatedWithCreator(
            String name,
            String description,
            String imageUrl,
            Publicity publicity,
            PermissionType permissionType,
            Member creator
    ) {
        TopicInfo topicInfo = TopicInfo.of(name, description, imageUrl);
        TopicStatus topicStatus = TopicStatus.of(publicity, permissionType);
        Topic topic = new Topic(topicInfo, topicStatus, creator);

        creator.addTopic(topic);

        return topic;
    }

    public void updateTopicInfo(
            String name,
            String description,
            String imageUrl
    ) {
        this.topicInfo = TopicInfo.of(name, description, imageUrl);
    }

    public void updateTopicStatus(Publicity publicity, PermissionType permissionType) {
        topicStatus.update(publicity, permissionType);
    }

    public int countPins() {
        return pins.size();
    }

    public void addPin(Pin pin) {
        pins.add(pin);
    }

    public void addMemberTopicPermission(MemberTopicPermission memberTopicPermission) {
        memberTopicPermissions.add(memberTopicPermission);
    }

}
