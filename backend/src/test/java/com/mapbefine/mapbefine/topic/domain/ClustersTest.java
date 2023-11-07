package com.mapbefine.mapbefine.topic.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.mapbefine.mapbefine.location.LocationFixture;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.pin.PinFixture;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.exception.TopicException.TopicBadRequestException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ClustersTest {

    @Test
    @DisplayName("각기 약 9000미터 떨어져있는 핀 5개를 받아 3개의 집합이 완성된다.")
    void createClusters_success() {
        // given
        Member member = MemberFixture.create("member", "member@naver.com", Role.USER);
        Topic topic = TopicFixture.createByName("topic", member);
        List<Pin> pins = List.of(
                PinFixture.create(LocationFixture.createByCoordinate(36, 124), topic, member),
                PinFixture.create(LocationFixture.createByCoordinate(36, 124.1), topic, member),
                PinFixture.create(LocationFixture.createByCoordinate(36, 124.2), topic, member),
                PinFixture.create(LocationFixture.createByCoordinate(38, 124), topic, member),
                PinFixture.create(LocationFixture.createByCoordinate(38, 124.1), topic, member)
        );

        // when
        List<Cluster> actual = Clusters.from(pins, 9000D)
                .getClusters();

        // then
        assertAll(
                () -> assertThat(actual).hasSize(3),
                () -> assertThat(actual)
                        .extracting(Cluster::getLatitude, Cluster::getLongitude)
                        .contains(tuple(36d, 124d), tuple(36d, 124.2d), tuple(38d, 124d))
        );
    }

    @Test
    @DisplayName("이미지의 크기를 입력하지 않으면 예외가 발생한다.")
    void createClusters_fail() {
        // given
        Member member = MemberFixture.create("member", "member@naver.com", Role.USER);
        Topic topic = TopicFixture.createByName("topic", member);
        List<Pin> pins = List.of(
                PinFixture.create(LocationFixture.createByCoordinate(36, 124), topic, member)
        );

        // when then
        assertThatThrownBy(() -> Clusters.from(pins, null))
                .isInstanceOf(TopicBadRequestException.class);
    }

}
