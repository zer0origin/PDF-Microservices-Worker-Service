package com.example.workerservicenode.listener;

import com.example.workerservicenode.event.ImageProcessingEvent;
import com.willcocks.callum.model.ImageRequest;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Component
public class OnStartImageProcessing {
    public void handle(ImageProcessingEvent e){
        ImageRequest msg = e.getImageRequest();

        if (msg.getCallbackService() == null || msg.getCallbackService().isEmpty()){
            throw new IllegalStateException("CallbackService string needs to be provided.");
        }

        if (msg.getCallbackURL() == null || msg.getCallbackURL().isEmpty()){
            throw new IllegalStateException("CallbackService string needs to be provided.");
        }

        //TODO: CONVERT IMAGE-REQUEST TO IMAGE.
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

            e.setProcessedImages(encodedArr);
    } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
