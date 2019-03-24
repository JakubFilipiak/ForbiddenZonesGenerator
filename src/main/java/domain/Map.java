package domain;

import lombok.Getter;
import lombok.Setter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created by Jakub Filipiak on 27.02.2019.
 */
public enum Map {

    INSTANCE;

    //    ClassLoader classLoader = getClass().getClassLoader();
//    File defaultMapFile =
//            new File(classLoader.getResource("LublinBig2.png").getFile());

    @Getter @Setter private File mapFile;


    public BufferedImage getMapImage() throws IOException {
        BufferedImage mapImage = ImageIO.read(getMapFile());
        return mapImage;
    }

//    @Getter private InputStream v2MapFileInputStream = Map.class.getResourceAsStream(
//            "/LublinBig2.png");
//    @Getter private InputStream v3MapFileInputStream =
//            Map.class.getResourceAsStream("/LublinBig3.png");
//
    @Getter @Setter private InputStream mapFileInputStream;

    private Color defaultAllowedColor = new Color(25, 144, 25);;
    private Color defaultForbiddenColor = new Color(0, 0, 0);

    @Getter private Color allowedColor = defaultAllowedColor;
    @Getter private Color forbiddenColor = defaultForbiddenColor;

    //V2
    @Getter private float v2BottomLeftCornerLatitude = 49.656450f;
    @Getter private float v2BottomLeftCornerLongitude = 20.834433f;

    @Getter private float v2UpperRightCornerLatitude = 52.503133f;
    @Getter private float v2UpperRightCornerLongitude = 24.397150f;
    //end of V2

    //V3
    @Getter private float v3BottomLeftCornerLatitude = 49.658540f;
    @Getter private float v3BottomLeftCornerLongitude = 20.845411f;

    @Getter private float v3UpperRightCornerLatitude = 52.502860f;
    @Getter private float v3UpperRightCornerLongitude = 24.391747f;
    //end of V3

    @Setter @Getter private float bottomLeftCornerLatitude;
    @Setter @Getter private float bottomLeftCornerLongitude;

    @Setter @Getter private float upperRightCornerLatitude;
    @Setter @Getter private float upperRightCornerLongitude;

    public float getRelativeLongitudeZero(){
        return getBottomLeftCornerLongitude();
    }

    public float getRelativeLatitudeZero() {
        return getUpperRightCornerLatitude();
    }

    public float getLongitudeResolution(){
        return getUpperRightCornerLongitude() - getBottomLeftCornerLongitude();
    }

    public float getLatitudeResolution(){
        return getUpperRightCornerLatitude() - getBottomLeftCornerLatitude();
    }



//    @Getter private float relativeLongitudeZero =
//            getBottomLeftCornerLongitude();
//    @Getter private float relativeLatitudeZero =
//            getUpperRightCornerLatitude();
//
//    @Getter private float longitudeResolution =
//            getUpperRightCornerLongitude() - bottomLeftCornerLongitude;
//    @Getter private float latitudeResolution =
//            getUpperRightCornerLatitude() - getBottomLeftCornerLatitude();
}
