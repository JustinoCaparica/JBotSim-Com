/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.sensors;

import mathutils.Vector2d;
import simulation.Simulator;
import simulation.robot.Robot;
import simulation.util.Arguments;

/**
 * Sensor to perceive the distance
 * and the angle to the recruited robot
 * @author gus
 */
public class RecruitedSensor extends Sensor {

    
    private Robot recruit;                      //the recruited robot
    
    
    
    
    
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
        
        recruit = null;
    }

    
    
    @Override
    public double getSensorReading( int sensorNumber ) {
        
        if ( recruit == null ) {                //there is no recruit
            return 0.0;                         //angle and distance are 0
        }
        
        
        
                                                            //sensorNumber == 0
        if ( sensorNumber == 0 ) {                          //return distance
            return getDistanceOutVal( recruit, super.robot );
        }
        else {                                              //assume sensorNumber == 1 
            return getAngleOutVal( recruit, super.robot );  //return angle                                
        }
        
        
        
    }

    
    /**
     * Gets the recruit 
     * @return the recruit
     * or null if there
     * is none
     */
    public Robot getRecruit() {
        return recruit;
    }

    
    /**
     * Sets the recruit
     * robot
     * @param recruit the
     * recruited robot; can
     * be set to null
     */
    public void setRecruit( Robot recruit ) {
        this.recruit = recruit;
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
        angle = v.signedAngle( recruiter.getPosition() );   //in range ]-Pi, PI]
        
        angle = angle + Math.PI;                             
        angle = angle / (2*Math.PI);                        //orientation in range [0,1]
        
        return angle;
        
    }
    
    
    
    
    
    
    
    
    
}
