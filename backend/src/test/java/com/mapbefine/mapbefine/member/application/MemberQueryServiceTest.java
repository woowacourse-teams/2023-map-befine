package com.mapbefine.mapbefine.member.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.common.annotation.ServiceTest;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.MemberTopicPermission;
import com.mapbefine.mapbefine.member.domain.MemberTopicPermissionRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.member.dto.response.MemberResponse;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
public class MemberQueryServiceTest {

    @Autowired
    private MemberQueryService memberQueryService;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberTopicPermissionRepository memberTopicPermissionRepository;

    @Test
    @DisplayName("Topic 에 권한이 있는자들을 모두 조회한다.") // creator 는 권한이 있는자들을 조회할 때 조회되어야 할 것인가??
    void findAllWithPermission() {
        // given
        Member member1InTopic1 = memberRepository.save(
                MemberFixture.create("member", "member@naver.com", Role.USER)
        );
        Member member2InTopic1 = memberRepository.save(
                MemberFixture.create("members", "members@naver.com", Role.USER)
        );
        Member member3InTopic2 = memberRepository.save(
                MemberFixture.create("memberss", "memberss@naver.com", Role.USER)
        );
        Topic topic1 = topicRepository.save(TopicFixture.createByName("topic1", member1InTopic1));
        Topic topic2 = topicRepository.save(TopicFixture.createByName("topic2", member1InTopic1));
        memberTopicPermissionRepository.save(
                MemberTopicPermission.createPermissionAssociatedWithTopicAndMember(topic1, member1InTopic1)
        );
        memberTopicPermissionRepository.save(
                MemberTopicPermission.createPermissionAssociatedWithTopicAndMember(topic1, member2InTopic1)
        );
        memberTopicPermissionRepository.save(
                MemberTopicPermission.createPermissionAssociatedWithTopicAndMember(topic2, member3InTopic2)
        );

        // when
        List<MemberResponse> memberResponses = memberQueryService.findAllWithPermission(topic1.getId());
        MemberResponse memberResponse1 = MemberResponse.from(member1InTopic1);
        MemberResponse memberResponse2 = MemberResponse.from(member2InTopic1);

        // then
        assertThat(memberResponses).hasSize(2)
                .usingRecursiveComparison()
                .isEqualTo(List.of(memberResponse1, memberResponse2));
    }

}
