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
 * A sensor to perceive the recruit
 * @author gus
 */
public class RecruitConesSensor extends RobotSensor {
    
    public RecruitConesSensor(Simulator simulator, int id, Robot robot, Arguments args) {
        super(simulator, id, robot, args);
    }
    
    
    /**
     * Sets the recruit that the 
     * sensor should perceive
     * @param recruit the recruit
     */
    public void setRecruit( Robot recruit ){
        super.setTarget( recruit );
    }

    
    /**
     * Gets the recruit being
     * perceived
     * @return the recruit
     * or null if there is
     * none
     */
    public Robot getRecruit() {
        return super.getTarget();
    }
    
    
    
}
