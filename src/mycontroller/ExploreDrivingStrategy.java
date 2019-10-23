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

        //Add all traversable coordinates
        for (Coordinate coord : controller.getMap().keySet()) {
            if (!Pather.isTraversable(controller.getMap().get(coord))) {
                continue;
            }
            unexploredCoords.add(coord);
        }

        //Remove the seen coordinates
        for (Coordinate coord : explored.keySet()) {
            unexploredCoords.remove(coord);
        }

        Coordinate currentPosition = new Coordinate(controller.getPosition());
        Coordinate nearestUnexplored = Pather.findNearestUnexplored(currentPosition , explored, controller.getMap());
        Coordinate nextMove = Pather.getNextMove(currentPosition, controller.getMap(), nearestUnexplored);
        return nextMove;
    }
}
