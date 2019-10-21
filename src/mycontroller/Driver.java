package mycontroller;

import controller.CarController;
import utilities.Coordinate;
import world.WorldSpatial;

import java.security.InvalidAlgorithmParameterException;

public class Driver {
    private CarController carController;

    private enum relativeDirection {
        AHEAD,
        BEHIND,
        LEFT,
        RIGHT
    };

    public Driver(CarController carController) {
        this.carController = carController;
    }

    public void driveTowards(Coordinate adjacentDestination) throws InvalidAlgorithmParameterException {
        Coordinate currentPosition = new Coordinate(carController.getPosition());
        WorldSpatial.Direction currentOrientation = carController.getOrientation();
        WorldSpatial.Direction direction = findDirection(currentPosition, adjacentDestination);
        
    }

    private WorldSpatial.Direction findDirection(Coordinate source, Coordinate destination)
            throws InvalidAlgorithmParameterException {
        int deltaX = destination.x - source.x;
        int deltaY = destination.y - source.y;

        // Check that the destination is not diagonally away from us
        if (deltaX != 0 | deltaY != 0) {
            throw new InvalidAlgorithmParameterException("Driver instructed to move diagonally");
        }

        // Check that we are not already at the destination
        if (source.x == destination.x && source.y == destination.y) {
            throw new InvalidAlgorithmParameterException("Driver instructed to move to its current position");
        }

        if (deltaX == 0) {
            // Above or below us
            if (deltaY > 0) {
                return WorldSpatial.Direction.NORTH;
            } else {
                return WorldSpatial.Direction.SOUTH;
            }
        } else {
            // Left or right of us
            if (deltaX > 0) {
                return WorldSpatial.Direction.EAST;
            } else {
                return WorldSpatial.Direction.WEST;
            }
        }
    }

    /*
    private relativeDirection findRelativeDirection(Coordinate source,
                                                    Coordinate destination, WorldSpatial.Direction orientation) {
        int deltaX = destination.x - source.x;
        int deltaY = destination.y - source.y;

        if (deltaX == 0) {
            // Either above or below us
            if (deltaY > 0) {

            }
        }
    }*/
}
