package com.mapbefine.mapbefine.auth.domain.token;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class RefreshToken {

    @Id
    private String token;

    @Column(nullable = false, unique = true)
    private Long memberId;

    public RefreshToken(String token, Long memberId) {
        this.token = token;
        this.memberId = memberId;
    }

}
