package com.example.workerservicenode.extraction;

import com.example.workerservicenode.config.RabbitMQ;
import dto.Selection;
import dto.Word;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PDFPageTextExtractor extends PDFTextStripper {
    private final List<List<Word>> selectionWordArr = new ArrayList<>();
    private List<Selection> pageSelections = new ArrayList<>();

    public PDFPageTextExtractor(int page) throws IOException {
        super.setSortByPosition(true);

        super.setStartPage(page);
        super.setEndPage(page);
    }

    @Override
    protected void writeString(String string, List<TextPosition> textPositions) throws IOException {
        if (!pageSelections.isEmpty()) {
            extractTextRegions(string, textPositions);
        }
    }

    private void extractTextRegions(String string, List<TextPosition> textPositions) {
        for (Selection s : pageSelections) { // as of right now, we don't care about the selection method, that is used for building the JSON / CSV and not for deciding how to select.
            float x1 = s.getX1();
            float x2 = s.getX2();
            float y1 = s.getY1();
            float y2 = s.getY2();

            Word word = convertTextRegionToWord(string, textPositions);
            float x = word.getX();
            float y = word.getY();

            boolean withinX = x > x1 && x < x2;
            boolean withinY = y > y1 && y < y2;

            //TODO: make a class that handles configuration of selections and other objects.
            if (withinX && withinY) {
                int stepSize;
                if (s.getStepSize() <= 0){ //Disabled
                    stepSize = RabbitMQ.STEP_SIZE_FOR_EXTRACTION;
                }else{
                    stepSize = s.getStepSize();
                }

                if (s.overrideLineAssignment()){
                    if(s.useXForLines()){
                        s.addWord(word, (int) word.getX(), stepSize);
                    }else{
                        s.addWord(word, (int) word.getY(), stepSize);
                    }
                    continue;
                }

                if(RabbitMQ.USE_X_FOR_LINES == 1){
                    s.addWord(word, (int) word.getX(), stepSize);
                }else{
                    s.addWord(word, (int) word.getY(), stepSize);
                }
            }
        }
    }

    private Word convertTextRegionToWord(String string, List<TextPosition> textPositions) {
        Word word = new Word();
        word.setWord(string);
        word.setX(textPositions.get(0).getX());
        word.setY(textPositions.get(0).getY());

        return word;
    }

    public List<Selection> getPageSelections() {
        return pageSelections;
    }

    public void setPageSelections(List<Selection> pageSelections) {
        this.pageSelections = pageSelections;
    }

    public List<List<Word>> getSelectionWordArr() {
        return selectionWordArr;
    }
}
