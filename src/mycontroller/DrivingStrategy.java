package mycontroller;

import controller.CarController;
import tiles.MapTile;
import utilities.Coordinate;

import java.util.HashMap;

abstract public class DrivingStrategy {
    public abstract boolean isApplicable(HashMap<Coordinate, MapTile> explored, CarController controller);
    public abstract void doRule(HashMap<Coordinate, MapTile> explored, CarController controller);
    //TODO: Update this once the pather is implemented
    public boolean canPathTo(Coordinate coordinate) {
        return true;
    }
}
