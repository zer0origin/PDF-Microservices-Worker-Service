package com.example.workerservicenode.listener.rabbitmq;

import dto.ImageRequest;
import dto.response.ImageResponse;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.apache.pdfbox.rendering.PDFRenderer;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@RabbitListener(queues = "imageReplyQueue", containerFactory = "prefetchRabbitListenerContainerFactory")
@Component
public class JobImageProcessingQueueHandler {
    private static final Logger logger = LoggerFactory.getLogger(JobDocumentQueueSubmittedHandler.class);

    private final ApplicationEventPublisher applicationEventPublisher;

    public JobImageProcessingQueueHandler(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @RabbitHandler
    public ImageResponse handle(ImageRequest msg){
        //TODO: CONVERT IMAGEREQUEST TO IMAGE.

        byte[] decoded = Base64.getDecoder().decode(msg.getBase64Document());

        try (PDDocument document = Loader.loadPDF(decoded)) {
            int pageNo = document.getNumberOfPages();

            PDFRenderer pdfRenderer = new PDFRenderer(document);
            String[] encodedArr = new String[pageNo];
            for (int i = 0; i < pageNo; i++) {
                BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(i, 300); // 300 DPI
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "png", outputStream);
                byte[] imageBytes = outputStream.toByteArray();

                String encodedBase64 = Base64.getEncoder().encodeToString(imageBytes);

                encodedArr[i] = encodedBase64;
            }

            ImageResponse res = new ImageResponse(msg);
            res.setImageEncodedArr(encodedArr);
            return res;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
