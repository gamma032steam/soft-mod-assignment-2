package mycontroller;

import controller.CarController;
import utilities.Coordinate;
import world.WorldSpatial;

import java.security.InvalidAlgorithmParameterException;

import static world.WorldSpatial.Direction.*;

public class Driver {
    private CarController carController;
    /** Keeps track of whether we are currently reversing */
    private static boolean inReverse = false;

    private enum RelativeDirection {
        AHEAD,
        BEHIND,
        LEFT,
        RIGHT
    }

    public Driver(CarController carController) {
        this.carController = carController;
    }

    /** Move towards an adjacent tile */
    public static void driveTowards(Coordinate adjacentDestination, CarController carController) throws InvalidAlgorithmParameterException {
        Coordinate currentPosition = new Coordinate(carController.getPosition());
        WorldSpatial.Direction currentOrientation = carController.getOrientation();
        WorldSpatial.Direction direction = findDirection(currentPosition, adjacentDestination);
        RelativeDirection relativeDirection = findRelativeDirection(currentOrientation, direction);
        control(relativeDirection, carController);
    }

    /** Turn based on a relative direction to the current car position */
    private static void control(RelativeDirection relativeDirection, CarController carController) {
        System.out.print(relativeDirection);
        float speed = carController.getSpeed();
        switch (relativeDirection) {
            case LEFT :
                // Turn left if we have the speed
                if (speed != 0) {
                    carController.turnLeft();
                } else {
                    // We're at the start. Begin by moving forwards.
                    carController.applyForwardAcceleration();
                }
                break;
            case RIGHT:
                // Turn right if we have the speed, otherwise back up and turn
                if (speed != 0) {
                    carController.turnRight();
                } else {
                    // We're at the start. Begin by moving forwards.
                    carController.applyForwardAcceleration();
                }
                break;
            case AHEAD:
                // Go forward only if we are still or in reverse, to limit speed at 1
                System.out.print(inReverse);
                if (speed == 0) {
                    carController.applyForwardAcceleration();
                    // Switching out of reverse
                    inReverse = false;
                } else if (inReverse) {
                    carController.applyForwardAcceleration();
                }
                break;
            case BEHIND:
                // Go backwards only if we are are still or going forwards
                if (speed == 0) {
                    System.out.println("beep beep reversing");
                    carController.applyReverseAcceleration();
                    // Switching into reverse
                    inReverse = true;
                } else if (!inReverse) {
                    carController.applyReverseAcceleration();
                }
                break;
        }
    }

    /** Find the compass direction to the destination */
    private static WorldSpatial.Direction findDirection(Coordinate source, Coordinate destination)
            throws InvalidAlgorithmParameterException {
        int deltaX = destination.x - source.x;
        int deltaY = destination.y - source.y;

        // No source or destination found
        if (destination == null || source == null) {
            throw new InvalidAlgorithmParameterException("Source or destination was null!");
        }

        // Check that the destination is not diagonally away from us
        if (deltaX != 0 && deltaY != 0) {
            throw new InvalidAlgorithmParameterException("Driver instructed to move diagonally, deltaX = " + deltaX +" deltaY =" + deltaY);
        }

        // Check that we are not already at the destination
        if (source.x == destination.x && source.y == destination.y) {
            throw new InvalidAlgorithmParameterException("Driver instructed to move to its current position");
        }

        if (deltaX == 0) {
            // Above or below us
            if (deltaY > 0) {
                return NORTH;
            } else {
                return SOUTH;
            }
        } else {
            // Left or right of us
            if (deltaX > 0) {
                return WorldSpatial.Direction.EAST;
            } else {
                return WEST;
            }
        }
    }

    /** Returns the direction our car needs to move to reach the destination */
    private static RelativeDirection findRelativeDirection (WorldSpatial.Direction currentOrientation,
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
    private static WorldSpatial.Direction rightDirection(WorldSpatial.Direction direction) {
        switch(direction) {
            case NORTH:
                return EAST;
            case EAST:
                return SOUTH;
            case SOUTH:
                return WEST;
            default:
                // West
                return NORTH;
        }
    }

    /** Returns the opposite compass direction */
    private static WorldSpatial.Direction oppositeDirection (WorldSpatial.Direction direction) {
        switch(direction) {
            case NORTH:
                return SOUTH;
            case SOUTH:
                return NORTH;
            case EAST:
                return WEST;
            default:
                // WEST
                return EAST;
        }
    }

    public static void setInReverse(Boolean inReverse) {
        Driver.inReverse = inReverse;
    }
}
