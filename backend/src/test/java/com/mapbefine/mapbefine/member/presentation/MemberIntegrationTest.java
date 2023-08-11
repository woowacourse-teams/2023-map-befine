package com.mapbefine.mapbefine.member.presentation;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.common.IntegrationTest;
import com.mapbefine.mapbefine.location.LocationFixture;
import com.mapbefine.mapbefine.location.domain.Location;
import com.mapbefine.mapbefine.location.domain.LocationRepository;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.MemberTopicBookmark;
import com.mapbefine.mapbefine.member.domain.MemberTopicBookmarkRepository;
import com.mapbefine.mapbefine.member.domain.MemberTopicPermission;
import com.mapbefine.mapbefine.member.domain.MemberTopicPermissionRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.member.dto.request.MemberCreateRequest;
import com.mapbefine.mapbefine.member.dto.request.MemberTopicPermissionCreateRequest;
import com.mapbefine.mapbefine.member.dto.response.MemberDetailResponse;
import com.mapbefine.mapbefine.member.dto.response.MemberResponse;
import com.mapbefine.mapbefine.member.dto.response.MemberTopicBookmarkResponse;
import com.mapbefine.mapbefine.member.dto.response.MemberTopicPermissionDetailResponse;
import com.mapbefine.mapbefine.member.dto.response.MemberTopicPermissionResponse;
import com.mapbefine.mapbefine.pin.PinFixture;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.domain.PinRepository;
import com.mapbefine.mapbefine.pin.dto.response.PinResponse;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
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

    @Autowired
    private PinRepository pinRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private MemberTopicPermissionRepository memberTopicPermissionRepository;

    @Autowired
    private MemberTopicBookmarkRepository memberTopicBookmarkRepository;

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
        Member member = memberRepository.save(
                MemberFixture.create("member", "member@naver.com", Role.USER));
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
        Member member = memberRepository.save(MemberFixture.create(
                        "member",
                        "member@naver.com",
                        Role.USER
                )
        );
        String authHeader = Base64.encodeBase64String(
                ("Basic " + creator.getMemberInfo().getEmail()).getBytes()
        );
        Topic topic = topicRepository.save(TopicFixture.createByName("topicName", creator));

        // when
        MemberTopicPermission memberTopicPermission =
                MemberTopicPermission.createPermissionAssociatedWithTopicAndMember(topic, member);
        Long savedId = memberTopicPermissionRepository.save(memberTopicPermission).getId();
        ExtractableResponse<Response> response = given().log().all()
                .header(AUTHORIZATION, authHeader)
                .when().delete("/members/permissions/" + savedId)
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
        Member member1 = memberRepository.save(
                MemberFixture.create(
                        "memberrr",
                        "memberrr@naver.com",
                        Role.USER
                )
        );
        Member member2 = memberRepository.save(
                MemberFixture.create(
                        "member",
                        "member@naver.com",
                        Role.USER
                )
        );
        String authHeader = Base64.encodeBase64String(
                ("Basic " + creator.getMemberInfo().getEmail()).getBytes()
        );
        Topic topic = topicRepository.save(TopicFixture.createByName("topicName", creator));
        MemberTopicPermission memberTopicPermission1 =
                MemberTopicPermission.createPermissionAssociatedWithTopicAndMember(topic, member1);
        MemberTopicPermission memberTopicPermission2 =
                MemberTopicPermission.createPermissionAssociatedWithTopicAndMember(topic, member2);

        // when
        memberTopicPermissionRepository.save(memberTopicPermission1);
        memberTopicPermissionRepository.save(memberTopicPermission2);
        ExtractableResponse<Response> response = given().log().all()
                .header(AUTHORIZATION, authHeader)
                .when().get("/members/permissions/topics/" + topic.getId())
                .then().log().all()
                .extract();

        // then
        List<MemberTopicPermissionResponse> memberTopicPermissionResponses = response.as(
                new TypeRef<>() {
                });
        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.OK.value());
        assertThat(memberTopicPermissionResponses)
                .hasSize(2)
                .extracting(MemberTopicPermissionResponse::memberResponse)
                .usingRecursiveComparison()
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
        Member member = memberRepository.save(
                MemberFixture.create("memberrr", "memberrr@naver.com", Role.USER));
        String authHeader = Base64.encodeBase64String(
                ("Basic " + creator.getMemberInfo().getEmail()).getBytes()
        );
        Topic topic = topicRepository.save(TopicFixture.createByName("topicName", creator));
        MemberTopicPermission memberTopicPermission =
                MemberTopicPermission.createPermissionAssociatedWithTopicAndMember(topic, member);

        // when
        memberTopicPermission = memberTopicPermissionRepository.save(memberTopicPermission);
        ExtractableResponse<Response> response = given().log().all()
                .header(AUTHORIZATION, authHeader)
                .when().get("/members/permissions/" + memberTopicPermission.getId())
                .then().log().all()
                .extract();

        // then
        MemberTopicPermissionDetailResponse memberTopicPermissionDetailResponse = response.as(
                MemberTopicPermissionDetailResponse.class);
        assertThat(response.statusCode())
                .isEqualTo(HttpStatus.OK.value());
        assertThat(memberTopicPermissionDetailResponse)
                .extracting(MemberTopicPermissionDetailResponse::memberDetailResponse)
                .usingRecursiveComparison()
                .ignoringFieldsOfTypes(LocalDateTime.class)
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
        ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/members")
                .then().log().all()
                .extract();

        // then
        assertThat(response.header("Location")).isNotBlank();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("유저 목록을 조회한다.")
    void findAllMember() {
        // given
        Member member = Member.of(
                "member",
                "member@naver.com",
                "https://map-befine-official.github.io/favicon.png",
                Role.USER
        );
        Member memberr = Member.of(
                "memberr",
                "memberr@naver.com",
                "https://map-befine-official.github.io/favicon.png",
                Role.USER
        );
        String authHeader = Base64.encodeBase64String(
                ("Basic " + member.getMemberInfo().getEmail()).getBytes()
        );

        // when
        memberRepository.save(member);
        memberRepository.save(memberr);

        ExtractableResponse<Response> response = given().log().all()
                .header(AUTHORIZATION, authHeader)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members")
                .then().log().all()
                .extract();

        List<MemberResponse> memberResponses = response.as(new TypeRef<>() {
        });

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(memberResponses).hasSize(2)
                .usingRecursiveComparison()
                .isEqualTo(List.of(MemberResponse.from(member), MemberResponse.from(memberr)));
    }

    @Test
    @DisplayName("유저를 단일 조회한다.")
    void findMemberById() {
        // given
        Member member = Member.of(
                "member",
                "member@naver.com",
                "https://map-befine-official.github.io/favicon.png",
                Role.USER
        );
        String authHeader = Base64.encodeBase64String(
                ("Basic " + member.getMemberInfo().getEmail()).getBytes()
        );

        // when
        member = memberRepository.save(member);

        ExtractableResponse<Response> response = given().log().all()
                .header(AUTHORIZATION, authHeader)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/" + member.getId())
                .then().log().all()
                .extract();

        MemberDetailResponse memberDetailResponse = response.as(MemberDetailResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(memberDetailResponse)
                .usingRecursiveComparison()
                .ignoringFields("updatedAt")
                .isEqualTo(MemberDetailResponse.from(member));
    }

    @Test
    @DisplayName("유저가 생성한 핀을 조회한다.")
    void findPinsByMember() {
        // given
        Member creator = memberRepository.save(
                MemberFixture.create(
                        "member",
                        "member@naver.com",
                        Role.USER
                )
        );
        Location location = locationRepository.save(LocationFixture.create());
        Topic topic = topicRepository.save(TopicFixture.createByName("topic", creator));
        Pin pin1 = pinRepository.save(
                PinFixture.create(
                        location,
                        topic,
                        creator
                )
        );
        Pin pin2 = pinRepository.save(
                PinFixture.create(
                        location,
                        topic,
                        creator
                )
        );
        String authHeader = Base64.encodeBase64String(
                ("Basic " + creator.getMemberInfo().getEmail()).getBytes()
        );

        // when
        ExtractableResponse<Response> response = given().log().all()
                .header(AUTHORIZATION, authHeader)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/pins")
                .then().log().all()
                .extract();
        List<PinResponse> pinResponses = response.as(new TypeRef<>() {
        });

        // then
        assertThat(pinResponses).hasSize(2)
                .usingRecursiveComparison()
                .isEqualTo(List.of(PinResponse.from(pin1), PinResponse.from(pin2)));
    }

    @Test
    @DisplayName("유저가 생성한 토픽을 조회한다.")
    void findTopicsByMember() {
        // given
        Member creator = memberRepository.save(
                MemberFixture.create("member", "member@naver.com", Role.USER)
        );
        Topic topic1 = topicRepository.save(TopicFixture.createByName("topic1", creator));
        Topic topic2 = topicRepository.save(TopicFixture.createByName("topic2", creator));
        String authHeader = Base64.encodeBase64String(
                ("Basic " + creator.getMemberInfo().getEmail()).getBytes()
        );

        // when
        ExtractableResponse<Response> response = given().log().all()
                .header(AUTHORIZATION, authHeader)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/topics")
                .then().log().all()
                .extract();
        List<TopicResponse> topicResponses = response.as(new TypeRef<>() {
        });

        // then
        assertThat(topicResponses).hasSize(2)
                .usingRecursiveComparison()
                .ignoringFieldsOfTypes(LocalDateTime.class)
                .isEqualTo(List.of(TopicResponse.from(topic1), TopicResponse.from(topic2)));
    }

    @Test
    @DisplayName("유저가 토픽을 즐겨찾기 목록에 추가한다.")
    void addTopicInBookmark_Success() {
        //given
        Member creator = MemberFixture.create("member", "member@naver.com", Role.USER);
        memberRepository.save(creator);

        Topic topic = TopicFixture.createByName("topic1", creator);
        topicRepository.save(topic);

        Member otherUser =
                MemberFixture.create("otherUser", "otherUse@naver.com", Role.USER);
        memberRepository.save(otherUser);

        String authHeader = Base64.encodeBase64String(
                ("Basic " + otherUser.getMemberInfo().getEmail()).getBytes()
        );

        //when
        ExtractableResponse<Response> response = given().log().all()
                .header(AUTHORIZATION, authHeader)
                .param("topicId", topic.getId())
                .when().post("/members/bookmarks")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).startsWith("/members/bookmarks/")
                .isNotNull();
    }

    @Test
    @DisplayName("유저의 즐겨찾기 토픽 목록을 조회한다.")
    void findTopicsInBookmarks_Success() {
        //given
        Member creator = MemberFixture.create("member", "member@naver.com", Role.USER);
        memberRepository.save(creator);

        Topic topic1 = TopicFixture.createByName("topic1", creator);
        Topic topic2 = TopicFixture.createByName("topic1", creator);
        topicRepository.save(topic1);
        topicRepository.save(topic2);

        Member otherUser =
                MemberFixture.create("otherUser", "otherUse@naver.com", Role.USER);
        memberRepository.save(otherUser);

        MemberTopicBookmark bookmarkedTopic1 =
                MemberTopicBookmark.createWithAssociatedTopicAndMember(topic1, otherUser);
        MemberTopicBookmark bookmarkedTopic2 =
                MemberTopicBookmark.createWithAssociatedTopicAndMember(topic2, otherUser);

        memberTopicBookmarkRepository.save(bookmarkedTopic1);
        memberTopicBookmarkRepository.save(bookmarkedTopic2);

        String authHeader = Base64.encodeBase64String(
                ("Basic " + otherUser.getMemberInfo().getEmail()).getBytes()
        );

        //when
        List<MemberTopicBookmarkResponse> response = given().log().all()
                .header(AUTHORIZATION, authHeader)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/bookmarks")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(new TypeRef<>() {
                });

        //then
        assertThat(response).hasSize(2)
                .usingRecursiveComparison()
                .ignoringFieldsOfTypes(LocalDateTime.class)
                .isEqualTo(List.of(
                        MemberTopicBookmarkResponse.from(bookmarkedTopic1),
                        MemberTopicBookmarkResponse.from(bookmarkedTopic2))
                );
    }

    @Test
    @DisplayName("유저의 즐겨찾기 토픽을 삭제한다.")
    void deleteTopicInBookmark_Success() {
        //given
        Member creator = MemberFixture.create("member", "member@naver.com", Role.USER);
        memberRepository.save(creator);

        Topic topic = TopicFixture.createByName("topic1", creator);
        topicRepository.save(topic);

        MemberTopicBookmark bookmarkedTopic =
                MemberTopicBookmark.createWithAssociatedTopicAndMember(topic, creator);
        memberTopicBookmarkRepository.save(bookmarkedTopic);

        String authHeader = Base64.encodeBase64String(
                ("Basic " + creator.getMemberInfo().getEmail()).getBytes()
        );

        //when then
        given().log().all()
                .header(AUTHORIZATION, authHeader)
                .when().delete("/members/bookmarks/" + bookmarkedTopic.getId())
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("유저의 즐겨찾기 토픽을 전체 삭제한다.")
    void deleteAllTopicsInBookmark_Success() {
        //given
        Member creator = MemberFixture.create("member", "member@naver.com", Role.USER);
        memberRepository.save(creator);

        Topic topic1 = TopicFixture.createByName("topic1", creator);
        Topic topic2 = TopicFixture.createByName("topic1", creator);
        topicRepository.save(topic1);
        topicRepository.save(topic2);

        MemberTopicBookmark bookmarkedTopic1 =
                MemberTopicBookmark.createWithAssociatedTopicAndMember(topic1, creator);
        MemberTopicBookmark bookmarkedTopic2 =
                MemberTopicBookmark.createWithAssociatedTopicAndMember(topic2, creator);
        memberTopicBookmarkRepository.save(bookmarkedTopic1);
        memberTopicBookmarkRepository.save(bookmarkedTopic2);

        String authHeader = Base64.encodeBase64String(
                ("Basic " + creator.getMemberInfo().getEmail()).getBytes()
        );

        //when then
        given().log().all()
                .header(AUTHORIZATION, authHeader)
                .when().delete("/members/bookmarks/")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

}
