package com.example.workerservicenode.listener.rabbitmq;

import com.example.workerservicenode.event.StartExtractionEvent;
import com.rabbitmq.client.Channel;
import dto.DocumentQueueEntity;
import dto.response.DocumentResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@RabbitListener(queues = "workerQueue", containerFactory = "prefetchRabbitListenerContainerFactory")
@Component
public class JobWorkerQueueSubmittedHandler {
    private static final Logger logger = LoggerFactory.getLogger(JobWorkerQueueSubmittedHandler.class);

    private ApplicationEventPublisher applicationEventPublisher;

    public JobWorkerQueueSubmittedHandler(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @RabbitHandler
    public DocumentResponseEntity handleRabbitMQMessage(DocumentQueueEntity message) {
        String str = "Received RabbitMQ message: " + message.getJobUUID() + "Document Size: " + message.getPdfBase64Document().length();
        applicationEventPublisher.publishEvent(new StartExtractionEvent(this, message));
        logger.info(str);
        return new DocumentResponseEntity(message.getPageUUID(), message.getJobUUID(), message);
    }
}
