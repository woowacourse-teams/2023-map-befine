package com.mapbefine.mapbefine.member.domain;

import com.mapbefine.mapbefine.topic.domain.Topic;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberTopicPermissionRepository extends JpaRepository<MemberTopicPermission, Long> {

    List<MemberTopicPermission> findByTopic(Topic topic);

    boolean existsByTopicAndMember(Topic topic, Member member);

}
