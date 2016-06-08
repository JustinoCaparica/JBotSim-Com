/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.sensors;

import mathutils.Vector2d;
import simulation.Simulator;
import simulation.robot.Robot;
import simulation.robot.actuators.RecruitedActuator;
import simulation.util.Arguments;

/**
 * Sensor to perceive the distance
 * and the angle to the recruited robot
 * @author gus
 */
public class RecruitedSensor extends Sensor {

    
    private Robot recruited;                    //the recruited robot
    
    
    private Robot recruitRequester;             //a robot that has requested
                                                //a recruit but has not been
                                                //accepted as a recruiter yet
    
    
    
    
    /**
     * Initializes a new instance
     * @param simulator the simulator
     * @param id the id of the sensor
     * @param robot the robot that
     * owns this sensor
     * @param args the arguments
     */
    public RecruitedSensor(Simulator simulator, int id, Robot robot, Arguments args) {
        super(simulator, id, robot, args);
        
        recruited = null;
    }

    
    
    @Override
    public double getSensorReading( int sensorNumber ) {
        
        if ( recruited == null ) {              //there is no recruited
            return 0.0;                         //angle and distance are 0
        }
        
        
        
                                                            //sensorNumber == 0
        if ( sensorNumber == 0 ) {                          //return distance
            return getDistanceOutVal( recruited, super.robot );
        }
        else {                                              //assume sensorNumber == 1 
            return getAngleOutVal( recruited, super.robot );//return angle                                
        }
        
        
        
    }

    
    /**
     * Gets the recruited 
     * robot
     * @return the recruited
     * robot or null if there
     * is none
     */
    public Robot getRecruited() {
        return recruited;
    }

    
    /**
     * Sets the recruited
     * robot
     * @param recruited the
     * recruited robot; can
     * be set to null
     */
    public void setRecruited( Robot recruited ) {
        this.recruited = recruited;
    }
    
    
    
    
    
    
    
    
    
    /**
     * Determines the output value
     * according to distance
     * from recruiter to recruited
     * @param recruiter the recruiter
     * @param recruited the recruited
     * @return the inverse of the
     * distance in the range ]0,1]
     */
    private double getDistanceOutVal( Robot recruited, Robot recruiter ){
        
        double distance;                    //distance between robots
        distance = recruited.getPosition().distanceTo( recruiter.getPosition() );
        
        distance = distance + 1.0;          //making sure distance >= 1

        return  1 / distance;               //return value is inverse of distance
    }

    
    /**
     * Determines the output value
     * according to the angle between
     * the recruited orientation
     * and the position of the
     * recruiter
     * @param recruited the recruited 
     * robot
     * @param recruiter the recruiter
     * robot
     * @return the angle in radians
     * in range [0,1]
     */
    private double getAngleOutVal( Robot recruited, Robot recruiter ) {
        
        double recruitedOrientation;
        recruitedOrientation = recruited.getOrientation();  //orientation in range [-PI,PI]
        
        
                                                            //vector representation
        Vector2d v = new Vector2d( recruitedOrientation );  //of recruited orientation
        
        
        double angle;                                       //angle between robots
        angle = v.getSignedAngle( recruiter.getPosition() );//in range [-PI, PI]  
        angle += Math.PI;                                   
        angle /= Math.PI;                                   //orientation in range [0,1]
        
        
        return angle;
        
    }
    
    
    
    
    
    
    
    
    
}
