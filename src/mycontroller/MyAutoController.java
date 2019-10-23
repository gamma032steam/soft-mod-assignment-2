package mycontroller;

import controller.CarController;
import world.Car;

import java.util.HashMap;

import tiles.MapTile;
import utilities.Coordinate;
import world.World;
import world.WorldSpatial;

public class MyAutoController extends CarController{		
		// How many minimum units the wall is away from the player.
		private int wallSensitivity = 1;
		
		private boolean isFollowingWall = false; // This is set to true when the car starts sticking to a wall.
		
		// Car Speed to move at
		private final int CAR_MAX_SPEED = 1;

		/** Tiles of the map discovered so far */
		private HashMap<Coordinate, MapTile> exploredMap;
		/** Whole map */
		private HashMap<Coordinate, MapTile> map;
		/** If we have seen a coordinate */
		private HashMap<Coordinate, Boolean> seenMap;

		/** Drives the car on behalf of the controller */
		Driver driver = new Driver(this);

		CompositeDrivingStrategy strategy = new CompositeDrivingStrategy();

		public MyAutoController(Car car) {
			super(car);
			// What we have seen in our 9x9 vision
			exploredMap = new HashMap<Coordinate, MapTile>();
			// All roads and tiles
			map = World.getMap();
			strategy.addStrategy(new ParcelDrivingStrategy());
			strategy.addStrategy(new ExitDrivingStrategy());
		}

		@Override
		public void update() {
			// Gets what the car can see
			HashMap<Coordinate, MapTile> currentView = getView();

			// Adds what is seen to the new explored hashmap
			exploredMap.putAll(currentView);
			// And note that we have seen the tile
			

			//printMap(explored);
			try {
				// TODO: Can just make a get exploredMap in the controller, so you don't have to pass it in
				Coordinate target = strategy.getNextMove(exploredMap, this);
				driver.driveTowards(target);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/**
		 * Check if you have a wall in front of you!
		 * @param orientation the orientation we are in based on WorldSpatial
		 * @param currentView what the car can currently see
		 * @return
		 */
		private boolean checkWallAhead(WorldSpatial.Direction orientation, HashMap<Coordinate, MapTile> currentView){
			switch(orientation){
			case EAST:
				return checkEast(currentView);
			case NORTH:
				return checkNorth(currentView);
			case SOUTH:
				return checkSouth(currentView);
			case WEST:
				return checkWest(currentView);
			default:
				return false;
			}
		}
		
		/**
		 * Check if the wall is on your left hand side given your orientation
		 * @param orientation
		 * @param currentView
		 * @return
		 */
		private boolean checkFollowingWall(WorldSpatial.Direction orientation, HashMap<Coordinate, MapTile> currentView) {
			
			switch(orientation){
			case EAST:
				return checkNorth(currentView);
			case NORTH:
				return checkWest(currentView);
			case SOUTH:
				return checkEast(currentView);
			case WEST:
				return checkSouth(currentView);
			default:
				return false;
			}	
		}
		
		/**
		 * Method below just iterates through the list and check in the correct coordinates.
		 * i.e. Given your current position is 10,10
		 * checkEast will check up to wallSensitivity amount of tiles to the right.
		 * checkWest will check up to wallSensitivity amount of tiles to the left.
		 * checkNorth will check up to wallSensitivity amount of tiles to the top.
		 * checkSouth will check up to wallSensitivity amount of tiles below.
		 */
		public boolean checkEast(HashMap<Coordinate, MapTile> currentView){
			// Check tiles to my right
			Coordinate currentPosition = new Coordinate(getPosition());
			for(int i = 0; i <= wallSensitivity; i++){
				MapTile tile = currentView.get(new Coordinate(currentPosition.x+i, currentPosition.y));
				if(tile.isType(MapTile.Type.WALL)){
					return true;
				}
			}
			return false;
		}
		
		public boolean checkWest(HashMap<Coordinate,MapTile> currentView){
			// Check tiles to my left
			Coordinate currentPosition = new Coordinate(getPosition());
			for(int i = 0; i <= wallSensitivity; i++){
				MapTile tile = currentView.get(new Coordinate(currentPosition.x-i, currentPosition.y));
				if(tile.isType(MapTile.Type.WALL)){
					return true;
				}
			}
			return false;
		}
		
		public boolean checkNorth(HashMap<Coordinate,MapTile> currentView){
			// Check tiles to towards the top
			Coordinate currentPosition = new Coordinate(getPosition());
			for(int i = 0; i <= wallSensitivity; i++){
				MapTile tile = currentView.get(new Coordinate(currentPosition.x, currentPosition.y+i));
				if(tile.isType(MapTile.Type.WALL)){
					return true;
				}
			}
			return false;
		}
		
		public boolean checkSouth(HashMap<Coordinate,MapTile> currentView){
			// Check tiles towards the bottom
			Coordinate currentPosition = new Coordinate(getPosition());
			for(int i = 0; i <= wallSensitivity; i++){
				MapTile tile = currentView.get(new Coordinate(currentPosition.x, currentPosition.y-i));
				if(tile.isType(MapTile.Type.WALL)){
					return true;
				}
			}
			return false;
		}


		public void printMap(HashMap<Coordinate,MapTile> explored) {
			Coordinate coord;
			for (int y = world.World.MAP_HEIGHT; y >= 0; y--) {
				for (int x = 0; x < world.World.MAP_WIDTH; x++) {
					coord = new Coordinate(x, y);
					if (explored.containsKey(coord)) {
						System.out.print("+");
					} else {
						System.out.print("-");
					}
				}
				System.out.println();
			}
		}

	//TODO: Update this once the pather is implemented
	public boolean isPathTo(Coordinate coordinate) {
		return true;


	}
}
