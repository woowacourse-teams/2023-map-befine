package com.mapbefine.mapbefine.topic;


import com.mapbefine.mapbefine.image.FileFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.topic.domain.PermissionType;
import com.mapbefine.mapbefine.topic.domain.Publicity;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.dto.request.TopicCreateRequest;
import com.mapbefine.mapbefine.topic.dto.request.TopicMergeRequest;
import java.util.List;
import org.apache.http.conn.util.PublicSuffixList;

public class TopicFixture {

    private static final String IMAGE_URL = "https://map-befine-official.github.io/favicon.png";

    public static Topic createByPublicityAndPermissionTypeAndCreator(
            Publicity publicity,
            PermissionType permissionType,
            Member creator
    ) {
        return Topic.createTopicAssociatedWithCreator(
                "토픽",
                "토픽의 Publicity, PermissionType 이 동적으로 정해집니다.",
                IMAGE_URL,
                publicity,
                permissionType,
                creator
        );
    }

    public static Topic createPrivateAndGroupOnlyTopic(Member member) {
        return Topic.createTopicAssociatedWithCreator(
                "토픽 회원만 읽을 수 있는 토픽",
                "토픽 회원만 읽을 수 있습니다.",
                IMAGE_URL,
                Publicity.PRIVATE,
                PermissionType.GROUP_ONLY,
                member
        );
    }

    public static Topic createPublicAndAllMembersTopic(Member member) {
        return Topic.createTopicAssociatedWithCreator(
                "아무나 읽을 수 있는 토픽",
                "아무나 읽을 수 있습니다.",
                IMAGE_URL,
                Publicity.PUBLIC,
                PermissionType.ALL_MEMBERS,
                member
        );
    }

    public static Topic createPublicAndGroupOnlyTopic(Member member) {
        return Topic.createTopicAssociatedWithCreator(
                "아무나 읽을 수 있는 토픽",
                "아무나 읽지만 아무나 생성할 수는 없습니다.",
                IMAGE_URL,
                Publicity.PUBLIC,
                PermissionType.GROUP_ONLY,
                member
        );
    }

    public static Topic createPublicAndAllMembersTopic(String imageUrl, Member member) {
        return Topic.createTopicAssociatedWithCreator(
                "아무나 읽을 수 있는 토픽",
                "아무나 읽을 수 있습니다.",
                imageUrl,
                Publicity.PUBLIC,
                PermissionType.ALL_MEMBERS,
                member
        );
    }

    public static Topic createByName(String name, Member member) {
        return Topic.createTopicAssociatedWithCreator(
                name,
                "설명",
                IMAGE_URL,
                Publicity.PUBLIC,
                PermissionType.ALL_MEMBERS,
                member
        );
    }

    public static Topic createPrivateByName(String name, Member member) {
        return Topic.createTopicAssociatedWithCreator(
                name,
                "설명",
                IMAGE_URL,
                Publicity.PRIVATE,
                PermissionType.GROUP_ONLY,
                member
        );
    }

    public static TopicCreateRequest createPublicAndAllMembersCreateRequestWithPins(
            List<Long> pinIds
    ) {
        return new TopicCreateRequest(
                "아무나 읽을 수 있는 토픽",
                FileFixture.createFile(),
                "아무나 읽을 수 있는 토픽입니다.",
                Publicity.PUBLIC,
                PermissionType.ALL_MEMBERS,
                pinIds
        );
    }

    public static TopicCreateRequest createPublicAndAllMembersAndEmptyImageCreateRequestWithPins(
            List<Long> pinIds
    ) {
        return new TopicCreateRequest(
                "아무나 읽을 수 있는 토픽",
                null,
                "아무나 읽을 수 있는 토픽입니다.",
                Publicity.PUBLIC,
                PermissionType.ALL_MEMBERS,
                pinIds
        );
    }

    public static TopicMergeRequest createPublicAndAllMembersMergeRequestWithTopics(
            List<Long> topicIds
    ) {
        return new TopicMergeRequest(
                "아무나 읽을 수 있는 토픽",
                FileFixture.createFile(),
                "아무나 읽을 수 있는 토픽입니다.",
                Publicity.PUBLIC,
                PermissionType.ALL_MEMBERS,
                topicIds
        );
    }

}
