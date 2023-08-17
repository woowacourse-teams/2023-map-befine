package com.mapbefine.mapbefine.atlas.domain;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.mapbefine.mapbefine.atlas.exception.AtlasException.AtlasBadRequestException;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class AtlasTest {

    private static final Member MEMBER = MemberFixture.create("member", "member@member.com", Role.USER);

    private static final Topic TOPIC = TopicFixture.createByName("topic", MEMBER);

    @Nested
    class ValidationTest {


        @Test
        @DisplayName("정상적인 값이 입력되면 객체가 생성된다.")
        void success() {
            Atlas atlas = Atlas.createWithAssociatedMember(TOPIC, MEMBER);

            assertThat(atlas).isNotNull();
            assertThat(atlas.getTopic()).isEqualTo(TOPIC);
            assertThat(atlas.getMember()).isEqualTo(MEMBER);
        }

        @Test
        @DisplayName("Topic Id가 null이면 예외가 발생된다.")
        void validationTopic_fail() {
            assertThatThrownBy(() -> Atlas.createWithAssociatedMember(null, MEMBER))
                    .isInstanceOf(AtlasBadRequestException.class);
        }

        @Test
        @DisplayName("Member Id가 null이면 예외가 발생된다.")
        void validationMember_fail() {
            assertThatThrownBy(() -> Atlas.createWithAssociatedMember(TOPIC, null))
                    .isInstanceOf(AtlasBadRequestException.class);
        }

    }

}
