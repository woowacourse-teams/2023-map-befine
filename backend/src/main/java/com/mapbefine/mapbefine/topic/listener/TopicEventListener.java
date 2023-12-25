package com.mapbefine.mapbefine.topic.listener;

import com.mapbefine.mapbefine.topic.domain.BookmarkAdditionEvent;
import com.mapbefine.mapbefine.topic.domain.BookmarkDeleteEvent;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TopicEventListener {

    private final TopicRepository topicRepository;

    public TopicEventListener(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    @EventListener
    public void removeBookmark(BookmarkDeleteEvent event) {
        topicRepository.decreaseBookmarkCountById(event.topicId());
    }

    @EventListener
    public void addBookmark(BookmarkAdditionEvent event) {
        topicRepository.increaseBookmarkCountById(event.topicId());
    }

}
