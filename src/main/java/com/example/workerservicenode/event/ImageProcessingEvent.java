package com.example.workerservicenode.event;

import com.willcocks.callum.model.ImageRequest;
import org.springframework.context.ApplicationEvent;

public class ImageProcessingEvent extends ApplicationEvent {
    private final ImageRequest imageRequest;
    private String[] processedImages;

    public ImageProcessingEvent(Object source, ImageRequest imageRequest) {
        super(source);
        this.imageRequest = imageRequest;
    }

    public ImageRequest getImageRequest() {
        return imageRequest;
    }

    public String[] getProcessedImages() {
        return processedImages;
    }

    public void setProcessedImages(String[] processedImages) {
        this.processedImages = processedImages;
    }
}
