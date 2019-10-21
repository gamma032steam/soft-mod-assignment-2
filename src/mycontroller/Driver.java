package mycontroller;

import controller.CarController;
import utilities.Coordinate;
import world.WorldSpatial;

import java.security.InvalidAlgorithmParameterException;

public class Driver {
    private CarController carController;

    private enum RelativeDirection {
        AHEAD,
        BEHIND,
        LEFT,
        RIGHT
    };

    public Driver(CarController carController) {
        this.carController = carController;
    }

    /** Move towards an adjacent tile */
    public void driveTowards(Coordinate adjacentDestination) throws InvalidAlgorithmParameterException {
        Coordinate currentPosition = new Coordinate(carController.getPosition());
        WorldSpatial.Direction currentOrientation = carController.getOrientation();
        WorldSpatial.Direction direction = findDirection(currentPosition, adjacentDestination);
        RelativeDirection relativeDirection = findRelativeDirection(currentOrientation, direction);
    }

    /** Turn based on a relative direction to the current car position */
    public void control(RelativeDirection relativeDirection) {
        switch (relativeDirection) {
            case LEFT :
                carController.turnLeft();
                break;
            case RIGHT:
                carController.turnRight();
                break;
            case AHEAD:
                if (carController.getSpeed() >= 0) {
                    carController.applyForwardAcceleration();
                } else {
                    carController.applyBrake();
                }
            case BEHIND:
                if (carController.getSpeed() <= 0) {
                    carController.applyReverseAcceleration();
                } else {
                    carController.applyBrake();
                }
        }
    }

    /** Find the compass direction to the destination */
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

    /** Returns the direction our car needs to move to reach the destination */
    private RelativeDirection findRelativeDirection (WorldSpatial.Direction currentOrientation,
                                                     WorldSpatial.Direction direction) {
        if (currentOrientation == direction) {
            return RelativeDirection.AHEAD;
        } else if (oppositeDirection(currentOrientation) == direction) {
            return RelativeDirection.BEHIND;
        } else if (rightDirection(currentOrientation) == direction) {
           return RelativeDirection.RIGHT;
        } else {
            // Must be left
            return RelativeDirection.LEFT;
        }


    }

    /** Returns the compass direction to the right (plus 90 degrees) */
    private WorldSpatial.Direction rightDirection(WorldSpatial.Direction direction) {
        if (direction == WorldSpatial.Direction.NORTH) {
            return WorldSpatial.Direction.EAST;
        } else if (direction == WorldSpatial.Direction.EAST) {
            return WorldSpatial.Direction.SOUTH;
        } else if (direction == WorldSpatial.Direction.SOUTH) {
            return WorldSpatial.Direction.WEST;
        } else {
            // West
            return WorldSpatial.Direction.NORTH;
        }
    }

    /** Returns the opposite compass direction */
    private WorldSpatial.Direction oppositeDirection (WorldSpatial.Direction direction) {
        if (direction == WorldSpatial.Direction.NORTH) {
            return WorldSpatial.Direction.SOUTH;
        } else if (direction == WorldSpatial.Direction.SOUTH) {
            return WorldSpatial.Direction.NORTH;
        } else if (direction == WorldSpatial.Direction.EAST) {
            return WorldSpatial.Direction.WEST;
        } else {
            // West
            return WorldSpatial.Direction.EAST;
        }
    }
}
