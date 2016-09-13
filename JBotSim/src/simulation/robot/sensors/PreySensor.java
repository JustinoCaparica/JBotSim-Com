package simulation.robot.sensors;

import simulation.Simulator;
import simulation.physicalobjects.PhysicalObject;
import simulation.physicalobjects.Prey;
import simulation.physicalobjects.checkers.AllowOrderedPreyChecker;
import simulation.robot.Robot;
import simulation.util.Arguments;

public class PreySensor extends LightTypeSensor {

    
    
	public PreySensor(Simulator simulator, int id, Robot robot, Arguments args) {
		super(simulator, id, robot, args);
		setAllowedObjectsChecker(new AllowOrderedPreyChecker(robot.getId()));
	}
        
        
        /**
         * Checks if the sensor is detecting
         * a given prey
         * @param prey the prey
         * @return true if the prey is
         * detected, false otherwise
         */
        public Boolean detects( Prey prey ){
            
            for ( PhysicalObject p : getDetectedObjects() ) {
                if ( p != null && p.equals(prey) ) {
                    return true;
                }
            }
            
            return false;
        }
        
        
    
        /**
         * Gets the prey that is being detected
         * by a given sensor
         * @param sensorNumber the id of the sensor
         * @return the prey
         */
        public Prey getDetectedPrey( int sensorNumber ){
            return (Prey)getDetectedObjects()[sensorNumber];
        }
        
        
}