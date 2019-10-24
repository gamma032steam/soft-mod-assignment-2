package mycontroller;

public class DrivingStrategyFactory {

    private static DrivingStrategyFactory instance;

    private DrivingStrategyFactory() { }

    public static DrivingStrategyFactory getInstance() {
        if (instance == null) {
            instance = new DrivingStrategyFactory();
        }
        return instance;
    }

    public DrivingStrategy getStrategy() {
        InOrderCompositeDrivingStrategy strategy = new InOrderCompositeDrivingStrategy();
        strategy.addStrategy(new ExitDrivingStrategy());
        strategy.addStrategy(new ParcelDrivingStrategy());
        strategy.addStrategy(new ExploreDrivingStrategy());
        return strategy;
    }
}
