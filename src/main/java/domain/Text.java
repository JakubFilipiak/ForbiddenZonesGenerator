package domain;

import lombok.Getter;
import lombok.Setter;

import java.io.File;

/**
 * Created by Jakub Filipiak on 09.03.2019.
 */
public enum Text {

    INSTANCE;

    @Getter
    @Setter
    private File textFile;
//    private File textFile = new File("D:/ForbiddenZonesGenerator " +
//            "resources/ForbiddenZonesBig2App.txt");
}
