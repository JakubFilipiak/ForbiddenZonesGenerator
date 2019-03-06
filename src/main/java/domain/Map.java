package domain;

import java.awt.*;

/**
 * Created by Jakub Filipiak on 27.02.2019.
 */
public class Map {

//    Color colorGreen = new Color(0, 130, 0);
    Color colorGreen = new Color(25, 144, 25);
    Color colorBlack = new Color(0, 0, 0);

//    private float bottomLeftCornerLatitude = 50.636667f;
//    private float bottomLeftCornerLongitude = 21.850556f;

//    private float bottomLeftCornerLatitude = 49.656688f;
//    private float bottomLeftCornerLongitude = 20.834437f;

    private float bottomLeftCornerLatitude = 49.656450f;
    private float bottomLeftCornerLongitude = 20.834433f;



//    private float upperRightCornerLatitude = 51.926944f;
//    private float upperRightCornerLongitude = 23.907222f;

//    private float upperRightCornerLatitude = 52.503125f;
//    private float upperRightCornerLongitude = 24.396766f;

    private float upperRightCornerLatitude = 52.503133f;
    private float upperRightCornerLongitude = 24.397150f;




    private float longitudeResolution = upperRightCornerLongitude - bottomLeftCornerLongitude;
    private float latitudeResolution = upperRightCornerLatitude - bottomLeftCornerLatitude;

    public Color getColorGreen() {
        return colorGreen;
    }

    public Color getColorBlack() {
        return colorBlack;
    }

    public float getUpperRightCornerLatitude() {
        return upperRightCornerLatitude;
    }

    public float getBottomLeftCornerLongitude() {
        return bottomLeftCornerLongitude;
    }

    public float getLongitudeResolution() {
        return longitudeResolution;
    }

    public float getLatitudeResolution() {
        return latitudeResolution;
    }
}
