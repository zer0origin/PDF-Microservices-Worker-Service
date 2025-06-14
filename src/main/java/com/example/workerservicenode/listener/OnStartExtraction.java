package com.example.workerservicenode.listener;

import com.example.workerservicenode.spring.event.ExtractionEvent;
import com.example.workerservicenode.extraction.PDFPageTextExtractor;
import com.example.workerservicenode.rabbitMQ.listener.DocumentExtractionQueueHandler;
import dto.Selection;
import network.queue.request.ExtractionQueueRequest;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.*;

@Component
public class OnStartExtraction {
    private static final Logger logger = LoggerFactory.getLogger(DocumentExtractionQueueHandler.class);

    @EventListener
    public void handle(ExtractionEvent event) {
        ExtractionQueueRequest message = event.getEntity();

        try {
            extractWords(message.getDocument().getPdfBase64Document(), message.getDocument().getSelectionMap());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void extractWords(String base64PDF, Map<Integer, List<Selection>> AllSelections) throws IOException {
        byte[] decodedDocument = Base64.getDecoder().decode(base64PDF);

        try (PDDocument document = Loader.loadPDF(decodedDocument)) {
            for (Integer page : AllSelections.keySet()) {
                List<Selection> SelectionsForPage = AllSelections.get(page);

                if (SelectionsForPage != null) {
                    PDFPageTextExtractor wordExtractor = new PDFPageTextExtractor(page);
                    wordExtractor.setPageSelections(SelectionsForPage); //Indexes Start at 0 and not 1.
                    wordExtractor.getText(document);
                }
            }
        }
    }
}
