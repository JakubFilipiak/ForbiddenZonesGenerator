package domain;

import lombok.Getter;
import lombok.Setter;

import java.io.File;

/**
 * Created by Jakub Filipiak on 09.03.2019.
 */
public enum Track {

    INSTANCE;

    @Getter @Setter
    File trackFile;
//    File trackFile = new File("D:/ForbiddenZonesGenerator resources/LU-53-SP-ABM" +
//            ".trk");
}
