package com.example.workerservicenode.rabbitMQ.listener;

import com.example.workerservicenode.spring.event.GenerateMetaEvent;
import network.queue.request.MetaQueueRequest;
import dto.Image;
import network.queue.response.DocumentMetaQueueResponse;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;

@RabbitListener(queues = "documentMetaQueue", containerFactory = "prefetchRabbitListenerContainerFactory")
@Component
public class MetaQueueHandler {
    private final ApplicationEventPublisher publisher;

    public MetaQueueHandler(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @RabbitHandler
    public DocumentMetaQueueResponse handleRabbitMQMessage(MetaQueueRequest msg) {
        GenerateMetaEvent e = new GenerateMetaEvent(msg);
        publisher.publishEvent(e);
        return new DocumentMetaQueueResponse(msg.getDocumentMetaQueueEntity().getDocumentUUID(), msg.getResponseManagerUUID(), e.getImage());
    }
}
