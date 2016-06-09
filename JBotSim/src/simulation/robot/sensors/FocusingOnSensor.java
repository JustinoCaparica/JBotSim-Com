/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.sensors;

import simulation.Simulator;
import simulation.robot.Robot;
import simulation.util.Arguments;



/**
 * Sensor to perceive if the robot
 * is focusing on another robot
 * @author gus
 */
public class FocusingOnSensor extends Sensor {

    private final RecruiterSensor sensor;           //sensor that knows
                                                    //the recruiter
    
    
    /**
     * Initializes a new instance
     * @param simulator the simulator
     * @param id the sensor id
     * @param robot the robot that
     * owns the sensor
     * @param args the arguments
     */
    public FocusingOnSensor(Simulator simulator, int id, Robot robot, Arguments args) {
        super(simulator, id, robot, args);
        
        sensor = (RecruiterSensor) robot.getSensorByType( RecruiterSensor.class );
    }

    
    
    
    @Override
    public double getSensorReading( int sensorNumber ) {
        
        if ( sensor.getRecruiter() != null ) {      //there is a recruiter
            return 1.0;                             //return 1
        }
        
        return 0.0;                                 //otherwise return 0
    }

    
    
    
    /**
     * Gets the robot that this
     * robot is focusing on
     * @return the robot
     * that has recruited this
     * robot or null if no robot
     * has recruited this robot
     */
    public Robot getRecruiter() {
        return sensor.getRecruiter();
    }

    
    /**
     * Determines if the robot
     * is focused
     * @return true if the 
     * robot is focused, false
     * otherwise
     */
    public boolean isFocused() {
        return sensor.getRecruiter() != null;
    }
    
}
