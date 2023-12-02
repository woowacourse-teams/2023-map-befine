package com.mapbefine.mapbefine.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByOauthId(OauthId oauthId);

    boolean existsByMemberInfoNickName(String nickName);

    List<Member> findAllByMemberInfoRole(Role role);

}
