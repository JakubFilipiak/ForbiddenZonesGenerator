package service;

import domain.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jakub Filipiak on 25.02.2019.
 */
public enum MapService {

    INSTANCE;

    private Map map = Map.INSTANCE;
    private Properties properties = Properties.INSTANCE;

    private PointOfTrack pointOne = null;
    private PointOfTrack pointTwo = null;
    private PointOfTrack pointThree = null;

    private double angle;

    private BufferedImage mapImage;

    private InputStream mapInputStream;

//    {
//        try {
//            mapImage = ImageIO.read(map.getMapFile());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }



    public boolean checkMapColors() throws IOException {

//        Map map = Map.INSTANCE;

        mapImage = map.getMapImage();
        System.out.println(mapImage);

        int height = mapImage.getHeight();
        int width = mapImage.getWidth();
        int y;
        int  x;

        Set<Color> otherColorsSet = new HashSet<>();
        Set<Color> colorSet = new HashSet<>();
        Set<Coordinates> coordinatesSet = new HashSet<>();


        for (x = 0; x < width; x++) {
            for (y = 0; y < height; y++) {

                Color tmpColor = new Color(mapImage.getRGB(x, y));

                if (tmpColor.getRGB() != map.getAllowedColor().getRGB() && tmpColor.getRGB() != map.getForbiddenColor().getRGB()) {
                    otherColorsSet.add(tmpColor);
                    coordinatesSet.add(new Coordinates(x, y));
                } else {
                    colorSet.add(tmpColor);
                }
            }
        }
        if (otherColorsSet.isEmpty()) {
            System.out.println("Correct colors detected!");
            System.out.println(colorSet);
            return true;
        } else {
            System.out.println("Incorrect color detected! " + otherColorsSet.toString());
            System.out.println("Coordinates: " + coordinatesSet);
            return false;
        }
    }

    public LocalTime analyzePoint(PointOfTrack pointOfTrack) throws IOException {

//        Map map = Map.INSTANCE;

        System.out.println(mapImage);

        System.out.println("Coordinates:");
        System.out.println(map.getBottomLeftCornerLatitude() + ", " + map.getBottomLeftCornerLongitude());
        System.out.println(map.getUpperRightCornerLatitude() + ", " + map.getUpperRightCornerLongitude());

        float relativeLongitudeZero = map.getRelativeLongitudeZero();
        float relativeLatitudeZero = map.getRelativeLatitudeZero();

        float relativeX = pointOfTrack.getLongitude() - map.getRelativeLongitudeZero();
        System.out.println(relativeX);

        float relativeY =  map.getRelativeLatitudeZero() - pointOfTrack.getLatitude();
        System.out.println(relativeY);

        System.out.println(mapImage);
        System.out.println(mapImage.getWidth());

        float pixelX =
                relativeX / map.getLongitudeResolution() * mapImage.getWidth() - 1;
        float pixelY =
                relativeY / map.getLatitudeResolution() * mapImage.getHeight() - 1;

        System.out.println(pixelX + ", " + pixelY);

        Color pixelColor = new Color(mapImage.getRGB((int)pixelX, (int)pixelY));

        if (pixelColor.getRGB() != map.getAllowedColor().getRGB()) {
            System.out.println(pointOfTrack.getTime());
            return pointOfTrack.getTime();
        } else {
            System.out.println("...");
            return null;
        }
    }

    public ForbiddenAnglePoint analyzeAngle(PointOfTrack newPointOfTrack) {

        int minimumAngle = properties.getMinimumAngle();

        pointOne = pointTwo;
        pointTwo = pointThree;
        pointThree = newPointOfTrack;

        if (pointOne != null) {
            angle = calculateAngle(pointOne, pointTwo, pointThree);
            if (angle > minimumAngle) {
                System.out.println("Angle: " + angle);
                System.out.println("Time: " + pointTwo.getTime());
                ForbiddenAnglePoint forbiddenAnglePoint =
                        new ForbiddenAnglePoint(pointOne, pointTwo, pointTwo,
                                pointThree, pointTwo.getTime());

                return forbiddenAnglePoint;
            }
        }
        return null;
    }


    private double calculateAngle(PointOfTrack pointOne, PointOfTrack pointTwo,
                                 PointOfTrack pointThree) {

        double x1 = pointOne.getLongitude();
        double y1 = pointOne.getLatitude();
        double x2 = pointTwo.getLongitude();
        double y2 = pointTwo.getLatitude();
        double x3 = pointThree.getLongitude();
        double y3 = pointThree.getLatitude();

        double angle1 = Math.atan2(y1 - y2, x1 - x2);
        double angle2 = Math.atan2(y2 - y3, x2 - x3);

        double angle = Math.abs(Math.abs(angle1) - Math.abs(angle2));
        angle = Math.toDegrees(angle);

        return angle;
    }

        public void setMapParameters(String version) throws IOException {

//            Map map = Map.INSTANCE;

        ClassLoader classLoader = getClass().getClassLoader();




        if (version.equals("v2")) {

//            File mapFile =
//                    new File(classLoader.getResource("LublinBig2.png").getFile());
            map.setMapFileInputStream(MapService.class.getResourceAsStream(
                    "/LublinBig2.png"));

            mapInputStream = map.getMapFileInputStream();

            mapImage = ImageIO.read(mapInputStream);
//            map.setMapFileInputStream(map.getV2MapFileInputStream());
//            System.out.println("New input stream: " + map.getMapFileInputStream());
//            BufferedImage mapImage = ImageIO.read(map.getMapFileInputStream());

            map.setBottomLeftCornerLatitude(map.getV2BottomLeftCornerLatitude());
            map.setBottomLeftCornerLongitude(map.getV2BottomLeftCornerLongitude());
            map.setUpperRightCornerLatitude(map.getV2UpperRightCornerLatitude());
            map.setUpperRightCornerLongitude(map.getV2UpperRightCornerLongitude());
        } else if (version.equals("v3")) {

//            File mapFile =
//                    new File(classLoader.getResource("LublinBig3.png").getFile());

            map.setMapFileInputStream(MapService.class.getResourceAsStream(
                    "/LublinBig3.png"));

            mapInputStream = map.getMapFileInputStream();

            mapImage = ImageIO.read(mapInputStream);
//            map.setMapFileInputStream(map.getV3MapFileInputStream());
//            System.out.println("New input stream: " + map.getMapFileInputStream());
//            BufferedImage mapImage = ImageIO.read(map.getMapFileInputStream());

            map.setBottomLeftCornerLatitude(map.getV3BottomLeftCornerLatitude());
            map.setBottomLeftCornerLongitude(map.getV3BottomLeftCornerLongitude());
            map.setUpperRightCornerLatitude(map.getV3UpperRightCornerLatitude());
            map.setUpperRightCornerLongitude(map.getV3UpperRightCornerLongitude());
        }
    }

    public void eraseVariables() {
        pointOne = null;
        pointTwo = null;
        pointThree = null;
    }
}
