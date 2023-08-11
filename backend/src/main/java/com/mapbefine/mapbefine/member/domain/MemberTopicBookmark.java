package com.mapbefine.mapbefine.member.domain;

import com.mapbefine.mapbefine.topic.domain.Topic;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberTopicBookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private MemberTopicBookmark(Topic topic, Member member) {
        this.topic = topic;
        this.member = member;
    }

    // TODO: 2023/08/11 필요한 검증이 무엇이 있을까.. 현재로썬 외부에서 검증하는 방법 밖에 ?
    public static MemberTopicBookmark createWithAssociatedTopicAndMember(
            Topic topic,
            Member member
    ) {
        MemberTopicBookmark memberTopicBookmark = new MemberTopicBookmark(topic, member);

        topic.addBookmarkedMember(memberTopicBookmark);
        member.addTopicInBookmark(memberTopicBookmark);

        return memberTopicBookmark;
    }

}
