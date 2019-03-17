package domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

/**
 * Created by Jakub Filipiak on 13.03.2019.
 */
@AllArgsConstructor
public class ForbiddenAnglePoint {

    @Getter
    private PointOfTrack entranceLinePointA;
    @Getter
    private PointOfTrack entranceLinePointB;

    @Getter
    private PointOfTrack departureLinePointA;
    @Getter
    private PointOfTrack departureLinePointB;

    @Getter
    private LocalTime ForbiddenAngleTime;
}
