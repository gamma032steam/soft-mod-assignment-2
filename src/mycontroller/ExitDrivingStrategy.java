package mycontroller;

import controller.CarController;
import tiles.MapTile;
import utilities.Coordinate;
import world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ExitDrivingStrategy extends DrivingStrategy {

    @Override
    public boolean isApplicable(HashMap<Coordinate, MapTile> explored, CarController controller) {
        if (controller.numParcelsFound() < controller.numParcels()) {
            return false;
        }

        // Get hashmap with only exit tiles
        Map<Coordinate, MapTile> exits = filterVisible(explored, new MapTile(MapTile.Type.FINISH));

        for (Coordinate location : exits.keySet()) {
            if ((Pather.canReach(new Coordinate(controller.getPosition()), explored, location))) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Coordinate getNextMove(HashMap<Coordinate, MapTile> explored, CarController controller) {
        if (Pather.isSameType(World.getMap().get(new Coordinate(controller.getPosition())), new MapTile (MapTile.Type.FINISH))) {
            return null;
        }

        Map<Coordinate, MapTile> exits = filterVisible(explored, new MapTile(MapTile.Type.FINISH));
        ArrayList<Coordinate> exitCoords = new ArrayList<Coordinate>(exits.keySet());
        Coordinate currentPosition = new Coordinate(controller.getPosition());

        Coordinate nearestExit = Pather.getNearest(currentPosition, explored, exitCoords);
        return Pather.getNextMove(currentPosition, explored, nearestExit);
    }

}
