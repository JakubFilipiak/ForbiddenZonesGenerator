package utils;

import domain.ForbiddenZone;

import java.util.Comparator;

/**
 * Created by Jakub Filipiak on 14.03.2019.
 */
public class IntervalComparator implements Comparator<ForbiddenZone> {

    @Override
    public int compare(ForbiddenZone o1, ForbiddenZone o2) {

        if (o1.getEntranceTime().isBefore(o2.getEntranceTime())) {
            return -1;
        } else if (o1.getEntranceTime().isAfter(o2.getEntranceTime())) {
            return 1;
        } else {
            if (o1.getDepartureTime().isBefore(o2.getDepartureTime())) {
                return -1;
            } else if (o1.getDepartureTime().isAfter(o2.getDepartureTime())) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}