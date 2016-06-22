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
 * and the angle to the recruiter robot
 * @author gus
 */
public class RecruiterSensor extends Sensor {

    
    
    
    private Robot recruiter;                    //the recruiter robot
    
    
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
    public RecruiterSensor(Simulator simulator, int id, Robot robot, Arguments args) {
        super(simulator, id, robot, args);
        
        recruiter        = null;
        recruitRequester = null;
    }

    
    
    @Override
    public double getSensorReading( int sensorNumber ) {
        
        if ( recruiter == null &&               //there is no recruiter
             recruitRequester == null ) {       //neither recruit requester
            return 0.0;                         //angle is 0, distance is 0, there is recruiter/recruit requester is 0
        }
        
        
        Robot r;
        if ( recruiter != null ) {              //there is already a recruiter    
            r = recruiter;                      //select the recruiter
        }
        else{                                   //there is only a recruit requester
            r = recruitRequester;               //select the recruit requester
        }
        
        
        
        
        switch ( sensorNumber ) {
            
            case 0:                                         //sensorNumber == 0                                            
                return getDistanceOutVal(super.robot, r);   //return distance
            
            case 1:                                         //sensorNumber == 1
                return getAngleOutVal(super.robot, r);      //return angle                                
            
            case 2:                                         //sensorNumber == 2
                return 1.0;                                 //return boolean "there is a recruiter/ recruit requester"
                
            default:
                throw new RuntimeException( "Invalid sensor number in RecruiterSensor" );
        }
        
        
        
    }
    
    
    
    /**
     * Sets the recruit requester. Set 
     * this value to null to have none
     * recruit requester
     * @param recruitRequester the
     * recruit requester
     */
    public void setRecruitRequester( Robot recruitRequester ) {
        this.recruitRequester = recruitRequester;
    }

    
    /**
     * Gets the recruit requester
     * @return the recruit requester
     * of null if there is no
     * recruit requester
     */
    public Robot getRecruitRequester() {
        return this.recruitRequester;
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

        return  1 / distance;               //return value is inverse of distance
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
     * the recruiter robot
     */
    public void setRecruiter (Robot recruiter ) {
        this.recruiter = recruiter;
    }
    
    
    
    
    
    
    
    
    
}
