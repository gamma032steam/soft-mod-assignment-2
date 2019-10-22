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
        HashMap<String, Integer> defaultTrapWeight = new HashMap<String, Integer>();
        defaultTrapWeight.put("lava", AVOID_WEIGHT);
        defaultTrapWeight.put("parcel", DEFAULT_WEIGHT);
        defaultTrapWeight.put("mud", IMPASSABLE_WEIGHT);
        defaultTrapWeight.put("water", DEFAULT_WEIGHT);
        defaultTrapWeight.put("health", DEFAULT_WEIGHT);
        defaultTrapWeight.put("grass", AVOID_WEIGHT);
        return defaultTrapWeight;
    }

    private static HashMap<MapTile.Type, Integer> DEFAULT_NON_TRAP_WEIGHT = defaultNonTrapWeightInitialiser();

    private static HashMap<MapTile.Type, Integer> defaultNonTrapWeightInitialiser() {
        HashMap<MapTile.Type, Integer> defaultNonTrapWeight = new HashMap<MapTile.Type, Integer>();
        defaultNonTrapWeight.put(MapTile.Type.WALL, IMPASSABLE_WEIGHT);
        defaultNonTrapWeight.put(MapTile.Type.UTILITY, DEFAULT_WEIGHT);
        defaultNonTrapWeight.put(MapTile.Type.START, DEFAULT_WEIGHT);
        defaultNonTrapWeight.put(MapTile.Type.FINISH, DEFAULT_WEIGHT);
        defaultNonTrapWeight.put(MapTile.Type.ROAD, DEFAULT_WEIGHT);
        defaultNonTrapWeight.put(MapTile.Type.EMPTY, DEFAULT_WEIGHT);
        return defaultNonTrapWeight;
    }

    /**
     * Gets this cool shit and finds da way
     * @param root
     * @param explored
     */
    public static Coordinate dijkstra(Coordinate root, HashMap<Coordinate, MapTile> explored, MapTile objective) {
        // Initialising the best distances map
        // As well as the previous map, will be used to trace back the route later
        HashMap<Coordinate, Integer> tentativeDistance = new HashMap<>();
        HashMap<Coordinate, Coordinate> previous = new HashMap<>();
        for (Coordinate location : explored.keySet()) {
            tentativeDistance.put(location, Integer.MAX_VALUE);
            previous.put(location, null);
        }
        // Root node has distance 0
        tentativeDistance.replace(root, 0);

        HashSet<Coordinate> seen = new HashSet<>();
        Coordinate found = null;
        while (!tentativeDistance.isEmpty()) {
            Coordinate u = getMin(tentativeDistance);
            seen.add(u);
            tentativeDistance.remove(u);

            // Found the closest objective
            if (isSameType(explored.get(u), objective)) {
                found = u;
                break;
            }

            Integer vDistance, candidateTentative;
            for (Coordinate v : getNeighbours(u)) {
                vDistance = weighTile(explored.get(v), objective);
                if (seen.contains(v) || vDistance.equals(IMPASSABLE_WEIGHT)) {
                    continue;
                }

                candidateTentative = tentativeDistance.get(u) + vDistance
                if (candidateTentative < tentativeDistance.get(v)) {
                    tentativeDistance.replace(v, candidateTentative);
                    previous.replace(v, u);
                }

            }

            if (found == null) {
                return null;
            }

            Coordinate x = found, y = null;
            while (!x.equals(root)) {
                y = x;
                x = previous.get(y);
            }
            return x;
        }


    }

    /**
     * Returns the key of the hashmap with the smallest value (the first one only actually)
     * @param map
     * @return
     */
    private static Coordinate getMin(HashMap<Coordinate, Integer> map) {
        Integer min = Collections.min(map.values());
        for (Map.Entry<Coordinate, Integer> entry : map.entrySet()) {
            if (entry.getValue().equals(min)) {
                return entry.getKey();
            }
        }
    }

    /**
     * Given a coordinate root, returns a set of its neighbours which are legal
     * @param root
     * @return
     */
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

    /**
     * Helper function for the weighTile function
     * @param trap
     * @param objective
     * @return
     */
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

    private static boolean isSameType(MapTile a, MapTile b) {
        if (a.getType().equals(b.getType())) {
            if (a.getType().equals(MapTile.Type.TRAP)) {
                TrapTile aTrap = (TrapTile) a;
                TrapTile bTrap = (TrapTile) b;
                return aTrap.getTrap().equals(bTrap.getTrap())
            } else {
                return true;
            }
        }
        return false;
    }
}
