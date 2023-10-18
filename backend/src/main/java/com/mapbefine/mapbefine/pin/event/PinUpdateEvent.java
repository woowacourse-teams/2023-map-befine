package com.mapbefine.mapbefine.pin.event;

import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.pin.domain.Pin;

public record PinUpdateEvent(Pin pin, Member member) {
}
