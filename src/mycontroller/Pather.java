package mycontroller;

import tiles.MapTile;
import tiles.TrapTile;
import utilities.Coordinate;
import world.World;

import java.util.*;

public class Pather {

    private static Integer SMALL_WEIGHT = -1000;
    private static Integer AVOID_WEIGHT = 1000;
    private static Integer IMPASSABLE_WEIGHT = null;
    private static Integer DEFAULT_WEIGHT = 1;

    private static HashMap<String, Integer> DEFAULT_TRAP_WEIGHT = defaultTrapWeightInitialiser();

    private static HashMap<String, Integer> defaultTrapWeightInitialiser() {
        HashMap<String, Integer> defaultTrapWeight = new HashMap<String, Integer>;
        defaultTrapWeight.put("lava", AVOID_WEIGHT);
        defaultTrapWeight.put("parcel", DEFAULT_WEIGHT);
        defaultTrapWeight.put("mud", IMPASSABLE_WEIGHT);
        defaultTrapWeight.put("water", DEFAULT_WEIGHT);
        defaultTrapWeight.put("health", DEFAULT_WEIGHT);
        defaultTrapWeight.put("grass", AVOID_WEIGHT);
        return defaultTrapWeight
    }

    private static HashMap<MapTile.Type, Integer> DEFAULT_NON_TRAP_WEIGHT = defaultNonTrapWeightInitialiser();

    private static HashMap<MapTile.Type, Integer> defaultNonTrapWeightInitialiser() {
        HashMap<String, Integer> defaultNonTrapWeight = new HashMap<String, Integer>;
        defaultNonTrapWeight.put(MapTile.Type.WALL, IMPASSABLE_WEIGHT);
        defaultNonTrapWeight.put(MapTile.Type.UTILITY, DEFAULT_WEIGHT);
        defaultNonTrapWeight.put(MapTile.Type.START, DEFAULT_WEIGHT);
        defaultNonTrapWeight.put(MapTile.Type.FINISH, DEFAULT_WEIGHT);
        defaultNonTrapWeight.put(MapTile.Type.ROAD, DEFAULT_WEIGHT);
        defaultNonTrapWeight.put(MapTile.Type.EMPTY, DEFAULT_WEIGHT);
    }

    /**
     * Gets this cool shit and finds da way
     * @param root
     * @param explored
     */
    public static void dijkstra(Coordinate root, HashMap<Coordinate, MapTile> explored, MapTile objective) {
        // Initialising the best distances map
        HashMap<Coordinate, Integer> tentativeDistance = new HashMap<>();
        HashMap<Coordinate, Coordinate> previous = new HashMap<>();
        for (Coordinate location : explored.keySet()) {
            tentativeDistance.put(location, Integer.MAX_VALUE);
            previous.put(location, null);
        }
        tentativeDistance.replace(root, 0);

        HashSet<Coordinate> seen = new HashSet<>();
        while (!tentativeDistance.isEmpty()) {
            Coordinate u = getMin(tentativeDistance);
            seen.add(u);
            tentativeDistance.remove(u);

            Integer vDistance, candidateTentative;
            for (Coordinate v : getNeighbours(u)) {
                if (!seen.contains(v)) {
                    vDistance = weighTile(explored.get(v), objective);
                    if (!vDistance.equals(IMPASSABLE_WEIGHT)) {
                        candidateTentative = tentativeDistance.get(u) + vDistance
                        if (candidateTentative < tentativeDistance.get(v)) {
                            tentativeDistance.replace(v, candidateTentative);
                            previous.replace(v, u);
                        }

                    }
                }
            }
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

    private static HashSet<Coordinate> getNeighbours(Coordinate root) {
        HashSet<Coordinate> neighbours = new HashSet<>();
        for (int dx = -1; dx <= 1; dx += 2) {
            for (int dy = -1; dy <= 1; dy += 2) {
                Coordinate candidate = new Coordinate(root.x + dx, root.y + dy);
                if (Math.abs(dx + dy) == 1 && isWithinWorldBoundaries(candidate)) {
                    neighbours.add(candidate);
                }
            }
        }
        return neighbours;
    }

    private static boolean isWithinWorldBoundaries(Coordinate candidate) {
        if (candidate.x >= 0 && candidate.x < World.MAP_WIDTH) {
            if (candidate.y >= 0 && candidate.y < World.MAP_HEIGHT) {
                return true;
            }
        }
        return false;
    }

    private static Integer weighTile(MapTile tile, MapTile objective) {
        if (tile.getType().equals(MapTile.Type.TRAP)) {
            return weighTrap((TrapTile) tile, objective);
        } else if (tile.getType().equals(objective.getType())) {
            return SMALL_WEIGHT;
        } else {
            return DEFAULT_NON_TRAP_WEIGHT.get(tile.getType());
        }

    }

    private static Integer weighTrap(TrapTile trap, MapTile objective) {
        // If objective is also a trap
        if (objective.getType().equals(MapTile.Type.TRAP)) {
            TrapTile objectiveTrap = (TrapTile) objective;
            if (trap.getTrap().equals(objectiveTrap.getTrap())) {
                return SMALL_WEIGHT;
            }
        }
        return DEFAULT_TRAP_WEIGHT.get(trap.getTrap());
    }

    /*
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



    private static Integer weighCoordinate(HashMap<Coordinate, MapTile> explored, Coordinate candidate) {
        if (candidate.x >= 0 && candidate.x < World.MAP_WIDTH) {
            if (candidate.y >= 0 && candidate.y < World.MAP_HEIGHT) {
                MapTile tile = explored.get(candidate);

            }
        }
    }

    private static int weighTile(MapTile tile) {
        if (tile.getType().equals(MapTile.Type.TRAP)) {
            TrapTile trap = (TrapTile) tile;
            switch (trap.getTrap()) {
                case "lava":
                    return 10000;
                case "mud":
                    return -1;
                case "water":
                    return 1;
                case "parcel":
                    return Integer.MIN_VALUE;
            }
        } else {
            switch (tile.getType()) {
                case WALL:
                    return null;
                case ROAD:
                    return 1
                case EXIT:

            }
        }
    }
    */
}
