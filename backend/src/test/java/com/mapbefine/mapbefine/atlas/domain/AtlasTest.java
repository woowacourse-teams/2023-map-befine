package com.mapbefine.mapbefine.atlas.domain;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AtlasTest {

    private static final Member MEMBER = MemberFixture.create("member", "member@member.com", Role.USER);

    private static final Topic TOPIC = TopicFixture.createByName("topic", MEMBER);

    @Nested
    class ValidationTest {


        @Test
        @DisplayName("정상적인 값이 입력되면 객체가 생성된다.")
        void success() {
            Atlas atlas = Atlas.from(TOPIC, MEMBER);

            assertThat(atlas).isNotNull();
            assertThat(atlas.getTopic()).isEqualTo(TOPIC);
            assertThat(atlas.getMember()).isEqualTo(MEMBER);
        }

        @ParameterizedTest
        @MethodSource(value = "memberTopicProvider")
        @DisplayName("입력값이 null이면 예외가 발생된다.")
        void validation_fail(Topic topic, Member member) {
            assertThatThrownBy(() -> Atlas.from(topic, member))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("토픽과 멤버는 Null이어선 안됩니다.");
        }

        static Stream<Arguments> memberTopicProvider() {
            return Stream.of(
                    Arguments.of(null, MEMBER),
                    Arguments.of(TOPIC, null),
                    Arguments.of(null, null)
            );
        }
    }

}
