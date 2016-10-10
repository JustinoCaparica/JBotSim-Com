package simulation.robot.sensors;

import simulation.Simulator;
import simulation.physicalobjects.GeometricInfo;
import simulation.physicalobjects.PhysicalObjectDistance;
import simulation.physicalobjects.checkers.AllowAllRobotsChecker;
import simulation.robot.Robot;
import simulation.util.Arguments;
import simulation.util.ArgumentsAnnotation;

public class RobotSensor extends LightTypeSensor {

    private Robot target;                   //the robot to be perceived
                                            //by this sensor. If set to
                                            //null any robot within 
                                            //range can be perceived

    @ArgumentsAnnotation(name = "seeDisabledRobots", defaultValue = "1", help = "If this value is set to 0 the sensor will not see disabled robots")
    private boolean seeDisabledRobots;
    
    

    public RobotSensor(Simulator simulator, int id, Robot robot, Arguments args) {
        super(simulator, id, robot, args);
        setAllowedObjectsChecker(new AllowAllRobotsChecker(robot.getId()));

        target = null;

        seeDisabledRobots = args.getArgumentAsIntOrSetDefault("seeDisabledRobots", 1) == 1;
        
        
    }

    @Override
    protected double calculateContributionToSensor(int sensorNumber, PhysicalObjectDistance source) {
        
        if ( target != null &&                  //there is a target 
             !target.equals(source) ) {         //that is not the source
            return 0.0;                         //return 0.0
        }
        
        
        
        
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

    /**
     * Sets the targets of this sensor.
     *
     * @param target a specific robot to be perceived by the sensor or null to
     * perceive all neighbor robots
     */
    public void setTarget(Robot target) {
        this.target = target;
    }

    /**
     * Gets the target of this sensor
     * @return the target or 
     * null if no target exists
     */
    public Robot getTarget() {
        return target;
    }

    
    
    
    
    
    
    
}
