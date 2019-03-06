package domain;

import lombok.Data;

import java.time.LocalTime;

/**
 * Created by Jakub Filipiak on 25.02.2019.
 */
@Data
public class PointOfTrack {

    private float latitude;
    private float longitude;
    private LocalTime time;

    private PointOfTrack() {}

    public PointOfTrack(float latitude, float longitude, LocalTime time) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
    }
}
