package com.mapbefine.mapbefine.pin.event;

import java.util.List;

public record PinBatchDeleteEvent(List<Long> pinIds) {
}
