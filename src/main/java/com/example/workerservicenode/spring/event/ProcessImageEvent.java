package com.example.workerservicenode.spring.event;

import network.queue.request.ImageQueueRequest;
import org.springframework.context.ApplicationEvent;

import java.util.List;

public class ProcessImageEvent extends ApplicationEvent {
    private final ImageQueueRequest imageQueueRequest;
    private List<String> processedImages;

    public ProcessImageEvent(Object source, ImageQueueRequest imageQueueRequest) {
        super(source);
        this.imageQueueRequest = imageQueueRequest;
    }

    public ImageQueueRequest getImageQueueRequest() {
        return imageQueueRequest;
    }

    public List<String> getProcessedImages() {
        return processedImages;
    }

    public void setProcessedImages(List<String> processedImages) {
        this.processedImages = processedImages;
    }
}
