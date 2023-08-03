package com.mapbefine.mapbefine.member.presentation;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.common.IntegrationTest;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.member.dto.request.MemberCreateRequest;
import com.mapbefine.mapbefine.member.dto.request.MemberTopicPermissionCreateRequest;
import com.mapbefine.mapbefine.member.dto.response.MemberDetailResponse;
import com.mapbefine.mapbefine.member.dto.response.MemberResponse;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class MemberIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Test
    @DisplayName("Topic 을 만든자가 특정 유저에게 권한을 준다.")
    void addMemberTopicPermission() {
        // given
        Member creator = memberRepository.save(
                Member.of(
                        "memberr",
                        "memberr@naver.com",
                        "https://map-befine-official.github.io/favicon.png",
                        Role.USER
                )
        );
        Member member = memberRepository.save(MemberFixture.create("member", "member@naver.com", Role.USER));
        String authHeader = Base64.encodeBase64String(
                ("Basic " + creator.getMemberInfo().getEmail()).getBytes()
        );
        Topic topic = topicRepository.save(TopicFixture.createByName("topicName", creator));
        MemberTopicPermissionCreateRequest request = new MemberTopicPermissionCreateRequest(
                topic.getId(),
                member.getId()
        );

        // when
        ExtractableResponse<Response> response = saveMemberTopicPermission(authHeader, request);

        // then
        assertThat(response.header("Location")).isNotBlank();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> saveMemberTopicPermission(
            String authHeader,
            MemberTopicPermissionCreateRequest request
    ) {
        return given().log().all()
                .header(AUTHORIZATION, authHeader)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/members/permissions")
                .then().log().all()
                .extract();
    }

    @Test
    @DisplayName("Topic 을 만든자가 특정 유저에게 권한을 삭제한다.")
    void deleteMemberTopicPermission() {
        // given
        Member creator = memberRepository.save(
                Member.of(
                        "memberr",
                        "memberr@naver.com",
                        "https://map-befine-official.github.io/favicon.png",
                        Role.USER
                )
        );
        Member member = memberRepository.save(MemberFixture.create("member", "member@naver.com", Role.USER));
        String authHeader = Base64.encodeBase64String(
                ("Basic " + creator.getMemberInfo().getEmail()).getBytes()
        );
        Topic topic = topicRepository.save(TopicFixture.createByName("topicName", creator));
        MemberTopicPermissionCreateRequest request = new MemberTopicPermissionCreateRequest(
                topic.getId(),
                member.getId()
        );

        // when
        ExtractableResponse<Response> newMemberTopicPermission = saveMemberTopicPermission(authHeader, request);
        long memberTopicPermissionId = Long.parseLong(newMemberTopicPermission.header("Location").split("/")[3]);
        ExtractableResponse<Response> response = given().log().all()
                .header(AUTHORIZATION, authHeader)
                .when().delete("/members/permissions/" + memberTopicPermissionId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("Topic 에 권한을 가진 자들을 모두 조회한다.")
    void findMemberTopicPermissionAll() {
        // given
        Member creator = memberRepository.save(
                Member.of(
                        "memberr",
                        "memberr@naver.com",
                        "https://map-befine-official.github.io/favicon.png",
                        Role.USER
                )
        );
        Member member1 = memberRepository.save(MemberFixture.create("memberrr", "memberrr@naver.com", Role.USER));
        Member member2 = memberRepository.save(MemberFixture.create("member", "member@naver.com", Role.USER));
        String authHeader = Base64.encodeBase64String(
                ("Basic " + creator.getMemberInfo().getEmail()).getBytes()
        );
        Topic topic = topicRepository.save(TopicFixture.createByName("topicName", creator));
        MemberTopicPermissionCreateRequest request1 = new MemberTopicPermissionCreateRequest(
                topic.getId(),
                member1.getId()
        );
        MemberTopicPermissionCreateRequest request2 = new MemberTopicPermissionCreateRequest(
                topic.getId(),
                member2.getId()
        );

        // when
        saveMemberTopicPermission(authHeader, request1);
        saveMemberTopicPermission(authHeader, request2);
        ExtractableResponse<Response> response = given().log().all()
                .header(AUTHORIZATION, authHeader)
                .when().get("/members/permissions/topics/" + topic.getId())
                .then().log().all()
                .extract();

        // then
        List<MemberResponse> memberResponses = response.as(new TypeRef<>() {});
        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.OK.value());
        assertThat(memberResponses).usingRecursiveComparison()
                .isEqualTo(List.of(MemberResponse.from(member1), MemberResponse.from(member2)));
    }

    @Test
    @DisplayName("Topic 에 권한을 가진 자를 조회한다.")
    void findMemberTopicPermissionById() {
        // given
        Member creator = memberRepository.save(
                Member.of(
                        "memberr",
                        "memberr@naver.com",
                        "https://map-befine-official.github.io/favicon.png",
                        Role.USER
                )
        );
        Member member = memberRepository.save(MemberFixture.create("memberrr", "memberrr@naver.com", Role.USER));
        String authHeader = Base64.encodeBase64String(
                ("Basic " + creator.getMemberInfo().getEmail()).getBytes()
        );
        Topic topic = topicRepository.save(TopicFixture.createByName("topicName", creator));
        MemberTopicPermissionCreateRequest request = new MemberTopicPermissionCreateRequest(
                topic.getId(),
                member.getId()
        );

        // when
        ExtractableResponse<Response> newMemberTopicPermission = saveMemberTopicPermission(authHeader, request);
        long memberTopicPermissionId = Long.parseLong(newMemberTopicPermission.header("Location").split("/")[3]);

        ExtractableResponse<Response> response = given().log().all()
                .header(AUTHORIZATION, authHeader)
                .when().get("/members/permissions/" + memberTopicPermissionId)
                .then().log().all()
                .extract();

        // then
        MemberDetailResponse memberResponses = response.as(MemberDetailResponse.class);
        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.OK.value());
        assertThat(memberResponses).usingRecursiveComparison()
                .isEqualTo(MemberDetailResponse.from(member));
    }

    @Test
    @DisplayName("유저를 생성한다.")
    void add() {
        // given
        MemberCreateRequest request = new MemberCreateRequest(
                "member",
                "member@naver.com",
                "https://map-befine-official.github.io/favicon.png",
                Role.USER
        );

        // when
        ExtractableResponse<Response> response = saveMember(request);

        // then
        assertThat(response.header("Location")).isNotBlank();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> saveMember(
            MemberCreateRequest request
    ) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/members")
                .then().log().all()
                .extract();
    }

}
