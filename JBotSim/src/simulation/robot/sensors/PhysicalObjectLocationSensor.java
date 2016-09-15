/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.sensors;

import mathutils.Vector2d;
import simulation.Simulator;
import simulation.physicalobjects.PhysicalObject;
import simulation.robot.Robot;
import simulation.util.Arguments;

/**
 * Sensor to perceive the distance
 * and the angle to a given PhysicalObject
 * @author gus
 */
public class PhysicalObjectLocationSensor extends Sensor {

    
    
    
    private PhysicalObject targetObject;            //object being target
                                                    //by the sensor
    
    
    /**
     * Initializes a new instance
     * @param simulator the simulator
     * @param id the id of the sensor
     * @param robot the robot that
     * owns this sensor
     * @param args the arguments
     */
    public PhysicalObjectLocationSensor( Simulator simulator, int id, Robot robot, Arguments args ) {
        super(simulator, id, robot, args);
        
        targetObject = null;
    }

    
    
    @Override
    public double getSensorReading( int sensorNumber ) {
        
        if ( targetObject == null ) {                //there is no targetObject
            return 0.0;                         //angle is 0; distance is 0
        }
        
        
        
        
        switch ( sensorNumber ) {
            
            case 0:                                                 //sensorNumber is 0
                                                                    //return distance
                return getDistanceOutVal( super.robot, targetObject );
            
            case 1:                                                 //sensorNumber is 1
                return getAngleOutVal( super.robot, targetObject  );//return angle                                
            
            //case 2:                                               //sensorNumber is 2
            //    return 1.0;                                       //return boolean "there is a targetObject"
            
            default:
                throw new RuntimeException(PhysicalObjectLocationSensor.class.getCanonicalName() + ": Invalid sensor number");
        }
        
        
        
    }

    
    /**
     * Gets the targetObject 
     * @return the targetObject
     * or null if there
     * is none
     */
    public PhysicalObject getTargetObject() {
        return targetObject;
    }

    
    /**
     * Sets the object being 
     * target by this sensor
     * @param targetObject the
     * object; can
     * be set to null
     */
    public void setTargetObject( PhysicalObject targetObject ) {
        this.targetObject = targetObject;
    }
    
    
    
    
    
    
    
    
    
    /**
     * Determines the output value
     * according to the distance
     * from the robot that owns
     * this sensor to the target 
     * object
     * @param targetObject the 
     * target object
     * @param robot the robot
     * that owns this sensor
     * @return the inverse of the
     * distance in the range ]0,1]
     */
    private double getDistanceOutVal( Robot robot, PhysicalObject targetObject ){
        
        double distance;                    //distance between robots
        distance = robot.getPosition().distanceTo( targetObject.getPosition() );
        
        distance = distance + 1.0;          //making sure distance >= 1

        return  1 / distance;               //return value is inverse of distance
    }

    
    /**
     * Determines the output value
     * according to the angle between
     * the robot that owns this
     * sensor orientation
     * and the position of the
     * target object
     * @param targetObject the 
     * target object
     * @param robot the robot
     * that owns this sensor
     * @return the angle in radians
     * in range [0,1]
     */
    private double getAngleOutVal( Robot robot, PhysicalObject targetObject ) {
        
        double robotOrientation;
        robotOrientation = robot.getOrientation();      //orientation in range [-PI,PI]
        
        
                                                        //vector representation
        Vector2d v = new Vector2d( robotOrientation );  //of recruited orientation
        
        
        double angle;                                           //angle between robots
        angle = v.signedAngle( targetObject.getPosition() );    //in range ]-Pi, PI]
        
        angle = angle + Math.PI;                             
        angle = angle / (2*Math.PI);                        //orientation in range [0,1]
        
        return angle;
        
    }
    
    
    
    
    
    
    
    
    
}
