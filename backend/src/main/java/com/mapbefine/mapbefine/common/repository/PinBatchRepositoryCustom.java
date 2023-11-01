package com.mapbefine.mapbefine.common.repository;

import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.topic.domain.Topic;
import java.util.List;

public interface PinBatchRepositoryCustom {

    int[] saveAllToTopic(Topic topicForCopy, List<Pin> originalPins);

}
