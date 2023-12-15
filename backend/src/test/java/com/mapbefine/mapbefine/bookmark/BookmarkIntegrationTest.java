package com.mapbefine.mapbefine.bookmark;

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
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.assertj.core.api.Assertions.assertThat;

class BookmarkIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Test
    @DisplayName("회원이 토픽을 즐겨찾기 목록에 추가하면, 201을 반환한다.")
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
                .when().post("/bookmarks/topics/" + topic.getId())
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("회원의 즐겨찾기 토픽을 삭제하면, 204를 반환한다.")
    void deleteTopicInBookmark_Success() {
        //given
        Member creator = MemberFixture.create("member", "member@naver.com", Role.USER);
        memberRepository.save(creator);

        Topic topic = TopicFixture.createByName("topic1", creator);
        topicRepository.save(topic);

        Bookmark bookmark = Bookmark.of(topic, creator.getId());
        bookmarkRepository.save(bookmark);

        String creatorAuthHeader = testAuthHeaderProvider.createAuthHeader(creator);

        //when then
        given().log().all()
                .header(AUTHORIZATION, creatorAuthHeader)
                .when().delete("/bookmarks/topics/" + topic.getId())
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

}
