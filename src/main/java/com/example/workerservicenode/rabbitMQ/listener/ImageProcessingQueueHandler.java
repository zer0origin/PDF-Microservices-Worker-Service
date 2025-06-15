package com.example.workerservicenode.rabbitMQ.listener;

import com.example.workerservicenode.spring.event.ProcessImageEvent;
import network.queue.request.ImageQueueRequest;
import network.queue.response.ImageQueueResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@RabbitListener(queues = "imageProcessingQueue", containerFactory = "prefetchRabbitListenerContainerFactory")
@Component
public class ImageProcessingQueueHandler {
    private static final Logger logger = LoggerFactory.getLogger(DocumentExtractionQueueHandler.class);

    private final ApplicationEventPublisher applicationEventPublisher;

    public ImageProcessingQueueHandler(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @RabbitHandler
    public ImageQueueResponse handle(ImageQueueRequest msg) {
        System.out.println("Received request.");
        logger.trace("Received imageProcessingQueue request");
        ProcessImageEvent e = new ProcessImageEvent(this, msg);
        applicationEventPublisher.publishEvent(e); //Fire the image processing event.

        logger.trace("Finished executing imageProcessingQueue, sending response");
        //Create the response object to sent back to the client.
        ImageQueueResponse res = new ImageQueueResponse(msg);
        res.setImageEncodedArr(e.getProcessedImages());
        return res;
    }
}
