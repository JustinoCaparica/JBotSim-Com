package simulation.robot.sensors;

import simulation.Simulator;
import simulation.physicalobjects.GeometricInfo;
import simulation.physicalobjects.PhysicalObject;
import simulation.physicalobjects.PhysicalObjectDistance;
import simulation.physicalobjects.checkers.AllowAllRobotsChecker;
import simulation.robot.Robot;
import simulation.util.Arguments;
import simulation.util.ArgumentsAnnotation;

public class RobotSensor extends LightTypeSensor {

    @ArgumentsAnnotation(name = "seeDisabledRobots", defaultValue = "1", help = "If this value is set to 0 the sensor will not see disabled robots")
    private boolean seeDisabledRobots;
    
    

    public RobotSensor(Simulator simulator, int id, Robot robot, Arguments args) {
        super(simulator, id, robot, args);
        setAllowedObjectsChecker(new AllowAllRobotsChecker(robot.getId()));


        seeDisabledRobots = args.getArgumentAsIntOrSetDefault("seeDisabledRobots", 1) == 1;
        
        
    }

    @Override
    protected double calculateContributionToSensor(int sensorNumber, PhysicalObjectDistance source) {
        
        
        
        boolean enabledDebug;
        enabledDebug = source.getObject().isEnabled();
        
        if ( !seeDisabledRobots &&
             !source.getObject().isEnabled() ) {
            return 0.0;
        }
        

        GeometricInfo sensorInfo = getSensorGeometricInfo(sensorNumber, source);

        if ((sensorInfo.getDistance() < getCutOff())
                && (sensorInfo.getAngle() < (openingAngle / 2.0))
                && (sensorInfo.getAngle() > (-openingAngle / 2.0))) {
            return (getRange() - sensorInfo.getDistance()) / getRange();
        }
        return 0;
    }

    @Override
    protected void calculateSourceContributions(PhysicalObjectDistance source) {
        for (int j = 0; j < numberOfSensors; j++) {
            readings[j] = Math.max(calculateContributionToSensor(j, source), readings[j]);
        }
    }


    
    
    
    
    
    
    
}
