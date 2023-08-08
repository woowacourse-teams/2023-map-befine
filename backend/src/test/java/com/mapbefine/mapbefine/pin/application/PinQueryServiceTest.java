package com.mapbefine.mapbefine.pin.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.common.annotation.ServiceTest;
import com.mapbefine.mapbefine.location.LocationFixture;
import com.mapbefine.mapbefine.location.domain.Location;
import com.mapbefine.mapbefine.location.domain.LocationRepository;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.pin.PinFixture;
import com.mapbefine.mapbefine.pin.PinImageFixture;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.domain.PinRepository;
import com.mapbefine.mapbefine.pin.dto.response.PinDetailResponse;
import com.mapbefine.mapbefine.pin.dto.response.PinResponse;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class PinQueryServiceTest {

    @Autowired
    private PinQueryService pinQueryService;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private PinRepository pinRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private MemberRepository memberRepository;

    private Location location;
    private Topic topic;
    private Member member;
    private AuthMember authMember;

    @BeforeEach
    void setUp() {
        location = locationRepository.save(LocationFixture.create());
        member = memberRepository.save(MemberFixture.create("member", "member@naver.com", Role.ADMIN));
        authMember = AuthMember.from(member);
        topic = topicRepository.save(TopicFixture.createByName("topic", member));
    }

    @Test
    @DisplayName("핀 목록을 가져온다.")
    void findAllReadable_Success() {
        // given
        List<PinResponse> expected = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Pin pin = PinFixture.create(location, topic, member);
            pinRepository.save(pin);
            expected.add(PinResponse.from(pin));
        }

        // when
        List<PinResponse> responses = pinQueryService.findAllReadable(authMember);

        // then
        assertThat(responses).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("핀의 Id 를 넘기면 핀을 가져온다.")
    void findDetailById_Success() {
        // given
        Pin pin = PinFixture.create(location, topic, member);
        PinImageFixture.create(pin);
        pinRepository.save(pin);
        Long pinId = pin.getId();

        // when
        PinDetailResponse actual = pinQueryService.findDetailById(authMember, pinId);

        // then
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("updatedAt")
                .isEqualTo(PinDetailResponse.from(pin));
    }

    @Test
    @DisplayName("존재하지 않는 핀의 Id 를 넘기면 예외를 발생시킨다.")
    void findById_Fail() {
        // given when then
        assertThatThrownBy(() -> pinQueryService.findDetailById(authMember, 1L))
                .isInstanceOf(NoSuchElementException.class);
    }

}
