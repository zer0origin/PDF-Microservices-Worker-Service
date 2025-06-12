package com.example.workerservicenode.rabbitMQ.listener;

import network.DocumentMetaRequest;
import dto.Image;
import network.DocumentMetaResponse;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RabbitListener(queues = "documentMetaQueue", containerFactory = "prefetchRabbitListenerContainerFactory")
@Component
public class OnGetDocumentMetaQueueHandler {
    @RabbitHandler
    public DocumentMetaResponse handleRabbitMQMessage(DocumentMetaRequest msg) {
        float height, width;
        int noOfPages;

        //TODO: Get the meta data from the string.
        try (PDDocument document = Loader.loadPDF(msg.getBase64Document().getBytes())) {
            noOfPages = document.getNumberOfPages();
            PDPage documentPage = document.getPage(0);

            //Get the bounding box, MediaBox defines the boundaries of the physical medium and is required by the PDF spec.
            PDRectangle rec = documentPage.getMediaBox();
            height = rec.getHeight();
            width = rec.getWidth();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new DocumentMetaResponse(new Image(height, width, noOfPages), msg);
    }
}
