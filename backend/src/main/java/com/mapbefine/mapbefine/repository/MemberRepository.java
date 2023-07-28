package com.mapbefine.mapbefine.repository;

import com.mapbefine.mapbefine.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
