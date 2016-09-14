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
 * and the angle to the recruiter robot
 * @author gus
 */
public class RecruiterSensor extends Sensor {

    
    
    
    private Robot recruiter;                    //the recruiter robot
    
    
    
    
    /**
     * Initializes a new instance
     * @param simulator the simulator
     * @param id the id of the sensor
     * @param robot the robot that
     * owns this sensor
     * @param args the arguments
     */
    public RecruiterSensor(Simulator simulator, int id, Robot robot, Arguments args) {
        super(simulator, id, robot, args);
        
        recruiter        = null;
    }

    
    
    @Override
    public double getSensorReading( int sensorNumber ) {
        
        if ( recruiter == null ) {              //there is no recruiter                                                
            return 0.0;                         //angle is 0, distance is 0
        }
        
        
        switch ( sensorNumber ) {
            
            case 0:                                                 //sensorNumber == 0                                            
                return getDistanceOutVal(super.robot, recruiter);   //return distance
            
            case 1:                                                 //sensorNumber == 1
                return getAngleOutVal(super.robot, recruiter);      //return angle                                
            
                
            default:
                throw new RuntimeException( "Invalid sensor number in RecruiterSensor" );
        }
        
        
        
    }
    
    
    
    
    
    /**
     * Determines the output value
     * according to distance
     * from recruiter to recruited
     * @param recruiter the recruiter
     * @param recruit the recruit
     * @return the inverse of the
     * distance in the range ]0,1]
     */
    private double getDistanceOutVal( Robot recruit, Robot recruiter ){
        
        double distance;                    //distance between robots
        distance = recruit.getPosition().distanceTo( recruiter.getPosition() );

        distance = distance + 1.0;          //making sure distance >= 1
                                            //to avoid distance = 0

        return  1 / distance;               //returned value is inverse of distance
    }

    
    /**
     * Determines the output value
     * according to the angle between
     * the recruited orientation
     * and the position of the
     * recruiter
     * @param recruit the recruited 
     * robot
     * @param recruiter the recruiter
     * robot
     * @return the angle in radians
     * in range [0,1]
     */
    private double getAngleOutVal( Robot recruit, Robot recruiter ) {
        
        double recruitOrientation;
        recruitOrientation = recruit.getOrientation();      //orientation in range [-PI,PI]
       
                                                            //vector representation
        Vector2d v = new Vector2d( recruitOrientation );    //of recruit orientation
        
        
        double angle;                                       //angle between robots
        angle = v.signedAngle( recruiter.getPosition() );   //in range ]-PI, PI]  
        angle = angle + Math.PI;                             
        angle = angle / (2*Math.PI);                        //orientation in range [0,1]
        
        return angle;
        
    }

    
    /**
     * Gets the recruiter robot
     * @return the recruiter 
     * robot or null if there
     * is not any recruiter
     * robot
     */
    public Robot getRecruiter() {
        return recruiter;
    }

    
    /**
     * Sets the recruiter
     * @param recruiter 
     * the recruiter robot; set 
     * this value to null to
     * represent no recruiter
     */
    public void setRecruiter (Robot recruiter ) {
        this.recruiter = recruiter;
    }
    
    
    
    
    
    
    
    
    
}
