package service;

import domain.*;

import java.io.*;
import java.time.LocalTime;
import java.util.List;

/**
 * Created by Jakub Filipiak on 25.02.2019.
 */
public enum TrackService {

    INSTANCE;

    private Track track = Track.INSTANCE;
    private MapService mapService = MapService.INSTANCE;

    private ForbiddenZones forbiddenZones = ForbiddenZones.INSTANCE;
    private ForbiddenZonesService forbiddenZonesService = ForbiddenZonesService.INSTANCE;
    private Properties properties = Properties.INSTANCE;

    private int numberOfForbiddenPoints;
    private int numberOfForbiddenAnglePoints;
    private int numberOfBreakpoints;
    private ForbiddenZone forbiddenZone;
    private ForbiddenZone forbiddenAngleZone;
    private boolean forbiddenZoneCreated = false;
    private boolean forbiddenAngleZoneCreated = false;
    private LocalTime time;
    private List<ForbiddenZone> forbiddenByColors = forbiddenZones.getForbiddenByColors();
    private List<ForbiddenZone> forbiddenByAngles = forbiddenZones.getForbiddenByAngles();
    private List<ForbiddenZone> forbiddenByDrop = forbiddenZones.getForbiddenByDrop();
    private double entranceTimeAngle;
    private double departureTimeAngle;


    private boolean startTimeSaved = false;



    public void processFile() throws IOException {

        TextService textService = TextService.INSTANCE;



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

                if (!startTimeSaved) {
                    forbiddenByDrop.add(new ForbiddenZone(time, properties.getDropStartTime()));
                    startTimeSaved = true;
                }
                PointOfTrack pointOfTrack = new PointOfTrack(latitude, longitude,
                        time);
                //System.out.println(pointOfTrack);
                LocalTime tmpForbiddenPoint = mapService.analyzePoint(pointOfTrack);
                ForbiddenAnglePoint tmpForbiddenAnglePoint =
                        mapService.analyzeAngle(pointOfTrack);

                createForbiddenZoneBeta(tmpForbiddenPoint,
                        tmpForbiddenAnglePoint);

            }
        }
        forbiddenByDrop.add(new ForbiddenZone(properties.getDropStopTime(), time));
        List<ForbiddenZone> finalList = forbiddenZonesService.mergeLists();
        textService.writeText("Poniżej listy scalone i posegregowane:");
        textService.writeList(finalList);
        textService.closeFileWriter();
    }

    private void createForbiddenZoneBeta(LocalTime tmpForbiddenTime,
                                         ForbiddenAnglePoint tmpForbiddenAnglePoint) throws IOException {

        int timeBuffer = properties.getTimeBuffer();
        boolean dropOnTurns = properties.isDropOnTurns();
        int ignoredTurnsMinValue = properties.getIgnoredTurnsMinValue();
        int ignoredTurnsMaxValue = properties.getIgnoredTurnsMaxValue();



        if (tmpForbiddenTime != null) {
            if (!forbiddenZoneCreated) {
                //tmpEntranceTime = tmpForbiddenPoint;
                forbiddenZone = new ForbiddenZone(tmpForbiddenTime.minusSeconds(timeBuffer));
                forbiddenZoneCreated = true;
                numberOfForbiddenPoints = 1;
            } else {
                //tmpDepartureTime = tmpForbiddenPoint;
                forbiddenZone.setDepartureTime(tmpForbiddenTime);
                numberOfForbiddenPoints++;
            }
        } else {
            if (numberOfForbiddenPoints == 1) {
                forbiddenZone.setDepartureTime(forbiddenZone.getEntranceTime().plusSeconds(2 * timeBuffer));
                forbiddenByColors.add(forbiddenZone);
                System.out.println("Forbidden zone written: " + forbiddenZone.toString());
                forbiddenZoneCreated = false;
                numberOfForbiddenPoints = 0;
            } else if (numberOfForbiddenPoints > 1) {
                forbiddenZone.setDepartureTime(forbiddenZone.getDepartureTime().plusSeconds(timeBuffer));
                forbiddenByColors.add(forbiddenZone);
                System.out.println("Forbidden zone written: " + forbiddenZone.toString());
                forbiddenZoneCreated = false;
                numberOfForbiddenPoints = 0;
            } else {
                forbiddenZone = null;
                forbiddenZoneCreated = false;
                numberOfForbiddenPoints = 0;
            }
        }

        // part od angle analyzing

        if (tmpForbiddenAnglePoint != null) {
            if (!forbiddenAngleZoneCreated) {
                //tmpEntranceTime = tmpForbiddenPoint;
                forbiddenAngleZone =
                        new ForbiddenZone(tmpForbiddenAnglePoint.getForbiddenAngleTime().minusSeconds(timeBuffer));
                forbiddenAngleZoneCreated = true;
                numberOfForbiddenAnglePoints = 1;
                double y1 =
                        tmpForbiddenAnglePoint.getEntranceLinePointA().getLatitude();
                double y2 =
                        tmpForbiddenAnglePoint.getEntranceLinePointB().getLatitude();
                double x1 =
                        tmpForbiddenAnglePoint.getEntranceLinePointA().getLongitude();
                double x2 =
                        tmpForbiddenAnglePoint.getEntranceLinePointB().getLongitude();
                entranceTimeAngle = Math.atan2(y1 - y2, x1 - x2);
            } else if (numberOfBreakpoints <= 2){
                //tmpDepartureTime = tmpForbiddenPoint;
                forbiddenAngleZone.setDepartureTime(tmpForbiddenAnglePoint.getForbiddenAngleTime());
                numberOfForbiddenAnglePoints++;
                numberOfBreakpoints = 0;
                double y1 =
                        tmpForbiddenAnglePoint.getDepartureLinePointA().getLatitude();
                double y2 =
                        tmpForbiddenAnglePoint.getDepartureLinePointB().getLatitude();
                double x1 =
                        tmpForbiddenAnglePoint.getDepartureLinePointA().getLongitude();
                double x2 =
                        tmpForbiddenAnglePoint.getDepartureLinePointB().getLongitude();
                departureTimeAngle = Math.atan2(y1 - y2, x1 - x2);
            }
        } else {
            if (forbiddenAngleZoneCreated) {
                numberOfBreakpoints++;
                if (numberOfBreakpoints > 2) {
                    forbiddenAngleZoneCreated = false;
                    numberOfBreakpoints = 0;
                    if (numberOfForbiddenAnglePoints >= 2) {
                        forbiddenAngleZone.setDepartureTime(forbiddenAngleZone.getDepartureTime().plusSeconds(timeBuffer));
                        double turnAngle = Math.abs(Math.abs(entranceTimeAngle) - Math.abs(departureTimeAngle));
                        turnAngle = Math.toDegrees(turnAngle);

                        if (!dropOnTurns) {
                            forbiddenByAngles.add(forbiddenAngleZone);
                            System.out.println("Forbidden angle zone written: " + forbiddenAngleZone);
                            System.out.println("Turn angle: " + turnAngle);
                            numberOfForbiddenAnglePoints = 0;
                        } else {
                            if (turnAngle < ignoredTurnsMinValue || turnAngle > ignoredTurnsMaxValue) {
                                forbiddenByAngles.add(forbiddenAngleZone);
                                System.out.println("Forbidden angle zone written: " + forbiddenAngleZone);
                                System.out.println("Turn angle: " + turnAngle);
                                numberOfForbiddenAnglePoints = 0;
                            } else {
                                forbiddenAngleZone = null;
                                numberOfForbiddenAnglePoints = 0;
                            }

                        }

                    } else {
                        forbiddenAngleZone = null;
                        numberOfForbiddenAnglePoints = 0;
                    }
                }
            } else {
                forbiddenAngleZone = null;
                forbiddenAngleZoneCreated = false;
                numberOfForbiddenAnglePoints = 0;
            }
        }
    }
}
