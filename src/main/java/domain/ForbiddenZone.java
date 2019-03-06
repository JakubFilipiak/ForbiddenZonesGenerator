package domain;

import java.time.LocalTime;

/**
 * Created by Jakub Filipiak on 01.03.2019.
 */
public class ForbiddenZone {

    LocalTime entranceTime;
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

    public LocalTime getEntranceTime() {
        return entranceTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    @Override
    public String toString() {
        return "ForbiddenZone{" +
                "entranceTime=" + entranceTime +
                ", departureTime=" + departureTime +
                '}';
    }
}
