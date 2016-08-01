package simulation.robot.sensors;

import simulation.Simulator;
import simulation.physicalobjects.GeometricInfo;
import simulation.physicalobjects.PhysicalObjectDistance;
import simulation.physicalobjects.checkers.AllowAllRobotsChecker;
import simulation.robot.Robot;
import simulation.util.Arguments;

public class RobotSensor extends LightTypeSensor {

    private Robot target;                   //the robot to be perceived
                                            //by this sensor. If set to
                                            //null all robots within 
                                            //range are perceived
    
    
    public RobotSensor(Simulator simulator, int id, Robot robot,Arguments args) {
            super(simulator, id, robot, args);
            setAllowedObjectsChecker(new AllowAllRobotsChecker(robot.getId()));
            
            target = null;

    }
    
    
    
    @Override
	protected double calculateContributionToSensor(int sensorNumber, PhysicalObjectDistance source) {
//
//            if (target == null ) {
//                System.out.println("target = null");
//            }else{
//                System.out.println( "target=" + target.getDescription() );
//            }
//            
//            System.out.println("source.getObject():" + source.getObject());
            
            if ( target != null &&                          //there is a target and
                 !target.equals(source.getObject())) {      //the target is not the source
                //System.out.println("############# target equals source. returning 0.0");
                return 0.0;                                 //return contribution of 0.0
            }
            
            
            GeometricInfo sensorInfo = getSensorGeometricInfo(sensorNumber, source);

            if( (sensorInfo.getDistance() < getCutOff()) && 
                (sensorInfo.getAngle() < (openingAngle / 2.0)) && 
                (sensorInfo.getAngle() > (-openingAngle / 2.0)) ){
                    return (getRange() - sensorInfo.getDistance()) / getRange();
            }
            return 0;
	}

        
        
	@Override
	protected void calculateSourceContributions(PhysicalObjectDistance source) {
		for(int j=0; j<numberOfSensors; j++) {
			readings[j] = Math.max(calculateContributionToSensor(j, source), readings[j]);
		}
	}
    
    
    
    
    
    
    
    
    
    

    /**
     * Sets the targets of this sensor.
     * @param target a specific robot
     * to be perceived by the sensor
     * or null to perceive all 
     * neighbor robots
     */
    public void setTarget( Robot target ) {
        this.target = target;
    }
	
    
    
}
