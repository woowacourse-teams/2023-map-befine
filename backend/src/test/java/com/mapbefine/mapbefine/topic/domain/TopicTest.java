package com.mapbefine.mapbefine.topic.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.location.LocationFixture;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.pin.PinFixture;
import com.mapbefine.mapbefine.pin.domain.Pin;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("토픽 정보를 변경한다.")
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
    @DisplayName("토픽 상태를 변경한다.")
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
    @DisplayName("토픽의 핀 개수를 알 수 있다.")
    void countPins() {
        //given
        topic.addPin(pin);

        //when
        int pinCounts = topic.countPins();

        //then
        assertThat(pinCounts).isEqualTo(2);
    }

    @Test
    @DisplayName("토픽에 직접 핀을 추가해도, 조회 전용이므로 핀 변경 일시는 반영되지 않는다.")
    void addPin() {
        //when
        LocalDateTime beforeAdding = topic.getLastPinUpdatedAt();
        topic.addPin(pin);

        //then
        assertThat(topic.getPins()).contains(pin);
        assertThat(topic.getLastPinUpdatedAt()).isEqualTo(beforeAdding);
    }

    @Test
    @DisplayName("핀 없이 생성된 토픽의 초기 핀 추가 일시는 토픽 변경 일시와 같다.")
    void create() {
        // when
        Topic emptyPinsTopic = new Topic();

        //then
        assertThat(emptyPinsTopic.getCreatedAt()).isEqualTo(emptyPinsTopic.getLastPinUpdatedAt());
    }
}
