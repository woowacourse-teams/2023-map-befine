package com.mapbefine.mapbefine.topic.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.mapbefine.mapbefine.location.LocationFixture;
import com.mapbefine.mapbefine.location.domain.Location;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.pin.PinFixture;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.topic.TopicFixture;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ClusterTest {

    @Test
    @DisplayName("대표 핀과, 해당 좌표에 묶인 핀들의 집합을 받으면 정상적으로 생성된다.")
    void createCluster() {
        // given
        Member member = MemberFixture.create("member", "member@naver.com", Role.USER);
        Topic topic = TopicFixture.createByName("topic", member);
        Location firstLocation = LocationFixture.createByCoordinate(36, 124);
        Location secondLocation = LocationFixture.createByCoordinate(36, 124.1);
        List<Pin> pins = List.of(
                PinFixture.create(firstLocation, topic, member),
                PinFixture.create(secondLocation, topic, member)
        );

        // when
        Cluster actual = Cluster.from(pins.get(0), pins);

        // then
        assertAll(
                () -> assertThat(actual.getLatitude()).isEqualTo(36),
                () -> assertThat(actual.getLongitude()).isEqualTo(124),
                () -> assertThat(actual.getPins().get(0)).isEqualTo(pins.get(0)),
                () -> assertThat(actual.getPins()).usingRecursiveComparison()
                        .isEqualTo(pins)
        );
    }

}
