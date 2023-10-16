package com.mapbefine.mapbefine.history.domain;

import static lombok.AccessLevel.PROTECTED;

import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.pin.domain.Pin;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class PinUpdateHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pin_id", nullable = false)
    private Pin pin;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public PinUpdateHistory(Pin pin, Member member) {
        this.pin = pin;
        this.member = member;
    }

}
