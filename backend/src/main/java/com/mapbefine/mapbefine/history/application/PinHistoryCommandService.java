package com.mapbefine.mapbefine.history.application;

import com.mapbefine.mapbefine.history.domain.PinHistory;
import com.mapbefine.mapbefine.history.domain.PinHistoryRepository;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.event.PinUpdateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PinHistoryCommandService {

    private final PinHistoryRepository pinHistoryRepository;

    public PinHistoryCommandService(PinHistoryRepository pinHistoryRepository) {
        this.pinHistoryRepository = pinHistoryRepository;
    }

    @EventListener
    public void saveHistory(PinUpdateEvent event) {
        Pin pin = event.pin();
        pinHistoryRepository.save(new PinHistory(pin, event.member()));

        log.debug("pin history saved for update pin id =: {}", pin.getId());
    }

}
