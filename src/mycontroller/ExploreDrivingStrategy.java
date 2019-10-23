package mycontroller;

import controller.CarController;
import tiles.MapTile;
import utilities.Coordinate;

import java.util.HashMap;

public class ExploreDrivingStrategy extends DrivingStrategy {

    @Override
    public boolean isApplicable(HashMap<Coordinate, MapTile> explored, CarController controller) {
        return true;
    }

    @Override
    public Coordinate getNextMove(HashMap<Coordinate, MapTile> explored, CarController controller) {
        return null;
    }
}
