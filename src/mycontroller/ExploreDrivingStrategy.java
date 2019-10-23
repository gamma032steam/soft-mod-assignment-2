package mycontroller;

import controller.CarController;
import tiles.MapTile;
import utilities.Coordinate;

import java.util.*;

public class ExploreDrivingStrategy extends DrivingStrategy {

    @Override
    public boolean isApplicable(HashMap<Coordinate, MapTile> explored, CarController controller) {
        return true;
    }

    @Override
    public Coordinate getNextMove(HashMap<Coordinate, MapTile> explored, CarController controller) {
        ArrayList<Coordinate> unexploredCoords = new ArrayList<>();
        for (Coordinate coord : controller.getMap().keySet()) {
            if (!Pather.isTraversable(controller.getMap().get(coord))) {
                continue;
            }
            unexploredCoords.add(coord);
        }

        for (Coordinate coord : explored.keySet()) {
            unexploredCoords.remove(coord);
        }

        Coordinate nearestUnexplored = Pather.findNearestUnexplored(new Coordinate(controller.getPosition()), explored, controller.getMap());
        Coordinate nextMove = Pather.getNextMove(new Coordinate(controller.getPosition()), controller.getMap(), nearestUnexplored);
        return nextMove;
    }
}
