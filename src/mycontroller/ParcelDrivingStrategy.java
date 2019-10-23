package mycontroller;

import controller.CarController;
import tiles.MapTile;
import tiles.ParcelTrap;
import utilities.Coordinate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ParcelDrivingStrategy extends DrivingStrategy{
    @Override
    public boolean isApplicable(HashMap<Coordinate, MapTile> explored, CarController controller) {
        if (controller.numParcelsFound() >= controller.numParcels()) {
            return false;
        }

        Map<Coordinate, MapTile> parcels = filterVisible(explored, new ParcelTrap());

        for (Coordinate location : parcels.keySet()) {
            if (Pather.canReach(new Coordinate(controller.getPosition()), explored, location)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Coordinate getNextMove(HashMap<Coordinate, MapTile> explored, CarController controller) {
        Map<Coordinate, MapTile> parcels = filterVisible(explored, new ParcelTrap());

        ArrayList<Coordinate> parcelCoords = new ArrayList<Coordinate>(parcels.keySet());
        Coordinate currentPosition = new Coordinate(controller.getPosition());

        Coordinate nearestParcel = Pather.getNearest(currentPosition, explored, parcelCoords);
        return Pather.getNextMove(currentPosition, explored, nearestParcel);
    }
}
