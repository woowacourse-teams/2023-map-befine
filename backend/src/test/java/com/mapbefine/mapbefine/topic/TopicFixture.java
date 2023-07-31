package com.mapbefine.mapbefine.topic;


import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.topic.domain.Permission;
import com.mapbefine.mapbefine.topic.domain.Publicity;
import com.mapbefine.mapbefine.topic.domain.Topic;

public class TopicFixture {

    public static Topic createByName(String name, Member member) {
        return Topic.of(
                name,
                "설명",
                null,
                Publicity.PUBLIC,
                Permission.ALL_MEMBERS,
                member
        );
    }

}