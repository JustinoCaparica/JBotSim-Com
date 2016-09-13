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
                                            //null all robots within 
                                            //range are perceived

    @ArgumentsAnnotation(name = "seeDisabledRobots", defaultValue = "1", help = "If this value is set to 0 the sensor will not see disabled robots")
    private boolean seeDisabledRobots;
    
    @ArgumentsAnnotation(name = "seeRobotsWhenInRecruitment", defaultValue = "1", help = "If this value is set to 0 the sensor will not see other robots besides the recruiter/recruit when in a recruitment relationship")
    private boolean seeRobotsWhenInRecruitment;
    
    

    public RobotSensor(Simulator simulator, int id, Robot robot, Arguments args) {
        super(simulator, id, robot, args);
        setAllowedObjectsChecker(new AllowAllRobotsChecker(robot.getId()));

        target = null;

        seeDisabledRobots = args.getArgumentAsIntOrSetDefault("seeDisabledRobots", 1) == 1;
        
        seeRobotsWhenInRecruitment = args.getArgumentAsIntOrSetDefault("seeRobotsWhenInRecruitment", 1) == 1;
        
        
    }

    @Override
    protected double calculateContributionToSensor(int sensorNumber, PhysicalObjectDistance source) {
        
        boolean enabledDebug;
        enabledDebug = source.getObject().isEnabled();
        
        if ( !seeDisabledRobots &&
             !source.getObject().isEnabled() ) {
            return 0.0;
        }
        
        
//
//            if (target == null ) {
//                System.out.println("target = null");
//            }else{
//                System.out.println( "target=" + target.getDescription() );
//            }
//            
//            System.out.println("source.getObject():" + source.getObject());


//        if ( !seeRobotsWhenInRecruitment ) {
//            if ( target != null &&                          //there is a target and
//                !source.getObject().equals( target ) ) {    //the target is not the source
//                return 0.0;                                 //return contribution of 0.0
//            }
//        }
        

//        if ( !seeRobotsWhenInRecruitment ) {
            
//            if ( robot.getId() == 0) {
//                System.out.println("");
//                System.out.println("Do NOT see robots in recruitment");
//                System.out.println("robotSensor " + this.sensorPosition.toString() );
//                
//            }
                
            
//            if ( target != null ) {                         //there is a target
////                if ( robot.getId() == 0 ) {
////                    System.out.println("Robot 0 says there is a target. Return 0.0 for the robotSensor");
////                    System.out.println("The target is robot " + target.getId() );
////                    RobotSensor robotSensor = (RobotSensor) target.getSensorByType( RobotSensor.class );
////                    if ( robotSensor.getTarget() != null ) {
////                        System.out.println("Robot " + target.getId() + " robotSensor is targeting robot id: " + robotSensor.getTarget().getId() );
////                    }
////                    else{
////                        System.out.println("Robot " + target.getId() + " robotSensor is NULL");
////                    }
////                    
////                }
//                return 0.0;                                 //return contribution of 0.0
//            }
//            else{
////                if ( robot.getId() == 0 ) {
////                    System.out.println("Robot 0 says there is NO target. Return some VALUE for the robotSensor");
////                }
//            }
//            
//        
//        }
//        else{
////            System.out.println("see robots in recruitment");
////            System.out.println("");
//        }
        



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
