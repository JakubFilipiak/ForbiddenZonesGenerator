package service;

import domain.Map;
import domain.PointOfTrack;

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
    float relativeLongitudeZero = map.getRelativeLongitudeZero();
    float relativeLatitudeZero = map.getRelativeLatitudeZero();

    private BufferedImage mapImage;

    {
        try {
            mapImage = ImageIO.read(map.getMapFile());
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

        if (pixelColor.getRGB() == map.getForbiddenColor().getRGB()) {
            System.out.println(pointOfTrack.getTime());
            return pointOfTrack.getTime();
        } else {
            System.out.println("...");
            return null;
        }
    }
}
