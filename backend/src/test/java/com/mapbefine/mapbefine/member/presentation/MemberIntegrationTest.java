package com.mapbefine.mapbefine.member.presentation;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.common.IntegrationTest;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.member.dto.request.MemberTopicPermissionCreateRequest;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
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
        Member member = memberRepository.save(MemberFixture.create(Role.USER));
        String authHeader = Base64.encodeBase64String(
                ("Basic " + creator.getMemberInfo().getEmail()).getBytes()
        );
        Topic topic = topicRepository.save(TopicFixture.createByName("topicName", creator));
        MemberTopicPermissionCreateRequest request = new MemberTopicPermissionCreateRequest(
                topic.getId(),
                member.getId()
        );

        // when
        ExtractableResponse<Response> response = given().log().all()
                .header(AUTHORIZATION, authHeader)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/members/permissions")
                .then().log().all()
                .extract();

        // then
        assertThat(response.header("Location")).isNotBlank();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

}
