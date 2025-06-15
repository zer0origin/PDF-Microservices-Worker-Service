package com.example.workerservicenode.event;

import network.ExtractionRequest;
import org.springframework.context.ApplicationEvent;

public class ExtractionEvent extends ApplicationEvent {
    private ExtractionRequest entity;

    public ExtractionEvent(Object source, ExtractionRequest entity) {
        super(source);
        this.entity = entity;
    }

    public ExtractionRequest getEntity() {
        return entity;
    }
}
