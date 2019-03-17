package domain;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.*;

/**
 * Created by Jakub Filipiak on 27.02.2019.
 */
public enum Map {

    INSTANCE;

    //    ClassLoader classLoader = getClass().getClassLoader();
//    File defaultMapFile =
//            new File(classLoader.getResource("LublinBig2.png").getFile());

    @Getter @Setter
    private InputStream mapFileInputStream = Map.class.getResourceAsStream(
            "/LublinBig2.png");



//    @Getter @Setter
//    private File mapFile = defaultMapFile;

    private Color defaultAllowedColor = new Color(25, 144, 25);;
    private Color defaultForbiddenColor = new Color(0, 0, 0);

    @Getter
    private Color allowedColor = defaultAllowedColor;
    @Getter
    private Color forbiddenColor = defaultForbiddenColor;

    @Setter
    private float bottomLeftCornerLatitude = 49.656450f;
    @Setter
    private float bottomLeftCornerLongitude = 20.834433f;

    @Setter
    private float upperRightCornerLatitude = 52.503133f;
    @Setter
    private float upperRightCornerLongitude = 24.397150f;

    @Getter
    private float relativeLongitudeZero = bottomLeftCornerLongitude;
    @Getter
    private float relativeLatitudeZero = upperRightCornerLatitude;

    @Getter
    private float longitudeResolution = upperRightCornerLongitude - bottomLeftCornerLongitude;
    @Getter
    private float latitudeResolution = upperRightCornerLatitude - bottomLeftCornerLatitude;
}
