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
 * Sensor to perceive the focus state
 * of the robot
 * @author gus
 */
public class FocusSensor extends Sensor {

    private RecruitedActuator actuator;             //actuator that accepts 
                                                    //recruitment requests
    
    
    public FocusSensor(Simulator simulator, int id, Robot robot, Arguments args) {
        super(simulator, id, robot, args);
        
        actuator = (RecruitedActuator) robot.getActuatorByType( RecruitedActuator.class );
    }

    
    @Override
    public double getSensorReading( int sensorNumber ) {
        return actuator.isRecruited()? 1.0 : 0.0;   //if focused return 1, 
                                                    //otherwise return 0
    }
    
}
