package com.mapbefine.mapbefine.history.application;

import com.mapbefine.mapbefine.history.domain.PinUpdateHistory;
import com.mapbefine.mapbefine.history.domain.PinUpdateHistoryRepository;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.event.PinBatchDeleteEvent;
import com.mapbefine.mapbefine.pin.event.PinDeleteEvent;
import com.mapbefine.mapbefine.pin.event.PinUpdateEvent;
import java.util.List;
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

        log.debug("pin history saved for pin id =: {}", pin.getId());
    }

    @EventListener
    public void deleteHistory(PinDeleteEvent event) {
        Long pinId = event.pinId();
        pinUpdateHistoryRepository.deleteAllByPinId(pinId);

        log.debug("pin history deleted for pin id =: {}", pinId);
    }

    @EventListener
    public void deleteHistory(PinBatchDeleteEvent event) {
        List<Long> pinIds = event.pinIds();
        pinUpdateHistoryRepository.deleteAllByPinIds(pinIds);

        log.debug("pin history deleted for pin ids =: {}", pinIds);
    }

}
