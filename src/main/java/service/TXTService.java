package service;

import domain.ForbiddenZone;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Jakub Filipiak on 27.02.2019.
 */
public class TXTService {

    FileWriter fileWriter = null;

    public void initializeFileWriter() throws IOException {
        fileWriter = new FileWriter("D:/ForbiddenZonesGenerator " +
                "resources/ForbiddenZonesBig2.txt");
    }

    public void write(ForbiddenZone forbiddenZone) throws IOException {
        fileWriter.write(forbiddenZone.getEntranceTime() + " " + forbiddenZone.getDepartureTime());
        //fileWriter.write('\n');
        fileWriter.write(System.getProperty("line.separator"));
    }

    public void closeFileWriter() throws IOException {
        if (fileWriter != null) {
            fileWriter.close();
        }
    }
}
