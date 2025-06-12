package com.example.workerservicenode.event;

import dto.extraction.DocumentQueueEntity;
import org.springframework.context.ApplicationEvent;

public class ExtractionEvent extends ApplicationEvent {
    private DocumentQueueEntity entity;

    public ExtractionEvent(Object source, DocumentQueueEntity entity) {
        super(source);
        this.entity = entity;
    }

    public DocumentQueueEntity getEntity() {
        return entity;
    }
}
