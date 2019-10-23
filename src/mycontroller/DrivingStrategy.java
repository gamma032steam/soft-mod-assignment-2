package mycontroller;

import controller.CarController;
import tiles.MapTile;
import utilities.Coordinate;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

abstract public class DrivingStrategy {
    public abstract boolean isApplicable(HashMap<Coordinate, MapTile> explored, CarController controller);
    public abstract Coordinate getNextMove(HashMap<Coordinate, MapTile> explored, CarController controller);

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
