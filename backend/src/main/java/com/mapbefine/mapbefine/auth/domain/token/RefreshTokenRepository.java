package com.mapbefine.mapbefine.auth.domain.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    boolean existsByMemberId(Long memberId);

    void deleteByMemberId(Long memberId);

    @Modifying(clearAutomatically = true)
    void delete(RefreshToken token);

}
