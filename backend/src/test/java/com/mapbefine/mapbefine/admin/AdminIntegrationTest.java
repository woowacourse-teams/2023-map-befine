package com.mapbefine.mapbefine.admin;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.mapbefine.mapbefine.admin.dto.AdminMemberDetailResponse;
import com.mapbefine.mapbefine.admin.dto.AdminMemberResponse;
import com.mapbefine.mapbefine.common.IntegrationTest;
import com.mapbefine.mapbefine.location.LocationFixture;
import com.mapbefine.mapbefine.location.domain.Location;
import com.mapbefine.mapbefine.location.domain.LocationRepository;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.pin.PinFixture;
import com.mapbefine.mapbefine.pin.PinImageFixture;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.domain.PinImage;
import com.mapbefine.mapbefine.pin.domain.PinImageRepository;
import com.mapbefine.mapbefine.pin.domain.PinRepository;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

class AdminIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private PinRepository pinRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private PinImageRepository pinImageRepository;

    @Value("${security.admin.key}")
    private String secretKey;

    private Location location;
    private Member member;
    private Topic topic;
    private Pin pin;
    private PinImage pinImage;

    @BeforeEach
    void setup() {
        member = memberRepository.save(MemberFixture.create("member", "member@gmail.com", Role.USER));
        topic = topicRepository.save(TopicFixture.createByName("topic", member));
        location = locationRepository.save(LocationFixture.create());
        pin = pinRepository.save(PinFixture.create(location, topic, member));
        topic = topicRepository.save(topic);
        pinImage = pinImageRepository.save(PinImageFixture.create(pin));
    }

    @Test
    @DisplayName("어드민 시크릿 키로 요청할 경우, 회원을 전체 조회할 수 있다.")
    void findAllMembers_Success() {
        //given
        Member member1 = MemberFixture.create("member1", "member1@gmail.com", Role.USER);
        Member member2 = MemberFixture.create("member2", "member2@gmail.com", Role.USER);
        memberRepository.save(member1);
        memberRepository.save(member2);

        //when
        List<AdminMemberResponse> response = given().log().all()
                .header(AUTHORIZATION, secretKey)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/admin/members")
                .then().log().all()
                .extract()
                .as(new TypeRef<>() {
                });

        //then
        List<AdminMemberResponse> expected = List.of(
                AdminMemberResponse.from(member),
                AdminMemberResponse.from(member1),
                AdminMemberResponse.from(member2)
        );

        assertThat(response).usingRecursiveComparison()
                .ignoringFields("updatedAt")
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("잘못된 시크릿 키로 요청할 경우, 회원을 전체 조회할 수 없다.")
    void findAllMembers_Fail() {
        given().log().all()
                .header(AUTHORIZATION, "wrongKey")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/admin/members/" + member.getId())
                .then().log().all()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @DisplayName("어드민 시크릿 키로 요청할 경우, 특정 회원의 상세 정보를 조회할 수 있다.")
    void findMemberDetail_Success() {
        //given when
        AdminMemberDetailResponse response = given().log().all()
                .header(AUTHORIZATION, secretKey)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/admin/members/" + member.getId())
                .then().log().all()
                .extract()
                .as(new TypeRef<>() {
                });
        System.out.println("====" + topic.getPinCount());
        //then

        AdminMemberDetailResponse expected = AdminMemberDetailResponse.of(
                member,
                member.getCreatedTopics(),
                member.getCreatedPins()
        );

        assertThat(response).usingRecursiveComparison()
                .ignoringFields("updatedAt")
                .ignoringFields("topics.updatedAt")
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("잘못된 시크릿 키로 요청할 경우, 특정 회원의 상세 정보를 조회할 수 없다.")
    void findMemberDetail_Fail() {
        given().log().all()
                .header(AUTHORIZATION, "wrongKey")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/admin/members/" + member.getId())
                .then().log().all()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @DisplayName("어드민 시크릿 키로 요청할 경우, 특정 회원을 삭제(차단)할 수 있다.")
    void deleteMember_Success() {
        given().log().all()
                .header(AUTHORIZATION, secretKey)
                .when().delete("/admin/members/" + member.getId())
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("잘못된 시크릿 키로 요청할 경우, 특정 회원을 삭제(차단)할 수 없다.")
    void deleteMember_Fail() {
        given().log().all()
                .header(AUTHORIZATION, "wrongKey")
                .when().delete("/admin/members/" + member.getId())
                .then().log().all()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @DisplayName("어드민 시크릿 키로 요청할 경우, 특정 토픽을 삭제할 수 있다.")
    void deleteTopic_Success() {
        given().log().all()
                .header(AUTHORIZATION, secretKey)
                .when().delete("/admin/topics/" + topic.getId())
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("잘못된 시크릿 키로 요청할 경우, 특정 토픽을 삭제할 수 없다.")
    void deleteTopic_Fail() {
        given().log().all()
                .header(AUTHORIZATION, "wrongKey")
                .when().delete("/admin/topics/" + topic.getId())
                .then().log().all()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @DisplayName("어드민 시크릿 키로 요청할 경우, 특정 토픽 이미지를 삭제할 수 있다.")
    void deleteTopicImage_Success() {
        given().log().all()
                .header(AUTHORIZATION, secretKey)
                .when().delete("/admin/topics/" + topic.getId() + "/images")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("잘못된 시크릿 키로 요청할 경우, 특정 토픽 이미지를 삭제할 수 없다.")
    void deleteTopicImage_Fail() {
        given().log().all()
                .header(AUTHORIZATION, "wrongKey")
                .when().delete("/admin/topics/" + topic.getId() + "/images")
                .then().log().all()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @DisplayName("어드민 시크릿 키로 요청할 경우, 특정 핀을 삭제할 수 있다.")
    void deletePin_Success() {
        given().log().all()
                .header(AUTHORIZATION, secretKey)
                .when().delete("/admin/pins/" + pin.getId())
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("잘못된 시크릿 키로 요청할 경우, 특정 핀을 삭제할 수 없다.")
    void deletePin_Fail() {
        given().log().all()
                .header(AUTHORIZATION, "wrongKey")
                .when().delete("/admin/pins/" + pin.getId())
                .then().log().all()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @DisplayName("어드민 시크릿 키로 요청할 경우, 특정 핀 이미지를 삭제할 수 있다.")
    void deletePinImage_Success() {
        given().log().all()
                .header(AUTHORIZATION, secretKey)
                .when().delete("/admin/pins/images/" + pinImage.getId())
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("잘못된 시크릿 키로 요청할 경우, 특정 핀 이미지를 삭제할 수 없다.")
    void deletePinImage_Fail() {
        given().log().all()
                .header(AUTHORIZATION, "wrongKey")
                .when().delete("/admin/pins/images/" + pinImage.getId())
                .then().log().all()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

}
