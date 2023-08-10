package com.mapbefine.mapbefine.atlas.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mapbefine.mapbefine.atlas.domain.Atlas;
import com.mapbefine.mapbefine.atlas.domain.AtlasRepository;
import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.auth.domain.member.Admin;
import com.mapbefine.mapbefine.auth.domain.member.User;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class AtlasCommandServiceTest {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AtlasRepository atlasRepository;

    private AtlasCommandService atlasCommandService;
    private AuthMember authMember;
    private Topic topic;

    @BeforeEach
    void setUp() {
        atlasCommandService = new AtlasCommandService(topicRepository, memberRepository, atlasRepository);
        Member member = memberRepository.save(MemberFixture.create("member", "member@naver.com", Role.ADMIN));
        topic = topicRepository.save(TopicFixture.createPrivateByName("topic", member));

        authMember = new Admin(member.getId());
    }

    @Nested
    @DisplayName("모아보기에 토픽을 더할 때,")
    class addTopic {

        @Test
        @DisplayName("정상적인 값이 입력되면 성공한다.")
        void success() {
            //when
            atlasCommandService.addTopic(authMember, topic.getId());

            //then
            boolean isExisted = atlasRepository.existsByMemberIdAndTopicId(authMember.getMemberId(), topic.getId());
            assertThat(isExisted).isTrue();
        }

        @Test
        @DisplayName("이미 입력된 값이면 새로 입력하지 않는다.")
        void alreadyAdd_Fail() {
            //given
            atlasCommandService.addTopic(authMember, topic.getId());
            List<Atlas> expected = atlasRepository.findAllByMemberId(authMember.getMemberId());

            //when
            atlasCommandService.addTopic(authMember, topic.getId());
            List<Atlas> actual = atlasRepository.findAllByMemberId(authMember.getMemberId());

            //then
            assertThat(actual.size()).isEqualTo(expected.size());
        }

        @Test
        @DisplayName("접근 권한이 없는 경우 예외가 발생한다")
        void validateReadPermission_fail() {
            //given
            Member other = memberRepository.save(
                    MemberFixture.create("other", "other@othter.com", Role.USER)
            );
            AuthMember otherAuthMember = new User(other.getId(), Collections.emptyList(), Collections.emptyList());

            //when
            //then
            assertThatThrownBy(() -> atlasCommandService.addTopic(otherAuthMember, topic.getId()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("해당 지도에 접근권한이 없습니다.");
        }

    }

    @Test
    @DisplayName("")
    void remove_Success() {
        Long topicId = topic.getId();
        Long memberId = authMember.getMemberId();

        //given
        atlasCommandService.addTopic(authMember, topicId);
        List<Atlas> expected = atlasRepository.findAllByMemberId(memberId);

        //when
        atlasCommandService.removeTopic(authMember, topicId);
        List<Atlas> actual = atlasRepository.findAllByMemberId(memberId);

        //then
        assertThat(actual.size()).isEqualTo(expected.size() - 1);
    }

}
