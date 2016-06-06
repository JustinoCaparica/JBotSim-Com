/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.sensors;

import simulation.Simulator;
import simulation.robot.Robot;
import simulation.robot.actuators.RecruitedActuator;
import simulation.robot.actuators.RecruiterActuator;
import simulation.util.Arguments;

/**
 * Sensor to perceive the distance from the robot that owns
 * the sensor (the recruiter) to the recruit robot 
 * (the recruited)
 * @author gus
 */
public class RecruitedDistanceSensor extends Sensor {

    private final RecruiterActuator actuator;   //actuator that sends
                                                //recruitment requests
    
    
    /**
     * Initializes a new instance
     * @param simulator the simulator
     * @param id the sensor id
     * @param robot the robot
     * that owns this sensor
     * @param args the arguments
     */
    public RecruitedDistanceSensor(Simulator simulator, int id, Robot robot, Arguments args) {
        super(simulator, id, robot, args);
        
        actuator = (RecruiterActuator) robot.getActuatorByType( RecruiterActuator.class );
    }

    
    
    @Override
    public double getSensorReading( int sensorNumber ) {
        
        
        Robot recruited;                        //recruited robot
        recruited = actuator.getRecruiter();        
        
        if ( recruited == null ) {              //if there is no recruited,
            return 0.0;                         //sensor reading is 0
        }
        
        
        Robot recruiter;                        //recruiter robot
        recruiter = super.robot;
        
        double distance;                        //distance from recruiter to recruited
        distance = recruiter.getPosition().distanceTo( recruited.getPosition() );
        
        
        distance = distance + 1.0;              //making sure distance >= 1
                                                    
        return 1 / distance;                    //return inverse of distance
        
    }
    
    
    
}
