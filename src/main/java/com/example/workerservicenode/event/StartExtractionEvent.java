package com.example.workerservicenode.event;

import dto.extraction.DocumentQueueEntity;
import org.springframework.context.ApplicationEvent;

public class StartExtractionEvent extends ApplicationEvent {
    private DocumentQueueEntity entity;

    public StartExtractionEvent(Object source, DocumentQueueEntity entity) {
        super(source);
        this.entity = entity;
    }

    public DocumentQueueEntity getEntity() {
        return entity;
    }
}
