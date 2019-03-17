package service;

import domain.ForbiddenZone;
import domain.ForbiddenZones;
import utils.IntervalComparator;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Jakub Filipiak on 14.03.2019.
 */
public enum ForbiddenZonesService {

    INSTANCE;


    private ForbiddenZones forbiddenZones = ForbiddenZones.INSTANCE;



    private List<ForbiddenZone> mergedList = new ArrayList<>();

    public List<ForbiddenZone> mergeLists() throws IOException {
        TextService textService = TextService.INSTANCE;

        List<ForbiddenZone> forbiddenByColors =
                forbiddenZones.getForbiddenByColors();
        List<ForbiddenZone> forbiddenByAngles =
                forbiddenZones.getForbiddenByAngles();
        List<ForbiddenZone> forbiddenByDrop =
                forbiddenZones.getForbiddenByDrop();

        mergedList.addAll(forbiddenByDrop);
        mergedList.addAll(forbiddenByColors);
        mergedList.addAll(forbiddenByAngles);

        textService.writeText("Poniżej wszystkie listy po kolei:");
        textService.writeText("Dolot/powrót:");
        textService.writeList(forbiddenByDrop);
        textService.writeText("Czarne punkty:");
        textService.writeList(forbiddenByColors);
        textService.writeText("Zakręty:");
        textService.writeList(forbiddenByAngles);

        if (mergedList.size() == 0 || mergedList.size() == 1) {
            return mergedList;
        }

        Collections.sort(mergedList, new IntervalComparator());
        textService.writeText("Poniżej listy połączone i posegregowane:");
        textService.writeList(mergedList);

        ForbiddenZone firstZone = mergedList.get(0);
        LocalTime entranceTime = firstZone.getEntranceTime();
        LocalTime departureTime = firstZone.getDepartureTime();

        List<ForbiddenZone> finalList = new ArrayList<>();

        for (int i = 1; i < mergedList.size(); i++) {
            ForbiddenZone currentZone = mergedList.get(i);
            if (currentZone.getEntranceTime().isBefore(departureTime)) {
                if (currentZone.getDepartureTime().isAfter(departureTime)) {
                    departureTime = currentZone.getDepartureTime();
                }
            } else {
                finalList.add(new ForbiddenZone(entranceTime, departureTime));
                entranceTime = currentZone.getEntranceTime();
                departureTime = currentZone.getDepartureTime();
            }
        }

        finalList.add(new ForbiddenZone(entranceTime, departureTime));

        clearAllList();

        return finalList;
    }

    private void clearAllList() {
        forbiddenZones.getForbiddenByDrop().clear();
        forbiddenZones.getForbiddenByColors().clear();
        forbiddenZones.getForbiddenByAngles();
        mergedList.clear();
    }


}
