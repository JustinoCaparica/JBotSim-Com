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
 * Sensor to perceive if another robot 
 * is focusing on this robot
 * @author gus
 */
public class FocusedBySensor extends Sensor {

    private final RecruitedSensor sensor;           //sensor that knows
                                                    //the recruited
    
    
    /**
     * Initializes a new instance
     * @param simulator the simulator
     * @param id the sensor id
     * @param robot the robot that
     * owns the sensor
     * @param args the arguments
     */
    public FocusedBySensor(Simulator simulator, int id, Robot robot, Arguments args) {
        super(simulator, id, robot, args);
        
        sensor = (RecruitedSensor) robot.getSensorByType( RecruitedSensor.class );
    }

    
    
    
    @Override
    public double getSensorReading( int sensorNumber ) {
        
        if ( sensor.getRecruit()!= null ) {         //there is a recruit
            return 1.0;                             //return 1
        }
        
        return 0.0;                                 //otherwise return 0
    }

    
    
    
    /**
     * Gets the robot that is
     * focusing on this
     * robot 
     * @return the robot
     * that has been recruited
     * by this robot or null if
     * no robot has been recruited 
     * by this robot
     */
    public Robot getRecruit() {
        return sensor.getRecruit();
    }

    
    /**
     * Determines if there is
     * another robot focusing
     * on this robot
     * @return true if there
     * is another robot focused
     * on this robot, false
     * otherwise
     */
    public boolean isFocused() {
        return sensor.getRecruit() != null;
    }
    
}
