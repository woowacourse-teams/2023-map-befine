package com.mapbefine.mapbefine.pin.domain;

import static com.mapbefine.mapbefine.pin.domain.PinComment.ofParentPinComment;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mapbefine.mapbefine.location.LocationFixture;
import com.mapbefine.mapbefine.location.domain.Location;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.pin.PinFixture;
import com.mapbefine.mapbefine.pin.exception.PinCommentException.PinCommentBadRequestException;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PinCommentTest {

    private Pin pin;
    private Member creator;

    @BeforeEach
    void beforeEach() {
        Location location = LocationFixture.create();
        creator = MemberFixture.create("member", "member@naver.com", Role.USER);
        Topic topic = TopicFixture.createPublicAndAllMembersTopic("https://imageUrl", creator);
        pin = PinFixture.create(location, topic, creator);
    }

    @ParameterizedTest
    @MethodSource("validCommentContent")
    @DisplayName("유효한 댓글 내용일 경우 핀 댓글 생성에 성공한다.")
    void createPinComment_Success(String content) {
        // when
        PinComment pinComment = ofParentPinComment(pin, creator, content);

        // then
        assertThat(pinComment.getContent()).isEqualTo(content);
    }

    static Stream<Arguments> validCommentContent() {
        return Stream.of(
                Arguments.of("댓"),
                Arguments.of("댓".repeat(1000))
        );
    }

    @ParameterizedTest
    @MethodSource("invalidCommentContent")
    @DisplayName("유효하지 않은 핀 댓글 생성에 실패한다.")
    void createPinComment_Fail(String content) {
        // when then
        assertThatThrownBy(() -> ofParentPinComment(pin, creator, content))
                .isInstanceOf(PinCommentBadRequestException.class);
    }

    static Stream<Arguments> invalidCommentContent() {
        return Stream.of(
                Arguments.of(""),
                Arguments.of("댓".repeat(1001))
        );
    }

    @ParameterizedTest
    @MethodSource("validCommentContent")
    @DisplayName("유효한 댓글 내용일 경우 핀 댓글 내용 수정에 성공한다.")
    void updatePinComment_Success(String content) {
        // given
        PinComment pinComment = ofParentPinComment(pin, creator, "댓글 수정 전");

        // when
        pinComment.updateContent(content);

        // then
        assertThat(pinComment.getContent()).isEqualTo(content);
    }


    @ParameterizedTest
    @MethodSource("invalidCommentContent")
    @DisplayName("유효하지 않은 댓글 내용일 경우 핀 댓글 수정에 실패한다.")
    void updatePinComment_Fail(String content) {
        PinComment pinComment = ofParentPinComment(pin, creator, "댓글 수정 전");

        // when then
        assertThatThrownBy(() -> pinComment.updateContent(content))
                .isInstanceOf(PinCommentBadRequestException.class);
    }

}
