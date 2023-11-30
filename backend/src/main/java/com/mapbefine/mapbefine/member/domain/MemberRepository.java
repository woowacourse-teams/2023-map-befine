package com.mapbefine.mapbefine.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByOauthId(OauthId oauthId);

    boolean existsByMemberInfoNickName(String nickName);

    List<Member> findAllByMemberInfoRole(Role role);

    @Query(value = "SELECT m.memberInfo.nickName FROM Member m WHERE m.id = :id")
    String findMemberInfoNicknameById(@Param("id") Long id);

}
