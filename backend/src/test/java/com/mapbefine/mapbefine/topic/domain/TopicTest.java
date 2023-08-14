package com.mapbefine.mapbefine.topic.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.location.LocationFixture;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.pin.PinFixture;
import com.mapbefine.mapbefine.pin.domain.Pin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TopicTest {

    private Topic topic;
    private Pin pin;

    private Member member;

    @BeforeEach
    void setUp() {
        member = MemberFixture.create("member", "member@naver.com", Role.USER);
        topic = Topic.createTopicAssociatedWithCreator(
                "매튜의 산스장",
                "매튜가 엄마 몰래 찾는 산스장",
                "https://example.com/image.jpg",
                Publicity.PUBLIC,
                PermissionType.GROUP_ONLY,
                member
        );
        pin = PinFixture.create(LocationFixture.create(), topic, member);
    }

    @Test
    void updateTopicInfo() {
        //given
        String name = "New Topic";
        String description = "New Description";
        String imageUrl = "https://example.com/image.png";

        //when
        topic.updateTopicInfo(
                name,
                description,
                imageUrl
        );
        TopicInfo topicInfo = topic.getTopicInfo();

        //then
        assertThat(topicInfo.getName()).isEqualTo(name);
        assertThat(topicInfo.getDescription()).isEqualTo(description);
        assertThat(topicInfo.getImageUrl()).isEqualTo(imageUrl);
    }

    @Test
    void updateTopicStatus() {
        //given
        Publicity publicity = Publicity.PRIVATE;
        PermissionType permissionType = PermissionType.GROUP_ONLY;

        //when
        topic.updateTopicStatus(
                publicity,
                permissionType
        );
        TopicStatus topicStatus = topic.getTopicStatus();

        //then
        assertThat(topicStatus.getPublicity()).isEqualTo(publicity);
        assertThat(topicStatus.getPermissionType()).isEqualTo(permissionType);
    }

    @Test
    void countPins() {
        //given
        topic.addPin(pin);

        //when
        int pinCounts = topic.countPins();

        //then
        assertThat(pinCounts).isEqualTo(2);
        // 핀이 처음 생성됐을 때, 연관관계 매핑으로 인해 하나 추가된 후 해당 메서드에서 하나 더 추가됌
    }

    @Test
    void addPin() {
        //when
        topic.addPin(pin);

        //then
        assertThat(topic.getPins()).contains(pin);
    }
}
