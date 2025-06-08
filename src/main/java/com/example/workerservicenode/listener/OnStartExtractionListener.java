package com.example.workerservicenode.listener;

import com.example.workerservicenode.event.StartExtractionEvent;
import com.example.workerservicenode.extraction.PDFPageTextExtractor;
import com.example.workerservicenode.listener.rabbitmq.JobDocumentQueueSubmittedHandler;
import com.willcocks.callum.model.data.Selection;
import dto.DocumentQueueEntity;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class OnStartExtractionListener {
    private static final Logger logger = LoggerFactory.getLogger(JobDocumentQueueSubmittedHandler.class);

    @EventListener
    public void handle(StartExtractionEvent event) {
        DocumentQueueEntity message = event.getEntity();

        try {
            extractWords(message.getDocument().getPdfBase64Document(), message.getDocument().getSelectionMap());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void extractWords(String base64PDF, Map<Integer, List<Selection>> AllSelections) throws IOException {
        byte[] decoded = Base64.getDecoder().decode(base64PDF);

        try (PDDocument document = Loader.loadPDF(decoded)) {
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
