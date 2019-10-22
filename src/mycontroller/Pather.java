package mycontroller;

import tiles.MapTile;
import tiles.TrapTile;
import utilities.Coordinate;
import world.World;

import java.util.*;
import java.util.stream.Collectors;

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
        defaultNonTrapWeight.put(MapTile.Type.EMPTY, IMPASSABLE_WEIGHT);
        defaultNonTrapWeight.put(MapTile.Type.TRAP, DEFAULT_WEIGHT); // Shouldn't happen
        return defaultNonTrapWeight;
    }

    /*
    public static Coordinate dijkstra(Coordinate root, HashMap<Coordinate, MapTile> explored, MapTile objective) {
        // Initialising the best distances map
        // As well as the previous map, will be used to trace back the route later
        HashMap<Coordinate, Integer> tentativeDistance = new HashMap<>();
        HashMap<Coordinate, Coordinate> previous = new HashMap<>();
        for (Coordinate location : explored.keySet()) {
            if (!isSameType(explored.get(location), new MapTile(MapTile.Type.WALL)) &&
                !isSameType(explored.get(location), new MapTile(MapTile.Type.EMPTY))) {
                System.out.println("Test");
                tentativeDistance.put(location, Integer.MAX_VALUE);
                previous.put(location, null);
            }
        }
        // Root node has distance 0
        tentativeDistance.replace(root, 0);

        HashSet<Coordinate> seen = new HashSet<>();
        Coordinate found = null;
        while (!tentativeDistance.isEmpty()) {
            Coordinate u = getMin(tentativeDistance);
            seen.add(u);
            tentativeDistance.remove(u);
            System.out.format("Uh: %s\n", explored.get(u).getType().toString());

            // Found the closest objective
            if (isSameType(explored.get(u), objective)) {
                found = u;
                break;
            }

            // Add neighbours
            Integer vDistance, candidateTentative;
            for (Coordinate v : getNeighbours(u)) {
                vDistance = weighTile(explored.get(v), objective);
                if (seen.contains(v) || vDistance.equals(IMPASSABLE_WEIGHT)) {
                    continue;
                }

                candidateTentative = tentativeDistance.get(u) + vDistance;
                if (candidateTentative < tentativeDistance.get(v)) {
                    tentativeDistance.replace(v, candidateTentative);
                    previous.replace(v, u);
                }

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
        return y;
    }
    */

    /**
     * Gets this cool shit and finds da way
     * @param root
     * @param explored
     */
    public static Coordinate dijkstra(Coordinate root, HashMap<Coordinate, MapTile> explored, Coordinate destination) {
        // Initialising the best distances map
        // As well as the previous map, will be used to trace back the route later
        HashMap<Coordinate, Integer> tentativeDistance = new HashMap<>();
        HashMap<Coordinate, Coordinate> previous = new HashMap<>();
        for (Coordinate location : explored.keySet()) {
            if (!isSameType(explored.get(location), new MapTile(MapTile.Type.WALL)) &&
                    !isSameType(explored.get(location), new MapTile(MapTile.Type.EMPTY))) {
                tentativeDistance.put(location, Integer.MAX_VALUE);
                previous.put(location, null);
            }
        }
        // Root node has distance 0
        tentativeDistance.replace(root, 0);

        HashSet<Coordinate> seen = new HashSet<>();
        Coordinate found = null;
        while (!tentativeDistance.isEmpty()) {
            Coordinate u = getMin(tentativeDistance);
            System.out.println("Min:" + u);
            seen.add(u);
            tentativeDistance.remove(u);
            //System.out.format("Uh: %s\n", explored.get(u).getType().toString());

            // Found the closest objective
            if (u.equals(destination)) {
                found = u;
                break;
            }

            // Add neighbours
            Integer vDistance, candidateTentative;
            for (Coordinate v : getNeighbours(u)) {
                vDistance = 1;
                if (seen.contains(v) || vDistance.equals(IMPASSABLE_WEIGHT)) {
                    continue;
                }

                candidateTentative = tentativeDistance.get(u) + vDistance;
                if (candidateTentative < tentativeDistance.get(v)) {
                    // New shortest path
                    tentativeDistance.replace(v, candidateTentative);
                    previous.replace(v, u);
                    System.out.println(v + "  " + u);
                }

            }
        }

        if (found == null) {
            return null;
        }

        Coordinate x = found, y = null;
        while (!x.equals(root)) {
            y = x;
            System.out.println(y);
            x = previous.get(y);
            System.out.println(x);
        }

        return y;
    }

    /**
     * Finds the shortest path to the target, moving only vertically or horizontally.
     * @param root Source location for the search
     * @param explored Map of available coordinates
     * @param target Destination for the search
     * @return The first coordinate in the path from the root. Null if the target was unreachable.
     */
    public static Coordinate dijkstra2(Coordinate root, HashMap<Coordinate, MapTile> explored, Coordinate target) {
        // Distance to a node
        HashMap<Coordinate, Integer> distance = new HashMap<>();
        // Previous neighbour node
        HashMap<Coordinate, Coordinate> previous = new HashMap<>();
        // Unexplored nodes
        ArrayList<Coordinate> unexplored = new ArrayList<>();

        // Set all nodes to infinity distance and no parent
        for (Coordinate coordinate: explored.keySet()) {
            // Get rid of empty, wall tiles
            if (!isTraversable(explored.get(coordinate))) {
                continue;
            }
            distance.put(coordinate, Integer.MAX_VALUE);
            previous.put(coordinate, null);
            unexplored.add(coordinate);
        }

        // 'Find' the source cell
        distance.replace(root, 0);
        Coordinate finalNode = null;

        while(!unexplored.isEmpty()) {
            // Construct a hashmap with the distances of unexplored nodes
            // TODO: This is a bit inefficient
            HashMap<Coordinate, Integer> unexploredDistances = new HashMap<>();
            for (Coordinate coordinate: unexplored) {
                unexploredDistances.put(coordinate, distance.get(coordinate));
            }
            // Get the current node as the one with the smallest distance
            Coordinate currentNode = getMin(unexploredDistances);
            unexplored.remove(currentNode);

            // Never pick impossible tiles just because we can see them
            if (distance.get(currentNode) == Integer.MAX_VALUE) {
                // Return null for safety
                return null;
            }

            // See if we reached the goal
            if (currentNode.equals(target)) {
                // Found it, let's backtrack
                finalNode = currentNode;
                break;
            }

            // Get the neighbours
            System.out.println(getNeighbours(currentNode).size());
            for (Coordinate neighbour: getNeighbours(currentNode)) {
                // Not valid if we're not tracking the distance for this coordinate (can't see/drive on it)
                if (!distance.containsKey(neighbour)) {
                    continue;
                }

                if (!unexplored.contains(neighbour)) {
                    continue;
                }

                // Distance is the distance to the current node plus one extra step
                int newDistance = distance.get(currentNode) + 1;

                // Is this the better distance?
                if (newDistance < distance.get(neighbour)) {
                    distance.put(neighbour, newDistance);
                    previous.put(neighbour, currentNode);
                }
            }
        }

        // Did we find it? If not, return null for safety
        if (finalNode == null) {
            return null;
        }

        // Backtrack to the node before the root
        Coordinate curr = finalNode, next = previous.get(curr);
        while(!next.equals(root)) {
            curr = next;
            next = previous.get(next);
        }

        return curr;
    }

    private static Boolean canReach(Coordinate root, HashMap<Coordinate, MapTile> explored, Coordinate target) {
        return !(dijkstra2(root, explored, target) == null);
    }

    /**
     * Analyses a tile for properties that would prevent a car from driving over it
     * @param tile Tile to analyse
     * @return True if a car could drive on this tile, False otherwise
     */
    private static Boolean isTraversable(MapTile tile) {
        return !isSameType(tile, new MapTile(MapTile.Type.WALL)) &&
               !isSameType(tile, new MapTile(MapTile.Type.EMPTY ));
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
        return null;
    }

    /**
     * Given a coordinate root, returns a set of its neighbours which are legal
     * @param root
     * @return
     */
    private static HashSet<Coordinate> getNeighbours(Coordinate root) {
        // Build a set of adjacent coordinates
        HashSet<Coordinate> neighbours = new HashSet<>();
        // Generate adjacent cells (including diagonals)
        for (int dx = -1; dx <= 1; dx += 1) {
            for (int dy = -1; dy <= 1; dy += 1) {
                Coordinate candidate = new Coordinate(root.x + dx, root.y + dy);
                // Remove diagonals and enforce game border
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
                return aTrap.getTrap().equals(bTrap.getTrap());
            } else {
                return true;
            }
        }
        return false;
    }
}
