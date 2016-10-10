/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.sensors;

import simulation.Simulator;
import simulation.robot.Robot;
import simulation.util.Arguments;

/**
 * A sensor to perceive the recruiter
 * @author gus
 */
public class RecruiterSensorCones extends RobotSensor {
    
    public RecruiterSensorCones(Simulator simulator, int id, Robot robot, Arguments args) {
        super(simulator, id, robot, args);
    }
    
    
    /**
     * Sets the recruiter that the 
     * sensor should perceive
     * @param recruiter the recruiter
     */
    public void setRecruiter( Robot recruiter ){
        super.setTarget( recruiter );
    }
    
    
    
}
