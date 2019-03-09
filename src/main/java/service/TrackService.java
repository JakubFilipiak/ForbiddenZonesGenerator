package service;

import domain.ForbiddenZone;
import domain.PointOfTrack;
import domain.Track;

import java.io.*;
import java.time.LocalTime;

/**
 * Created by Jakub Filipiak on 25.02.2019.
 */
public enum TrackService {

    INSTANCE;

    Track track = Track.INSTANCE;
    MapService mapService = MapService.INSTANCE;
    TextService textService = TextService.INSTANCE;

    LocalTime tmpEntranceTime;
    LocalTime tmpDepartureTime;
    int numberOfForbiddenPoints;
    LocalTime tmpTime;
    ForbiddenZone forbiddenZone;
    boolean forbiddenZoneCreated = false;
    LocalTime time;

    public void processFile() throws IOException {

        InputStream bufferedInputStream =
                new BufferedInputStream(new FileInputStream(track.getTrackFile()));
        BufferedReader bufferedReader =
                new BufferedReader(new InputStreamReader(bufferedInputStream));

        String line;


        textService.initializeFileWriter();

        for (int i = 1; i < 1000; i ++) {
            while ((line = bufferedReader.readLine()) != null && line.startsWith(
                    "T")) {

                float latitude = 0f;
                float longitude = 0f;



                int index = 0;
                String fields[] = line.split(" ");
                for (String field : fields) {

                    switch (index) {
                        case 2:
                            latitude = Float.parseFloat(field.substring(1));
                            break;
                        case 3:
                            longitude = Float.parseFloat(field.substring(2));
                            break;
                        case 5:
                            time = LocalTime.parse(field);
                            break;
                        default:
                            break;
                    }
                    index++;
                }
                PointOfTrack pointOfTrack = new PointOfTrack(latitude, longitude,
                        time);
                //System.out.println(pointOfTrack);
                tmpTime = mapService.analyzePoint(pointOfTrack);
                //Algorithm:

                createForbiddenZoneBeta();
            }
        }
        textService.closeFileWriter();
    }

    private void createForbiddenZoneBeta() throws IOException {

        if (tmpTime != null) {
            if (!forbiddenZoneCreated) {
                //tmpEntranceTime = tmpTime;
                forbiddenZone = new ForbiddenZone(tmpTime.minusSeconds(3));
                forbiddenZoneCreated = true;
                numberOfForbiddenPoints = 1;
            } else {
                //tmpDepartureTime = tmpTime;
                forbiddenZone.setDepartureTime(tmpTime);
                numberOfForbiddenPoints++;
            }
        } else {
            if (numberOfForbiddenPoints == 1) {
                forbiddenZone.setDepartureTime(forbiddenZone.getEntranceTime().plusSeconds(6));
                textService.write(forbiddenZone);
                System.out.println("Forbidden point written: " + forbiddenZone.toString());
                forbiddenZoneCreated = false;
                numberOfForbiddenPoints = 0;
            } else if (numberOfForbiddenPoints > 1) {
                forbiddenZone.setDepartureTime(forbiddenZone.getDepartureTime().plusSeconds(3));
                textService.write(forbiddenZone);
                System.out.println("Forbidden point written: " + forbiddenZone.toString());
                forbiddenZoneCreated = false;
                numberOfForbiddenPoints = 0;
            } else {
                forbiddenZone = null;
                forbiddenZoneCreated = false;
                numberOfForbiddenPoints = 0;
            }
        }
    }
}
