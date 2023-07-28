package com.mapbefine.mapbefine;


import com.mapbefine.mapbefine.entity.member.Member;
import com.mapbefine.mapbefine.entity.topic.Permission;
import com.mapbefine.mapbefine.entity.topic.Publicity;
import com.mapbefine.mapbefine.entity.topic.Topic;

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
