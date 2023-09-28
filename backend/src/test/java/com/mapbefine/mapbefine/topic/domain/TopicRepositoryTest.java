package com.mapbefine.mapbefine.topic.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.topic.TopicFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
class TopicRepositoryTest {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(MemberFixture.create("member", "member@naver.com", Role.USER));
    }

    @Test
    @DisplayName("토픽을 삭제하면, soft-deleting 된다.")
    void deleteById_Success() {
        //given
        Topic topic = topicRepository.save(TopicFixture.createByName("Topic", member));

        assertThat(topic.isDeleted()).isFalse();

        //when
        topicRepository.deleteById(topic.getId());

        //then
        Topic deletedTopic = topicRepository.findById(topic.getId()).get();
        assertThat(deletedTopic.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("Member Id로 모든 토픽을 삭제하면, soft-deleting 된다.")
    void deleteAllByMemberId_Success() {
        //given
        for (int i = 0; i < 10; i++) {
            topicRepository.save(TopicFixture.createByName("topic" + i, member));
        }
        assertThat(member.getCreatedTopics()).hasSize(10)
                .extractingResultOf("isDeleted")
                .containsOnly(false);

        //when
        topicRepository.deleteAllByMemberId(member.getId());

        //then
        List<Topic> deletedTopics = topicRepository.findAllByCreatorId(member.getId());
        assertThat(deletedTopics).hasSize(10)
                .extractingResultOf("isDeleted")
                .containsOnly(true);
    }
}
