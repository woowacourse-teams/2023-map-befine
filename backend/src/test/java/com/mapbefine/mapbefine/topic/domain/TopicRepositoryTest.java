package com.mapbefine.mapbefine.topic.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class TopicRepositoryTest {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        member = MemberFixture.create("member", "member@naver.com", Role.USER);
        memberRepository.save(member);
    }

    @Test
    @DisplayName("토픽을 삭제하면, soft-deleting 된다.")
    void deleteById_Success() {
        //given
        Topic topic = Topic.createTopicAssociatedWithCreator(
                "토픽",
                "토픽설명",
                "https://example.com/image.jpg",
                Publicity.PUBLIC,
                Permission.ALL_MEMBERS,
                member
        );

        topicRepository.save(topic);

        assertThat(topic.isDeleted()).isFalse();

        //when
        topicRepository.deleteById(topic.getId());

        //then
        Topic deletedTopic = topicRepository.findById(topic.getId()).get();
        assertThat(deletedTopic.isDeleted()).isTrue();
    }

}