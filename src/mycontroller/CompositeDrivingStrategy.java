package mycontroller;

import java.util.ArrayList;

public abstract class CompositeDrivingStrategy extends DrivingStrategy {

    protected ArrayList<DrivingStrategy> strategies = new ArrayList<>();

    public void addStrategy(DrivingStrategy strategy) {
        strategies.add(strategy);
    }
}
