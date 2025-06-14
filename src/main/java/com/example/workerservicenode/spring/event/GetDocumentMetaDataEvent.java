package com.example.workerservicenode.spring.event;

import org.springframework.context.ApplicationEvent;

public class GetDocumentMetaDataEvent extends ApplicationEvent {
    public GetDocumentMetaDataEvent(Object source) {
        super(source);
    }
}
