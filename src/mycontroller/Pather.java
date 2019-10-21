package mycontroller;

import tiles.MapTile;
import utilities.Coordinate;
import world.World;

import java.util.*;

public class Pather {

    /**
     * Gets this cool shit and finds da way
     * @param route
     * @param explored
     */
    public static void dijkstra(Coordinate root, HashMap<Coordinate, MapTile> explored, MapTile objective) {
        // Initialising the best distances map
        HashMap<Coordinate, Integer> tentativeDistance = new HashMap<>();
        for (Coordinate location : explored.keySet()) {
            tentativeDistance.put(location, Integer.MAX_VALUE);
        }
        tentativeDistance.replace(root, 0);

        HashSet<Coordinate> seen = new HashSet<>():

        while (!tentativeDistance.isEmpty()) {
            Coordinate u = getMin(tentativeDistance);
            seen.add(u);
            tentativeDistance.remove(u);

            for (Coordinate v : )
        }


    }

    private static Coordinate getMin(HashMap<Coordinate, Integer> map) {
        Integer min = Collections.min(map.values());
        for (Map.Entry<Coordinate, Integer> entry : map.entrySet()) {
            if (entry.getValue().equals(min)) {
                return entry.getKey();
            }
        }
    }

    private static ArrayList<Coordinate> getNeighbours(HashMap<Coordinate, MapTile> explored, Coordinate root, ) {
        ArrayList<Coordinate> neighbours = new ArrayList<>();
        Coordinate candidate;

        candidate = new Coordinate(root.x + 1, root.y)
        if (weighCoordinate(candidate)) neighbours.add(candidate);

        candidate = new Coordinate(root.x - 1, root.y)
        if (weighCoordinate(candidate)) neighbours.add(candidate);

        candidate = new Coordinate(root.x, root.y + 1)
        if (weighCoordinate(candidate)) neighbours.add(candidate);

        candidate = new Coordinate(root.x, root.y - 1)
        if (weighCoordinate(candidate)) neighbours.add(candidate);
    }

    /**
     * determines if the coordinate is within map boundaries and a "road" tile
     * Meanings:
     * -1: Impassable, i.e. wall
     * INT_MAX: Last resort, but techincally passable, like lava
     * @param candidate
     * @return
     */
    private static int weighCoordinate(HashMap<Coordinate, MapTile> explored, Coordinate candidate) {
        if (candidate.x >= 0 && candidate.x < World.MAP_WIDTH) {
            if (candidate.y >= 0 && candidate.y < World.MAP_HEIGHT) {
                return 1;
            }
        }
    }
}
