package mycontroller;

import controller.CarController;
import tiles.MapTile;
import utilities.Coordinate;

import java.util.ArrayList;
import java.util.HashMap;

public class CompositeDrivingStrategy extends DrivingStrategy {

    private ArrayList<DrivingStrategy> strategies = new ArrayList<>();

    public void addStrategy(DrivingStrategy strategy) {
        strategies.add(strategy);
    }
    @Override
    public boolean isApplicable(HashMap<Coordinate, MapTile> explored, CarController controller) {
        for(DrivingStrategy strategy : strategies) {
            if (strategy.isApplicable(explored, controller)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Coordinate getNextMove(HashMap<Coordinate, MapTile> explored, CarController controller) throws Exception {
        for (DrivingStrategy strategy : strategies) {
            if (strategy.isApplicable(explored, controller)) {
                Coordinate nextMove = strategy.getNextMove(explored, controller);
                if (nextMove == null) {
                    return null;
                }

                return nextMove;
            }
        }
        return null;
    }
}
