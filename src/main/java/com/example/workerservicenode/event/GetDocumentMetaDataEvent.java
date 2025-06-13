package com.example.workerservicenode.event;

import org.springframework.context.ApplicationEvent;

public class GetDocumentMetaDataEvent extends ApplicationEvent {
    public GetDocumentMetaDataEvent(Object source) {
        super(source);
    }
}
