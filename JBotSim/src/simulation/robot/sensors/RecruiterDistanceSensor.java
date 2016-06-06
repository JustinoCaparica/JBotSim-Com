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
    }

    
    
    @Override
    public double getSensorReading( int sensorNumber ) {
        
        
        Robot recruiter;                        //recruiter robot
        recruiter = actuator.getRecruiter();        
        
        if ( recruiter == null ) {              //if there is no recruiter,
            return 0.0;                         //sensor reading is 0
        }
        
        
        Robot recruited;                        //recruited robot
        recruited = super.robot;
        
        double distance;                        //distance from recruiter to recruited
        distance = recruited.getPosition().distanceTo( recruiter.getPosition() );
        
        
        distance = distance + 1.0;              //making sure distance >= 1
                                                    
        return 1 / distance;                    //return inverse of distance
        
    }
    
    
    
}
