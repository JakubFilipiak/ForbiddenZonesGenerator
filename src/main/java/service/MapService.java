package service;

import domain.ForbiddenAnglePoint;
import domain.Map;
import domain.PointOfTrack;
import domain.Properties;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
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



    private BufferedImage mapImage;

    private PointOfTrack pointOne = null;
    private PointOfTrack pointTwo = null;
    private PointOfTrack pointThree = null;

    private double angle;


    {
        try {
            mapImage = ImageIO.read(map.getMapFileInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean checkMapColors() {

        int height = mapImage.getHeight();
        int width = mapImage.getWidth();
        int y;
        int  x;

        Set<Color> otherColorsSet = new HashSet<>();

        for (x = 0; x < width; x++) {
            for (y = 0; y < height; y++) {

                Color tmpColor = new Color(mapImage.getRGB(x, y));

                if (tmpColor.getRGB() != map.getAllowedColor().getRGB() && tmpColor.getRGB() != map.getForbiddenColor().getRGB()) {
                    otherColorsSet.add(tmpColor);
                }
            }
        }
        if (otherColorsSet.isEmpty()) {
            System.out.println("Correct colors detected!");
            return true;
        } else {
            System.out.println("Incorrect color detected! " + otherColorsSet.toString());
            return false;
        }
    }

    public LocalTime analyzePoint(PointOfTrack pointOfTrack) {

        float relativeLongitudeZero = map.getRelativeLongitudeZero();
        float relativeLatitudeZero = map.getRelativeLatitudeZero();

        float relativeX = pointOfTrack.getLongitude() - relativeLongitudeZero;
        System.out.println(relativeX);

        float relativeY =  relativeLatitudeZero - pointOfTrack.getLatitude();
        System.out.println(relativeY);

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
}
