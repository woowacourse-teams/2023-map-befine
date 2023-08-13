package com.mapbefine.mapbefine.atlas.application;

import com.mapbefine.mapbefine.atlas.domain.Atlas;
import com.mapbefine.mapbefine.atlas.domain.AtlasRepository;
import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.auth.domain.member.Admin;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class AtlasQueryServiceTest {

    @Autowired
    private AtlasRepository atlasRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TopicRepository topicRepository;

    private AtlasQueryService atlasQueryService;
    private AuthMember authMember;
    private List<Topic> topics;

    @BeforeEach
    void setUp() {
        atlasQueryService = new AtlasQueryService(atlasRepository);

        Member member = memberRepository.save(MemberFixture.create("member", "member@member.com", Role.ADMIN));
        authMember = new Admin(member.getId());

        createTopics(member);
        topics.forEach(topic -> atlasRepository.save(Atlas.from(topic, member)));
    }

    private void createTopics(Member member) {
        topics = List.of(
                TopicFixture.createPublicAndAllMembersTopic(member),
                TopicFixture.createPublicAndAllMembersTopic(member),
                TopicFixture.createPublicAndAllMembersTopic(member)
        );
        topicRepository.saveAll(topics);
    }

    @Test
    @DisplayName("멤버 ID를 이용해 모아보기할 모든 Topic들을 가져올 수 있다.")
    void findAtlasByMember_Success() {
        //when
        List<TopicResponse> atlas = atlasQueryService.findTopicsInAtlasByMember(authMember);

        //then
        Assertions.assertThat(atlas.size()).isEqualTo(topics.size());
    }
}
