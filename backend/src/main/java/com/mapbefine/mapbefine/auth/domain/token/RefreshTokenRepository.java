package com.mapbefine.mapbefine.auth.domain.token;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    Optional<RefreshToken> findByMemberId(Long memberId);

    void deleteByMemberId(Long memberId);

}
