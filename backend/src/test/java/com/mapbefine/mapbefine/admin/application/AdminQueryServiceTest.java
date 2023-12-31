package com.mapbefine.mapbefine.admin.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.TestDatabaseContainer;
import com.mapbefine.mapbefine.admin.dto.AdminMemberDetailResponse;
import com.mapbefine.mapbefine.admin.dto.AdminMemberResponse;
import com.mapbefine.mapbefine.common.annotation.ServiceTest;
import com.mapbefine.mapbefine.location.LocationFixture;
import com.mapbefine.mapbefine.location.domain.Location;
import com.mapbefine.mapbefine.location.domain.LocationRepository;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.pin.PinFixture;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.domain.PinRepository;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class AdminQueryServiceTest extends TestDatabaseContainer {

    @Autowired
    private AdminQueryService adminQueryService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private PinRepository pinRepository;

    @Autowired
    private LocationRepository locationRepository;

    private Location location;
    private Member admin;
    private Member member;
    private Topic topic;
    private Pin pin;

    @BeforeEach
    void setup() {
        admin = memberRepository.save(MemberFixture.create("Admin", "admin@naver.com", Role.ADMIN));
        member = memberRepository.save(MemberFixture.create("member", "member@gmail.com", Role.USER));
        topic = topicRepository.save(TopicFixture.createByName("topic", member));
        location = locationRepository.save(LocationFixture.create());
        pin = pinRepository.save(PinFixture.create(location, topic, member));
    }

    @Test
    @DisplayName("사용자와 관련된 세부(민감한 정보 포함) 정보를 모두 조회할 수 있다.")
    void findMemberDetail_Success() {
        //given
        AdminMemberDetailResponse response = adminQueryService.findMemberDetail(member.getId());

        //when //then
        assertThat(response).usingRecursiveComparison()
                .ignoringFields("updatedAt")
                .isEqualTo(AdminMemberDetailResponse.of(member, List.of(topic), List.of(pin)));
    }
    
    @Test
    @DisplayName("모든 사용자와 관련된 세부 정보를 모두 조회할 수 있다.")
    void findAllMemberDetails_Success() {
        //given
        ArrayList<Member> members = new ArrayList<>();
        members.add(member);
        for (int i = 0; i < 10; i++) {
            Member saveMember = MemberFixture.create("member" + i, "member" + i + "@email.com", Role.USER);
            members.add(memberRepository.save(saveMember));
        }

        //when
        List<AdminMemberResponse> responses = adminQueryService.findAllMemberDetails();
        //then
        List<AdminMemberResponse> expected = members.stream()
                .map(AdminMemberResponse::from)
                .toList();

        assertThat(responses).usingRecursiveComparison()
                .ignoringFields("updatedAt")
                .ignoringCollectionOrderInFields()
                .isEqualTo(expected);
    }

}
