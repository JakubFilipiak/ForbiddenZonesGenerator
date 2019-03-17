package domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

/**
 * Created by Jakub Filipiak on 01.03.2019.
 */
public class ForbiddenZone {

    @Getter
    LocalTime entranceTime;
    @Getter @Setter
    LocalTime departureTime;

    private ForbiddenZone() {}

    public ForbiddenZone(LocalTime entranceTime) {
        this.entranceTime = entranceTime;
    }

    public ForbiddenZone(LocalTime entranceTime, LocalTime departureTime) {
        this.entranceTime = entranceTime;
        this.departureTime = departureTime;
    }

    public void setEntranceTime(LocalTime entranceTime) {
        this.entranceTime = entranceTime;
    }


    @Override
    public String toString() {
        return "ForbiddenZone{" +
                "entranceTime=" + entranceTime +
                ", departureTime=" + departureTime +
                '}';
    }
}