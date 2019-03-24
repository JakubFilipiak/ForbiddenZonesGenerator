package domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

/**
 * Created by Jakub Filipiak on 15.03.2019.
 */
public enum Properties {

    INSTANCE;

    @Getter @Setter private boolean processTimeRange = false;
    @Getter @Setter private LocalTime dropStartTime = null;
    @Getter @Setter private LocalTime dropStopTime = null;


    @Getter @Setter private boolean processPoints = false;
    @Getter @Setter private int pointsInTimeBuffer = 0;
    @Getter @Setter private int pointsOutTimeBuffer = 0;


    @Getter @Setter private boolean processTurns = false;
    @Getter @Setter private int minimumAngle = 10;
    @Getter @Setter private int turnsInTimeBuffer = 0;
    @Getter @Setter private int turnsOutTimeBuffer = 0;

    @Getter @Setter private boolean dropOnTurns = false;
    @Getter @Setter private int ignoredTurnsMinValue = 65;
    @Getter @Setter private int ignoredTurnsMaxValue = 115;


    @Getter @Setter private boolean pointsMultiplied = true;
    @Getter @Setter private int pointsDivider = 5;


    @Getter @Setter private boolean finalFileForm = true;
}