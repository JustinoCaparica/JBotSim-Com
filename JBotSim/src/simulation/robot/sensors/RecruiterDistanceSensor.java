/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.sensors;

import simulation.Simulator;
import simulation.robot.Robot;
import simulation.robot.actuators.RecruitedActuator;
import simulation.util.Arguments;

/**
 * Sensor to perceive the distance from the robot that owns
 * the sensor (the recruited) to the robot requesting a recruit 
 * (the recruiter)
 * @author gus
 */
public class RecruiterDistanceSensor extends Sensor {

    private final RecruitedActuator actuator;   //actuator that 
                                                //accepts recruitment requests
                                                //this actuator knows
                                                //the recruiter robot
    
    private Robot recruitRequester;             //a robot that has requested
                                                //a recruit but has not been
                                                //accepted as a recruiter yet
    
    
    
    /**
     * Initializes a new instance
     * @param simulator the simulator
     * @param id the sensor id
     * @param robot the robot
     * that owns this sensor
     * @param args the arguments
     */
    public RecruiterDistanceSensor(Simulator simulator, int id, Robot robot, Arguments args) {
        super(simulator, id, robot, args);
        
        actuator = (RecruitedActuator) robot.getActuatorByType( RecruitedActuator.class );
        recruitRequester = null;
    }

    
    
    @Override
    public double getSensorReading( int sensorNumber ) {
        
        double distance = 0.0;                      //if there is no recruiter
                                                    //or recruitment requests
                                                    //the returned value is 0
        
                                                    
        Robot recruiter;                            //recruiter robot
        recruiter = actuator.getRecruiter();        
        
        
        if ( recruiter != null ) {                  //there is already a recruiter
            
            Robot recruited;                        //recruited robot
            recruited = super.robot;
            
            distance = determineDistanceOutputValue( recruiter, recruited );
            
        }
        else 
            if ( recruitRequester != null ) {       //there is no recruiter but
            Robot recruited;                        //a robot is trying to recruit me
            recruited = super.robot;                //recruited robot
            
            distance = determineDistanceOutputValue( recruitRequester, recruited );
            
        }
        
        return distance;
        
        
    }

    /**
     * Determines the output value
     * according to distance
     * from recruiter to recruited
     * @param recruiter the recruiter
     * @param recruited the recruited
     */
    private double determineDistanceOutputValue( Robot recruiter, Robot recruited ) {
        
        double distance;                    //distance between robots
        distance = recruited.getPosition().distanceTo( recruiter.getPosition() );
        
        distance = distance + 1.0;          //making sure distance >= 1

        return  1 / distance;               //return value is inverse of distance
        
    }

    
    /**
     * Sets the recruit requester. Set 
     * this value to null to have no
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
    
    
    
}
