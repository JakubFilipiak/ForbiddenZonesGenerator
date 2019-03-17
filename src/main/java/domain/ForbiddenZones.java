package domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jakub Filipiak on 14.03.2019.
 */
public enum ForbiddenZones {

    INSTANCE;

    @Getter
    List<ForbiddenZone> forbiddenByColors = new ArrayList<>();
    @Getter
    List<ForbiddenZone> forbiddenByAngles = new ArrayList<>();
    @Getter
    List<ForbiddenZone> forbiddenByDrop = new ArrayList<>();
}
