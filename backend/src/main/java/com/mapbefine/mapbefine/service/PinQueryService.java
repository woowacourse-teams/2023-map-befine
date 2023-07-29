package com.mapbefine.mapbefine.service;

import com.mapbefine.mapbefine.config.auth.AuthMember;
import com.mapbefine.mapbefine.config.auth.AuthTopic;
import com.mapbefine.mapbefine.dto.PinDetailResponse;
import com.mapbefine.mapbefine.dto.PinResponse;
import com.mapbefine.mapbefine.entity.pin.Pin;
import com.mapbefine.mapbefine.repository.PinRepository;
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
                .filter(pin -> member.canRead(AuthTopic.from(pin.getTopic())))
                .map(PinResponse::from)
                .toList();
    }

    public PinDetailResponse findById(AuthMember member, Long pinId) {
        Pin pin = pinRepository.findById(pinId)
                .filter(optionalPin -> member.canRead(AuthTopic.from(optionalPin.getTopic())))
                .orElseThrow(NoSuchElementException::new);

        return PinDetailResponse.from(pin);
    }

}
