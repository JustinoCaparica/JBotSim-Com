/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.sensors;

import java.util.Random;
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
    @ArgumentsAnnotation(name = "range", defaultValue = RANGE_STR, help = "sensor's range")
    private Double range; 
    
    
    
    private static final Double NOISE = 0.015;
    private static final String NOISE_STR = "0.015";
    @ArgumentsAnnotation(name = "noise", defaultValue = NOISE_STR, help = "sensor's noise. a value between -noise and noise is added to the sensor input. the final value is [0,1]")
    private Double noise; 
    
    
    
    
    private Simulator simulator;            //the simulator
    private Random random;                  //simulator randomizer
    
    
    private Robot robot;                    //the robot that owns
                                            //this sensor
    
    
    
    private RoleActuator act;               //variable to be used in a method
                                            //declared here only for efficiency
    
    
    
    public RoleHighestSensor( Simulator simulator, int id, Robot robot, Arguments args ) {
        super(simulator, id, robot, args);
        
        this.simulator  = simulator;
        this.robot      = robot;
        this.random     = simulator.getRandom();
        
        range = args.getArgumentAsDoubleOrSetDefault( "range", RANGE );
        noise = args.getArgumentAsDoubleOrSetDefault( "noise", NOISE );
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
        Double actVal;
        
        for (Robot closeRobot : simulator.getEnvironment().getClosestRobots(position, range) ) {
            
            if ( closeRobot != robot ) {                            //avoid choosing robot itself
                roleAct = (RoleActuator) closeRobot.getActuatorByType( RoleActuator.class );
                System.out.println("roleAct.getValue():" + roleAct.getValue());
                actVal = addNoise( roleAct.getValue() );
                System.out.println("actVal:" + actVal);
                if ( actVal > maxOutput ) {             
                    maxOutput = actVal;
                }
            }
            
        }
        
        //System.out.println("Robot " + robot.getId() + " maxOutput " + maxOutput);
        return maxOutput;
        
    }

    /**
     * Adds noise to a value
     * @param value
     * @return a value in the 
     * interval [0,1]
     */
    private Double addNoise(Double value) {
        
        Double actVal = value;
        
        actVal += ((random.nextDouble() * 2) - 1) * noise;
        if (actVal < 0) {
            actVal = 0.0;
        }
        else if( actVal > 1){
            actVal = 1.0;
        }
        
        return actVal;
    }
    
    
    
}
