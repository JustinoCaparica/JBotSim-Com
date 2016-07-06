/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.sensors;

import java.util.List;
import mathutils.Vector2d;
import simulation.Simulator;
import simulation.physicalobjects.Nest;
import simulation.physicalobjects.PhysicalObject;
import simulation.robot.Robot;
import simulation.util.Arguments;

/**
 * Sensor to perceive the distance
 * and the angle to the nest.
 * @author gus
 */
public class NestRangeAndBearingSensor extends Sensor {

    
    private final Simulator simulator;  //the simulator
    
    private Nest nest;                  //the nest being perceived
                                        //by this sensor
    
    
    
    /**
     * Initializes a new instance
     * @param simulator the simulator
     * @param id the id of the sensor
     * @param robot the robot that
     * owns this sensor
     * @param args the arguments
     */
    public NestRangeAndBearingSensor(Simulator simulator, int id, Robot robot, Arguments args) {
        super(simulator, id, robot, args);
        
        this.simulator = simulator;
        
    }

    
    
    @Override
    public double getSensorReading( int sensorNumber ) {
        
        //this pieace of code should be in the constructor
        //but at constructor time the environment is not
        //available yet
        if ( nest == null ) {
            
            List<PhysicalObject> objects;                           //get all objects
            objects = simulator.getEnvironment().getAllObjects();   //from the environment

            for (PhysicalObject object : objects) {
                if ( object instanceof Nest ) {                     //if object of type Nest
                    nest = (Nest) object;                           //set object as nest
                    break;                                          //ugly but quick
                }
            }
        
        }
        
        
        
        
        
        if ( nest == null ) {                   //there is no nest
            return 0.0;                         //angle is 0; distance is 0
        }
        
        
        
        switch ( sensorNumber ) {
            
            case 0:                                             //sensorNumber is 0
                                                                //return distance
                return getDistanceOutVal( nest, super.robot );
            
            case 1:                                             //sensorNumber is 1
                return getAngleOutVal( nest, super.robot );     //return angle                                
            
            default:
                throw new RuntimeException("Invalid sensor number in NestRangeAndBearingSensor");
        }
        
        
        
    }

    

    
    
    
    
    
    
    
    
    
    /**
     * Determines the output value
     * according to distance
     * from the robot to the nest
     * @param robot the robot
     * @param nest the nest
     * @return the inverse of the
     * (distance+1) in the range ]0,1]
     */
    private double getDistanceOutVal( Nest nest, Robot robot ){
        
        double distance;                    //distance between robots
        distance = nest.getPosition().distanceTo( robot.getPosition() );
        
        distance = distance + 1.0;          //making sure distance >= 1

        return  1 / distance;               //return value is inverse of distance
    }

    
    /**
     * Determines the output value
     * according to the angle between
     * the nest and the orientation
     * of the robot
     * @param nest the nest
     * @param robot the robot
     * @return the angle in radians
     * in range [0,1] where 0.5 means
     * the nest is in front of the
     * robot
     */
    private double getAngleOutVal( Nest nest, Robot robot ) {
        
        double robotOrientation;
        robotOrientation = robot.getOrientation();      //robot orientation 
                                                        //in range [-PI,PI]
        
        
                                                        //vector representation
        Vector2d v = new Vector2d( robotOrientation );  //of robot orientation
        
        
        Vector2d nestVector;                            //nest position
                                                        //relative to robot
        nestVector = new Vector2d(nest.getPosition().x - robot.getPosition().x,
                                  nest.getPosition().y - robot.getPosition().y);
        
        
        double angle;                                   //angle between robots
        angle = v.signedAngle( nestVector );            //in range ]-Pi, PI]
        
        angle = angle + Math.PI;                             
        angle = angle / (2*Math.PI);                    //orientation in range [0,1]
        
        return angle;
        
    }
    
    
    
    
    
    
    
    
    
}
