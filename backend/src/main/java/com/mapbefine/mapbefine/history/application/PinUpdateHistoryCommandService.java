package com.mapbefine.mapbefine.history.application;

import com.mapbefine.mapbefine.history.domain.PinUpdateHistory;
import com.mapbefine.mapbefine.history.domain.PinUpdateHistoryRepository;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.event.PinUpdateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PinUpdateHistoryCommandService {

    private final PinUpdateHistoryRepository pinUpdateHistoryRepository;

    public PinUpdateHistoryCommandService(PinUpdateHistoryRepository pinUpdateHistoryRepository) {
        this.pinUpdateHistoryRepository = pinUpdateHistoryRepository;
    }

    @EventListener
    public void saveHistory(PinUpdateEvent event) {
        Pin pin = event.pin();
        pinUpdateHistoryRepository.save(new PinUpdateHistory(pin, event.member()));

        log.debug("pin history saved for update pin id =: {}", pin.getId());
    }

}
