package com.mapbefine.mapbefine.permission.domain;

import com.mapbefine.mapbefine.member.dto.response.MemberResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PermissionRepository extends JpaRepository<Permission, PermissionId> {

    boolean existsByIdTopicIdAndIdMemberId(Long topicId, Long memberId);

    @Query(value = "SELECT p.id.topicId FROM Permission p WHERE p.id.memberId = :memberId")
    List<Long> findAllTopicIdsByMemberId(@Param("memberId") Long memberId);

    @Query(value = "SELECT new com.mapbefine.mapbefine.member.dto.response.MemberResponse(m.id, m.memberInfo.nickName) " +
            "FROM Permission p " +
            "JOIN Member m ON m.id = p.id.memberId " +
            "WHERE p.id.topicId = :topicId")
    List<MemberResponse> findAllPermittedMembersByTopicId(@Param("topicId") Long topicId);

    @Modifying(clearAutomatically = true)
    @Query("delete from Permission p where p.id.memberId = :memberId")
    void deleteAllByMemberId(@Param("memberId") Long memberId);

    @Modifying(clearAutomatically = true)
    @Query("delete from Permission p where p.id.topicId = :topicId")
    void deleteAllByTopicId(@Param("topicId") Long topicId);
}
