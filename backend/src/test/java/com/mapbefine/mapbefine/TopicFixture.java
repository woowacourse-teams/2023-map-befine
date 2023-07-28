package com.mapbefine.mapbefine;


import com.mapbefine.mapbefine.entity.member.Role;
import com.mapbefine.mapbefine.entity.topic.Permission;
import com.mapbefine.mapbefine.entity.topic.Publicity;
import com.mapbefine.mapbefine.entity.topic.Topic;

public class TopicFixture {

    public static Topic createByName(String name) { // TODO : 추후에 Fixture 를 더 구체화 할 수 있도록
        return new Topic(
                name,
                "설명",
                null,
                Publicity.PUBLIC,
                Permission.ALL_MEMBERS,
                MemberFixture.create(Role.ADMIN)
        );
    }

}
