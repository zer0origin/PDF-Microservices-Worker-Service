package com.example.workerservicenode.spring.event;

import dto.Image;
import network.queue.request.MetaQueueRequest;

public class GenerateMetaEvent {
    private final MetaQueueRequest msg;
    private Image image;

    public GenerateMetaEvent(MetaQueueRequest msg) {
        this.msg = msg;
    }

    public MetaQueueRequest getMsg() {
        return msg;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
