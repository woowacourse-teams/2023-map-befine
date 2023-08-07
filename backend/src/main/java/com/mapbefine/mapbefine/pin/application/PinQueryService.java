package com.mapbefine.mapbefine.pin.application;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.domain.PinRepository;
import com.mapbefine.mapbefine.pin.dto.response.PinDetailResponse;
import com.mapbefine.mapbefine.pin.dto.response.PinResponse;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class PinQueryService {

    private final PinRepository pinRepository;

    public PinQueryService(PinRepository pinRepository) {
        this.pinRepository = pinRepository;
    }

    public List<PinResponse> findAll(AuthMember member) {
        return pinRepository.findAll()
                .stream()
                .filter(pin -> member.canRead(pin.getTopic()))
                .map(PinResponse::from)
                .toList();
    }

    public PinDetailResponse findById(AuthMember member, Long pinId) {
        Pin pin = pinRepository.findById(pinId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 핀입니다."));

        if (member.canRead(pin.getTopic())) {
            // TODO 모든 PinImage 조회
            return PinDetailResponse.from(pin);
        }
        throw new IllegalArgumentException("해당 토픽의 핀 조회 권한이 없습니다.");
    }

}
