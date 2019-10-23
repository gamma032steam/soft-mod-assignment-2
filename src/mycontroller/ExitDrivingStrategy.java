package mycontroller;

import tiles.MapTile;
import utilities.Coordinate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ExitDrivingStrategy extends DrivingStrategy {

    @Override
    public boolean isApplicable(HashMap<Coordinate, MapTile> explored, MyAutoController controller) {
        if (controller.numParcelsFound() < controller.numParcels()) {
            return false;
        }

        // Get hashmap with only exit tiles
        Map<Coordinate, MapTile> exits = explored.entrySet().stream()
                .filter(entry -> entry.getValue().getType() == MapTile.Type.FINISH)
                .collect(Collectors.toMap(x->x.getKey(), x->x.getValue()));

        for (Coordinate location : exits.keySet()) {
            if (canPathTo(location)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void doRule(HashMap<Coordinate, MapTile> explored, MyAutoController controller) {

    }

}
