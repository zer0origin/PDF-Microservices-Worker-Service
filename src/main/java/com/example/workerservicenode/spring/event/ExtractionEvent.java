package com.example.workerservicenode.spring.event;

import network.queue.request.ExtractionQueueRequest;
import org.springframework.context.ApplicationEvent;

public class ExtractionEvent extends ApplicationEvent {
    private ExtractionQueueRequest entity;

    public ExtractionEvent(Object source, ExtractionQueueRequest entity) {
        super(source);
        this.entity = entity;
    }

    public ExtractionQueueRequest getEntity() {
        return entity;
    }
}
