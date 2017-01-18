/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.sensors;

import simulation.Simulator;
import simulation.robot.Robot;
import simulation.robot.actuators.RoleActuator;
import simulation.util.Arguments;

/**
 * Sensor to perceive the output value of a 
 * robot's own RoleActuator in the previous time step
 * @author guest
 */
public class RoleOwnSensor extends Sensor {

    
    
    private Robot robot;                    //the robot that owns
                                            //this sensor
    
    
    private RoleActuator act;               //variable to be used in a method
                                            //declared here only for efficiency
    
    
    
    public RoleOwnSensor( Simulator simulator, int id, Robot robot, Arguments args ) {
        super(simulator, id, robot, args);
        
        this.robot      = robot;
        
    }

    
    
    @Override
    public double getSensorReading( int sensorNumber ) {
        
        act = (RoleActuator) robot.getActuatorByType( RoleActuator.class );
        if ( act != null ) {
            return act.getLastValue();
        }
        else{
            return 0.0;
        }
        
    }
   
    
    
}
