/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.sensors;

import mathutils.Vector2d;
import simulation.Simulator;
import simulation.robot.Robot;
import simulation.robot.actuators.RoleActuator;
import simulation.util.Arguments;
import simulation.util.ArgumentsAnnotation;

/**
 * Sensor to perceive the highest output value of a RoleActuator
 * of another robot
 * @author guest
 */
public class RoleHighestSensor extends Sensor {

    private static final Double RANGE = 1.0;
    private static final String RANGE_STR = "1.0";
    @ArgumentsAnnotation(name = "range", defaultValue = RANGE_STR, help = "actuator's range")
    private Double range; 
    
    
    private Simulator simulator;            //the simulator
    
    private Robot robot;                    //the robot that owns
                                            //this sensor
    
    
    
    private RoleActuator act;               //variable to be used in a method
                                            //declared here only for efficiency
    
    
    
    public RoleHighestSensor( Simulator simulator, int id, Robot robot, Arguments args ) {
        super(simulator, id, robot, args);
        
        this.simulator  = simulator;
        this.robot      = robot;
        
        range = args.getArgumentAsDoubleOrSetDefault( "range", RANGE );
    }

    
    
    @Override
    public double getSensorReading( int sensorNumber ) {
        
        return getHighestOutput( robot.getPosition(), range );
        
    }
    
    
    /**
     * Gets the highest output from all
     * robots within a range, except the
     * robot that owns this sensor
     * @param position the position
     * @param range the range
     * @return the highest output values
     * from robots within range
     */
    private Double getHighestOutput( Vector2d position, Double range ){
        
        RoleActuator roleAct;
        Double maxOutput = 0.0;
        
        
        for (Robot closeRobot : simulator.getEnvironment().getClosestRobots(position, range) ) {
            
            if ( closeRobot != robot ) {                            //avoid choosing robot itself
                roleAct = (RoleActuator) closeRobot.getActuatorByType( RoleActuator.class );
                if ( roleAct.getValue() > maxOutput ) {             
                    maxOutput = roleAct.getValue();
                }
            }
            
        }
        
        System.out.println("Robot " + robot.getId() + " maxOutput " + maxOutput);
        return maxOutput;
        
    }
    
    
    
}
