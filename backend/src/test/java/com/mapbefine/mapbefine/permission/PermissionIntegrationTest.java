package com.mapbefine.mapbefine.permission;

import static com.mapbefine.mapbefine.oauth.domain.OauthServerType.KAKAO;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.common.IntegrationTest;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.OauthId;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.member.dto.response.MemberDetailResponse;
import com.mapbefine.mapbefine.member.dto.response.MemberResponse;
import com.mapbefine.mapbefine.permission.domain.Permission;
import com.mapbefine.mapbefine.permission.domain.PermissionRepository;
import com.mapbefine.mapbefine.permission.dto.request.PermissionRequest;
import com.mapbefine.mapbefine.permission.dto.response.PermissionDetailResponse;
import com.mapbefine.mapbefine.permission.dto.response.PermissionResponse;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class PermissionIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private PermissionRepository permissionRepository;

    private Member creator;
    private Member user1;
    private Member user2;
    private String creatorAuthHeader;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        creator = memberRepository.save(
                MemberFixture.createWithOauthId(
                        "creator",
                        "creator@naver.com",
                        Role.USER,
                        new OauthId(1L, KAKAO)
                )
        );
        user1 = memberRepository.save(
                MemberFixture.createWithOauthId(
                        "user1",
                        "user1@naver.com",
                        Role.USER,
                        new OauthId(2L, KAKAO)
                )
        );
        user2 = memberRepository.save(
                MemberFixture.createWithOauthId(
                        "user2",
                        "user2@naver.com",
                        Role.USER,
                        new OauthId(3L, KAKAO)
                )
        );
        creatorAuthHeader = testAuthHeaderProvider.createAuthHeader(creator);
    }

    @Test
    @DisplayName("Topic 을 만든자가 특정 유저에게 권한을 준다.")
    void addPermission() {
        // given
        Topic topic = topicRepository.save(TopicFixture.createByName("topicName", creator));

        // when
        PermissionRequest request = new PermissionRequest(topic.getId(), user1.getId());

        ExtractableResponse<Response> response = given().log().all()
                .header(AUTHORIZATION, creatorAuthHeader)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/permissions")
                .then().log().all()
                .extract();

        // then
        assertThat(response.header("Location")).isNotBlank();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("Topic 을 만든자가 특정 유저에게 권한을 삭제한다.")
    void deletePermission() {
        // given
        Topic topic = topicRepository.save(TopicFixture.createByName("topicName", creator));
        Permission permission =
                Permission.createPermissionAssociatedWithTopicAndMember(topic, user1);
        Long savedId = permissionRepository.save(permission).getId();

        // when
        ExtractableResponse<Response> response = given().log().all()
                .header(AUTHORIZATION, creatorAuthHeader)
                .when().delete("/permissions/" + savedId)
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
        Topic topic = topicRepository.save(TopicFixture.createByName("topicName", creator));
        Permission permission1 =
                Permission.createPermissionAssociatedWithTopicAndMember(topic, user1);
        Permission permission2 =
                Permission.createPermissionAssociatedWithTopicAndMember(topic, user2);
        permissionRepository.save(permission1);
        permissionRepository.save(permission2);

        // when
        ExtractableResponse<Response> response = given().log().all()
                .header(AUTHORIZATION, creatorAuthHeader)
                .when().get("/permissions/topics/" + topic.getId())
                .then().log().all()
                .extract();

        // then
        List<PermissionResponse> permissionResponse = response.as(new TypeRef<>() {
        });
        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.OK.value());
        assertThat(permissionResponse)
                .hasSize(2)
                .extracting(PermissionResponse::memberResponse)
                .usingRecursiveComparison()
                .isEqualTo(List.of(MemberResponse.from(user1), MemberResponse.from(user2)));
    }

    @Test
    @DisplayName("Topic 에 권한을 가진 자를 조회한다.")
    void findMemberTopicPermissionById() {
        // given
        Topic topic = topicRepository.save(TopicFixture.createByName("topicName", creator));
        Permission permission =
                Permission.createPermissionAssociatedWithTopicAndMember(topic, user1);
        permission = permissionRepository.save(permission);

        // when
        ExtractableResponse<Response> response = given().log().all()
                .header(AUTHORIZATION, creatorAuthHeader)
                .when().get("/permissions/" + permission.getId())
                .then().log().all()
                .extract();

        // then
        PermissionDetailResponse permissionDetailResponse = response.as(
                PermissionDetailResponse.class);
        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.OK.value());
        assertThat(permissionDetailResponse)
                .extracting(PermissionDetailResponse::memberDetailResponse)
                .usingRecursiveComparison()
                .ignoringFieldsOfTypes(LocalDateTime.class)
                .isEqualTo(MemberDetailResponse.from(user1));
    }


}
