package com.example.workerservicenode.listener.rabbitmq;

import com.example.workerservicenode.event.StartExtractionEvent;
import dto.DocumentQueueEntity;
import dto.response.SelectionResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@RabbitListener(queues = "documentProcessingQueue", containerFactory = "prefetchRabbitListenerContainerFactory")
@Component
public class JobDocumentQueueSubmittedHandler {
    private static final Logger logger = LoggerFactory.getLogger(JobDocumentQueueSubmittedHandler.class);

    private final ApplicationEventPublisher applicationEventPublisher;

    public JobDocumentQueueSubmittedHandler(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @RabbitHandler
    public SelectionResponseEntity handleRabbitMQMessage(DocumentQueueEntity msg) {
        String str = "Received RabbitMQ message: " + msg.getJobUUID() + "Document Size: " + msg.getDocument().getPdfBase64Document().length();
        applicationEventPublisher.publishEvent(new StartExtractionEvent(this, msg));
        logger.info(str);
        return new SelectionResponseEntity(msg);
    }
}
