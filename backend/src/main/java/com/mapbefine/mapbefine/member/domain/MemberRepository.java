package com.mapbefine.mapbefine.member.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberInfoEmail(String email);

    boolean existsByMemberInfoName(String name);

    boolean existsByMemberInfoEmail(String email);

}
