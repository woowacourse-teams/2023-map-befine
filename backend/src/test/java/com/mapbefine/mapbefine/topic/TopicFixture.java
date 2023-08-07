package com.mapbefine.mapbefine.topic;


import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.topic.domain.Permission;
import com.mapbefine.mapbefine.topic.domain.Publicity;
import com.mapbefine.mapbefine.topic.domain.Topic;

public class TopicFixture {

    private static final String IMAGE_URL = "https://map-befine-official.github.io/favicon.png";

    public static Topic createPrivateAndGroupOnlyTopic(Member member) {
        return Topic.createTopicAssociatedWithCreator(
                "토픽 멤버만 읽을 수 있는 토픽",
                "토픽 멤버만 읽을 수 있습니다.",
                IMAGE_URL,
                Publicity.PRIVATE,
                Permission.GROUP_ONLY,
                member
        );
    }

    public static Topic createPublicAndAllMembersTopic(Member member) {
        return Topic.createTopicAssociatedWithCreator(
                "아무나 읽을 수 있는 토픽",
                "아무나 읽을 수 있습니다.",
                IMAGE_URL,
                Publicity.PUBLIC,
                Permission.ALL_MEMBERS,
                member
        );
    }

    public static Topic createByName(String name, Member member) {
        return Topic.createTopicAssociatedWithCreator(
                name,
                "설명",
                null,
                Publicity.PUBLIC,
                Permission.ALL_MEMBERS,
                member
        );
    }

}
