package com.mapbefine.mapbefine.atlas.domain;

import static lombok.AccessLevel.PROTECTED;

import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.topic.domain.Topic;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Atlas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private Atlas(Topic topic, Member member) {
        this.topic = topic;
        this.member = member;
    }

    public static Atlas from(Topic topic, Member member) {
        validateNotNull(topic, member);
        return new Atlas(topic, member);
    }

    private static void validateNotNull(Topic topic, Member member) {
        if (Objects.isNull(topic) || Objects.isNull(member)) {
            throw new IllegalArgumentException("토픽과 멤버는 Null이어선 안됩니다.");
        }
    }

}
