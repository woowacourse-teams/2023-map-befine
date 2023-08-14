package com.mapbefine.mapbefine.bookmark.presentation;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.assertj.core.api.Assertions.assertThat;

import com.mapbefine.mapbefine.bookmark.domain.Bookmark;
import com.mapbefine.mapbefine.bookmark.domain.BookmarkRepository;
import com.mapbefine.mapbefine.common.IntegrationTest;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.List;
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
    @DisplayName("유저가 토픽을 즐겨찾기 목록에 추가하면, 201을 반환한다.")
    void addTopicInBookmark_Success() {
        //given
        Member creator = MemberFixture.create("member", "member@naver.com", Role.USER);
        memberRepository.save(creator);

        Topic topic = TopicFixture.createByName("topic1", creator);
        topicRepository.save(topic);

        Member otherUser =
                MemberFixture.create("otherUser", "otherUse@naver.com", Role.USER);
        memberRepository.save(otherUser);

        String otherUserAuthHeader = testAuthHeaderProvider.createAuthHeader(otherUser);

        //when
        ExtractableResponse<Response> response = given().log().all()
                .header(AUTHORIZATION, otherUserAuthHeader)
                .param("topicId", topic.getId())
                .when().post("/bookmarks")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).startsWith("/bookmarks/")
                .isNotNull();
    }

    @Test
    @DisplayName("유저의 즐겨찾기 토픽 목록을 조회하면, 200을 반환한다.")
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

        String otherUserAuthHeader = testAuthHeaderProvider.createAuthHeader(otherUser);

        //when
        List<TopicResponse> response = given().log().all()
                .header(AUTHORIZATION, otherUserAuthHeader)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/bookmarks")
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
                        TopicResponse.from(topic1, Boolean.TRUE),
                        TopicResponse.from(topic2, Boolean.TRUE))
                );
    }

    @Test
    @DisplayName("유저의 즐겨찾기 토픽을 삭제하면, 204를 반환한다.")
    void deleteTopicInBookmark_Success() {
        //given
        Member creator = MemberFixture.create("member", "member@naver.com", Role.USER);
        memberRepository.save(creator);

        Topic topic = TopicFixture.createByName("topic1", creator);
        topicRepository.save(topic);

        Bookmark bookmark = Bookmark.createWithAssociatedTopicAndMember(topic, creator);
        bookmarkRepository.save(bookmark);

        String creatorAuthHeader = testAuthHeaderProvider.createAuthHeader(creator);

        //when then
        given().log().all()
                .header(AUTHORIZATION, creatorAuthHeader)
                .param("topicId", topic.getId())
                .when().delete("/bookmarks")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

}
