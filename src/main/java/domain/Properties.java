package domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

/**
 * Created by Jakub Filipiak on 15.03.2019.
 */
public enum Properties {

    INSTANCE;

    @Getter @Setter
    private LocalTime dropStartTime;
    @Getter @Setter
    private LocalTime dropStopTime;
    @Getter @Setter
    private int timeBuffer = 3;
    @Getter @Setter
    private int minimumAngle = 10;
    @Getter @Setter
    private boolean dropOnTurns = false;
    @Getter @Setter
    private int ignoredTurnsMinValue = 65;
    @Getter @Setter
    private int ignoredTurnsMaxValue = 115;
}
