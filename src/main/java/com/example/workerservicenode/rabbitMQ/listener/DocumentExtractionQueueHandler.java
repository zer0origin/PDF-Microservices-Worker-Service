package com.example.workerservicenode.rabbitMQ.listener;

import com.example.workerservicenode.spring.event.ExtractionEvent;
import network.queue.request.ExtractionQueueRequest;
import network.queue.response.ExtractionQueueResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@RabbitListener(queues = "documentProcessingQueue", containerFactory = "prefetchRabbitListenerContainerFactory")
@Component
public class DocumentExtractionQueueHandler {
    private static final Logger logger = LoggerFactory.getLogger(DocumentExtractionQueueHandler.class);

    private final ApplicationEventPublisher applicationEventPublisher;

    public DocumentExtractionQueueHandler(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @RabbitHandler
    public ExtractionQueueResponse handleRabbitMQMessage(ExtractionQueueRequest msg) {
        String str = "Received RabbitMQ message: " + msg.getJobUUID() + "Document Size: " + msg.getDocument().getPdfBase64Document().length();
        applicationEventPublisher.publishEvent(new ExtractionEvent(this, msg));
        logger.info(str);
        return new ExtractionQueueResponse(msg);
    }
}
