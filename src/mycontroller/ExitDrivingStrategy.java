package mycontroller;

import controller.CarController;
import tiles.MapTile;
import utilities.Coordinate;
import world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExitDrivingStrategy extends DrivingStrategy {

    @Override
    public boolean isApplicable(HashMap<Coordinate, MapTile> explored, CarController controller) {
        if (controller.numParcelsFound() < controller.numParcels()) {
            return false;
        }

        // Get hashmap with only exit tiles
        Map<Coordinate, MapTile> exits = filterVisible(explored, new MapTile(MapTile.Type.FINISH));

        for (Coordinate location : exits.keySet()) {
            if ((PathingUtilities.canReach(new Coordinate(controller.getPosition()), explored, location))) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Coordinate getNextMove(HashMap<Coordinate, MapTile> searchSpace, CarController controller) {
        if (PathingUtilities.isSameType(World.getMap().get(new Coordinate(controller.getPosition())), new MapTile(MapTile.Type.FINISH))) {
            return null;
        }
        Map<Coordinate, MapTile> exits = filterVisible(searchSpace, new MapTile(MapTile.Type.FINISH));
        ArrayList<Coordinate> exitCoords = new ArrayList<Coordinate>(exits.keySet());
        Coordinate currentPosition = new Coordinate(controller.getPosition());

        Coordinate nearestExit = PathingUtilities.getNearest(currentPosition, searchSpace, exitCoords);
        return PathingUtilities.getNextMove(currentPosition, searchSpace, nearestExit);
    }

}
