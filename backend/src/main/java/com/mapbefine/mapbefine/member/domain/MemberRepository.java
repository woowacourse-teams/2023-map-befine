package com.mapbefine.mapbefine.member.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberInfoEmail(String email);

    boolean existsByMemberInfoEmail(String email);

    Optional<Member> findByOauthIdOauthServerId(Long oauthServerId);

    Optional<Member> findByOauthId(OauthId oauthId);

}
