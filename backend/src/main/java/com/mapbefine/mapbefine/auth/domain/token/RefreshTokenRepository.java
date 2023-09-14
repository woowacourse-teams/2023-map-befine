package com.mapbefine.mapbefine.auth.domain.token;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    Long findMemberIdByToken(String token);

    void deleteByMemberId(Long memberId);
}
