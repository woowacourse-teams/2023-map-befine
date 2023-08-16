package com.mapbefine.mapbefine.bookmark.domain;

import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.topic.domain.Topic;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private Bookmark(Topic topic, Member member) {
        this.topic = topic;
        this.member = member;
    }

    public static Bookmark createWithAssociatedTopicAndMember(Topic topic, Member member) {
        validateNotNull(topic, member);
        Bookmark bookmark = new Bookmark(topic, member);

        topic.addBookmark(bookmark);
        member.addBookmark(bookmark);

        return bookmark;
    }

    private static void validateNotNull(Topic topic, Member member) {
        if (Objects.isNull(topic) || Objects.isNull(member)) {
            throw new IllegalArgumentException("지도와 유저는 Null이어선 안됩니다.");
        }
    }

}
