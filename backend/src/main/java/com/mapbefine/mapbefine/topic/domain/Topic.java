package com.mapbefine.mapbefine.topic.domain;

import static lombok.AccessLevel.PROTECTED;

import com.mapbefine.mapbefine.common.entity.BaseTimeEntity;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.pin.Domain.Pin;
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

    public static Topic of(
            String name,
            String description,
            String imageUrl,
            Publicity publicity,
            Permission permission,
            Member creator
    ) {
        return new Topic(
                TopicInfo.of(
                        name,
                        description,
                        imageUrl
                ),
                TopicStatus.of(publicity, permission),
                creator
        );

    }

    public void updateTopicInfo(
            String name,
            String description,
            String imageUrl
    ) {
        topicInfo.update(
                name,
                description,
                imageUrl
        );
    }

    public void updateTopicStatus(Publicity publicity, Permission permission) {
        topicStatus.update(publicity, permission);
    }

    public int countPins() {
        return pins.size();
    }

    public void addPin(Pin pin) {
        pins.add(pin);
    }

}
