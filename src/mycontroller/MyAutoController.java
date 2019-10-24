package mycontroller;

import controller.CarController;
import world.Car;

import java.util.HashMap;

import tiles.MapTile;
import utilities.Coordinate;

public class MyAutoController extends CarController{

		/** Tiles of the map discovered so far */
		private HashMap<Coordinate, MapTile> exploredMap;

		private DrivingStrategy strategy;

		public MyAutoController(Car car) {
			super(car);
			// What we have seen in our 9x9 vision
			strategy = DrivingStrategyFactory.getInstance().getStrategy();
			exploredMap = new HashMap<Coordinate, MapTile>();
		}

		@Override
		public void update() {
			// Gets what the car can see
			HashMap<Coordinate, MapTile> currentView = getView();

			// Adds what is seen to the new explored hashmap
			exploredMap.putAll(currentView);
			

			try {
				Coordinate target = strategy.getNextMove(exploredMap, this);
				if (target == null) {
					return;
				}
				DriverUtility.driveTowards(target, this);
			} catch (Exception e) {
				e.printStackTrace();
				// Back up
				applyReverseAcceleration();
				if (getSpeed() == 0) {
					DriverUtility.setInReverse(true);
				}
			}
		}
}
