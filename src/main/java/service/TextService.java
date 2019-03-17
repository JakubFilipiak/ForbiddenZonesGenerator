package service;

import domain.ForbiddenZone;
import domain.Text;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by Jakub Filipiak on 27.02.2019.
 */
public enum TextService {

    INSTANCE;


    Text text = Text.INSTANCE;
    private FileWriter fileWriter;

    public void initializeFileWriter() throws IOException {

        fileWriter = new FileWriter(text.getTextFile());
    }

//    public void write(ForbiddenZone forbiddenZone) throws IOException {
//        fileWriter.write(forbiddenZone.getEntranceTime() + " " + forbiddenZone.getDepartureTime());
//        fileWriter.write(System.getProperty("line.separator"));
//    }

    public void writeList(List<ForbiddenZone> mergedList) throws IOException {
        for (ForbiddenZone tmpForbiddenZone : mergedList) {
            fileWriter.write(tmpForbiddenZone.getEntranceTime() + " " + tmpForbiddenZone.getDepartureTime());
            fileWriter.write(System.getProperty("line.separator"));
        }

    }

    public void writeText(String text) throws IOException {
        fileWriter.write(System.getProperty("line.separator"));
        fileWriter.write(text);
        fileWriter.write(System.getProperty("line.separator"));
    }

    public void closeFileWriter() throws IOException {
        if (fileWriter != null) {
            fileWriter.close();
        }
    }
}
