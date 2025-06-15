package com.example.workerservicenode.listener;
import com.example.workerservicenode.spring.event.ProcessImageEvent;
import network.queue.request.ImageQueueRequest;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
public class OnStartImageProcessing {
    private Logger logger = LoggerFactory.getLogger(OnStartImageProcessing.class);
    @EventListener
    public void handle(ProcessImageEvent e){ //TODO: render specific pages, NOT ALL OF THEM!
        ImageQueueRequest msg = e.getImageQueueRequest();
        byte[] decoded = Base64.getDecoder().decode(msg.getPayload().getBase64Document());

        logger.info("Loading document");
      
        try (PDDocument document = Loader.loadPDF(decoded)) {
            int pageNo = document.getNumberOfPages();

            PDFRenderer pdfRenderer = new PDFRenderer(document);
            List<String> encodedArr = new ArrayList<>(pageNo);
            for (int i = msg.getStart(); i < pageNo && i < msg.getEnd(); i++) {
                logger.trace("Rendering page: " + i);
                System.out.println("Rendering page: " + i);
                BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(i, 300); // 300 DPI
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "png", outputStream);

                byte[] imageBytes = outputStream.toByteArray();
                String image = Base64.getEncoder().encodeToString(imageBytes);
                encodedArr.add(image);
                logger.trace("Finished page: " + i);
            }

            e.setProcessedImages(encodedArr);
    } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
