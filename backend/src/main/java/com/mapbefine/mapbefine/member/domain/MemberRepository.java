package com.mapbefine.mapbefine.member.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findById(Long id);

    Optional<Member> findByOauthId(OauthId oauthId);

    boolean existsByMemberInfoNickName(String nickName);

    List<Member> findAllByMemberInfoRole(Role role);

}
