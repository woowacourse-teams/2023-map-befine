package com.mapbefine.mapbefine.bookmark.presentation;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.bookmark.domain.Bookmark;
import com.mapbefine.mapbefine.bookmark.domain.BookmarkRepository;
import com.mapbefine.mapbefine.bookmark.dto.response.BookmarkResponse;
import com.mapbefine.mapbefine.common.IntegrationTest;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
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

public class BookmarkIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private BookmarkRepository bookmarkRepository;

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

        Bookmark bookmark1 = Bookmark.createWithAssociatedTopicAndMember(topic1, otherUser);
        Bookmark bookmark2 = Bookmark.createWithAssociatedTopicAndMember(topic2, otherUser);

        bookmarkRepository.save(bookmark1);
        bookmarkRepository.save(bookmark2);

        String authHeader = Base64.encodeBase64String(
                ("Basic " + otherUser.getMemberInfo().getEmail()).getBytes()
        );

        //when
        List<BookmarkResponse> response = given().log().all()
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
                        BookmarkResponse.from(bookmark1),
                        BookmarkResponse.from(bookmark2))
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

        Bookmark bookmark = Bookmark.createWithAssociatedTopicAndMember(topic, creator);
        bookmarkRepository.save(bookmark);

        String authHeader = Base64.encodeBase64String(
                ("Basic " + creator.getMemberInfo().getEmail()).getBytes()
        );

        //when then
        given().log().all()
                .header(AUTHORIZATION, authHeader)
                .when().delete("/members/bookmarks/" + bookmark.getId())
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

        Bookmark bookmark1 = Bookmark.createWithAssociatedTopicAndMember(topic1, creator);
        Bookmark bookmark2 = Bookmark.createWithAssociatedTopicAndMember(topic2, creator);
        bookmarkRepository.save(bookmark1);
        bookmarkRepository.save(bookmark2);

        String authHeader = Base64.encodeBase64String(
                ("Basic " + creator.getMemberInfo().getEmail()).getBytes()
        );

        //when then
        given().log().all()
                .header(AUTHORIZATION, authHeader)
                .when().delete("/members/bookmarks")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

}
