package mycontroller;

import controller.CarController;
import world.Car;

import java.util.HashMap;

import tiles.MapTile;
import utilities.Coordinate;
import world.World;
import world.WorldSpatial;

public class MyAutoController extends CarController{

		/** Tiles of the map discovered so far */
		private HashMap<Coordinate, MapTile> exploredMap;

		private CompositeDrivingStrategy strategy = new CompositeDrivingStrategy();

		public MyAutoController(Car car) {
			super(car);
			// What we have seen in our 9x9 vision
			exploredMap = new HashMap<Coordinate, MapTile>();
			strategy.addStrategy(new ParcelDrivingStrategy());
			strategy.addStrategy(new ExitDrivingStrategy());
			strategy.addStrategy(new ExploreDrivingStrategy());
		}

		@Override
		public void update() {
			// Gets what the car can see
			HashMap<Coordinate, MapTile> currentView = getView();

			// Adds what is seen to the new explored hashmap
			exploredMap.putAll(currentView);
			

			try {
				// TODO: Can just make a get exploredMap in the controller, so you don't have to pass it in
				Coordinate target = strategy.getNextMove(exploredMap, this);
				if (target == null) {
					return;
				}
				Driver.driveTowards(target, this);
			} catch (Exception e) {
				e.printStackTrace();
				// Back up
				applyReverseAcceleration();
				if (getSpeed() == 0) {
					Driver.setInReverse(true);
				}
			}
		}
}
