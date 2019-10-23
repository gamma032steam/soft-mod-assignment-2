package mycontroller;

import controller.CarController;
import tiles.MapTile;
import utilities.Coordinate;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

abstract public class DrivingStrategy {
    /**
     * Checks if the given conditions allow for this strategy to be used
     * @param explored
     * @param controller
     * @return True if applicable, false otherwise
     */
    public abstract boolean isApplicable(HashMap<Coordinate, MapTile> explored, CarController controller);

    /**
     * Gets the next tile to move to as deemed by the strategy
     * @param explored Map of tiles the car has seen
     * @param controller
     * @return The coordinate of the tile to move to
     * @throws Exception
     */
    public abstract Coordinate getNextMove(HashMap<Coordinate, MapTile> explored, CarController controller) throws Exception;

    /**
     * Filters the map for tiles which of type "type"
     * @param explored HashMap representation of the visible map
     * @param type MapTile instance of MapTile which the same as desired type
     * @return The filtered hashmap
     */
    protected Map<Coordinate, MapTile> filterVisible(HashMap<Coordinate, MapTile> explored, MapTile type) {
        Map<Coordinate, MapTile> tiles = explored.entrySet().stream()
                .filter(entry -> Pather.isSameType(entry.getValue(), type))
                .collect(Collectors.toMap(x->x.getKey(), x->x.getValue()));
        return tiles;
    }
}
