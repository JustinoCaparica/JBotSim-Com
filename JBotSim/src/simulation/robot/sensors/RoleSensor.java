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
 * Sensor to perceive the output value of a RoleActuator
 * of another robot
 * @author guest
 */
public class RoleSensor extends Sensor {

    private static final Double RANGE = 1.0;
    private static final String RANGE_STR = "1.0";
    @ArgumentsAnnotation(name = "range", defaultValue = RANGE_STR, help = "actuator's range")
    private Double range; 
    
    
    private Simulator simulator;            //the simulator
    
    private Robot robot;                    //the robot that owns
                                            //this sensor
    
    
    
    private RoleActuator act;               //variable to be used in a method
                                            //declared here only for efficiency
    
    
    
    public RoleSensor( Simulator simulator, int id, Robot robot, Arguments args ) {
        super(simulator, id, robot, args);
        
        this.simulator  = simulator;
        this.robot      = robot;
        
        range = args.getArgumentAsDoubleOrSetDefault( "range", RANGE );
    }

    
    
    @Override
    public double getSensorReading( int sensorNumber ) {
        
        // get closest neighbour robot
        Robot closestRobot = getClosestRobot(robot.getPosition(), range);
        
        
        
        //determine the output value of the closest neighbour robot's actuator
        if ( closestRobot != null ) {
            act = (RoleActuator) closestRobot.getActuatorByType( RoleActuator.class );
            return act.getValue();
        }else{
            return 0.0;
        }
        
    }
    
    
    /**
     * Gets the closest robot to a 
     * given position, within a range
     * @param position the position
     * @param range the range
     * @return the closest robot or
     * null if there are no robots within
     * range
     */
    private Robot getClosestRobot( Vector2d position, Double range ){
        
        Robot closestRobot = null;                      //the closest robot, 
                                                        //that we are looking for
        
        Double distToClosestRobot = Double.MAX_VALUE;   //distance to the closest
                                                        //robot so far
        
        Double distToTempRobot;                         //variable to use
                                                        //inside the cycle
        
        for (Robot tempRobot : simulator.getEnvironment().getClosestRobots(position, range) ) {
            
            if ( tempRobot != robot ) {                             //avoid choosing robot itself
                distToTempRobot = tempRobot.getDistanceBetween(position);   //determine distance 
                                                                            //from robot to position

                if ( distToTempRobot < distToClosestRobot ) {               //a robot closer than
                    closestRobot = tempRobot;                               //the current closest 
                    distToClosestRobot = distToTempRobot;                   //robot was found!
                }
            }
            
        }
        
        return closestRobot;
        
    }
    
    
    
}
