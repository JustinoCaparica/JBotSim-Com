/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.actuators;

import simulation.Simulator;
import simulation.util.Arguments;

/**
 * An Actuator to broadcast a message
 * relative to Prey proximity.  This class
 * exists only to be easier to call
 * this sensor in the experiments config file
 * @author guest
 */
public class BroadcastPreyActuator extends BroadcastActuator{
    
    
    public BroadcastPreyActuator(Simulator simulator, int id, Arguments args) {
        super(simulator, id, args);
    }
    
    
}
