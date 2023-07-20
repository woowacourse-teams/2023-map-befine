package com.mapbefine.mapbefine.service;

import com.mapbefine.mapbefine.dto.PinDetailResponse;
import com.mapbefine.mapbefine.dto.PinResponse;
import com.mapbefine.mapbefine.entity.Pin;
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

    public List<PinResponse> findAll() {
        return pinRepository.findAll()
                .stream()
                .map(PinResponse::from)
                .toList();
    }

    public PinDetailResponse findById(Long pinId) {
        Pin pin = pinRepository.findById(pinId)
                .orElseThrow(NoSuchElementException::new);

        return PinDetailResponse.from(pin);
    }

}
