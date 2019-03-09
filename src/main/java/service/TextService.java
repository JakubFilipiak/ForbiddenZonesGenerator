package service;

import domain.ForbiddenZone;
import domain.Text;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Jakub Filipiak on 27.02.2019.
 */
public enum TextService {

    INSTANCE;

    private Text text = Text.INSTANCE;

    private FileWriter fileWriter = null;

    public void initializeFileWriter() throws IOException {
        fileWriter = new FileWriter(text.getTextFile());
    }

    public void write(ForbiddenZone forbiddenZone) throws IOException {
        fileWriter.write(forbiddenZone.getEntranceTime() + " " + forbiddenZone.getDepartureTime());
        fileWriter.write(System.getProperty("line.separator"));
    }

    public void closeFileWriter() throws IOException {
        if (fileWriter != null) {
            fileWriter.close();
        }
    }
}
