package com.example.workerservicenode.spring.event;

import network.queue.request.ImageQueueRequest;
import org.springframework.context.ApplicationEvent;

public class ProcessImageEvent extends ApplicationEvent {
    private final ImageQueueRequest imageQueueRequest;
    private String[] processedImages;

    public ProcessImageEvent(Object source, ImageQueueRequest imageQueueRequest) {
        super(source);
        this.imageQueueRequest = imageQueueRequest;
    }

    public ImageQueueRequest getImageQueueRequest() {
        return imageQueueRequest;
    }

    public String[] getProcessedImages() {
        return processedImages;
    }

    public void setProcessedImages(String[] processedImages) {
        this.processedImages = processedImages;
    }
}
