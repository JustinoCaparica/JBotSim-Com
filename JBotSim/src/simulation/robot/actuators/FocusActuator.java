/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.actuators;

import simulation.Simulator;
import simulation.robot.Robot;
import simulation.robot.sensors.MessengerSocialSensor;
import simulation.util.Arguments;

/**
 * Acts on MessengerSocialSensor
 * to focus or defocus
 * @author gus
 */
public class FocusActuator extends Actuator {
    
    
    private MessengerSocialSensor msgSocialSensor;  //the social sensor
                                                    //that will be controlled
                                                    //by this actuator
    
    private Boolean focused;                        //when true the
                                                    //actuator will
                                                    //focus the robot

    
    /**
     * Initializes a new instance
     * @param simulator the simulator
     * @param id the actuator's id
     * @param args arguments
     */
    public FocusActuator( Simulator simulator, int id, Arguments args ) {
        
        super(simulator, id, args);
        
        this.msgSocialSensor = null;
        focused = false;
        
    }

    
    /**
     * Gets the state of
     * the actuator
     * @return true if
     * the actuator is
     * set to focus, false
     * otherwise
     */
    public Boolean getFocused() {
        return focused;
    }

    
    
    /**
     * Focus or defocus the
     * actuator
     * @param focused set
     * to true to focus 
     * or to false to
     * defocus
     */
    public void setFocused( Boolean focused ) {
        this.focused = focused;
    }

    
    
    
    
    @Override
    public void apply( Robot robot, double timeDelta ) {
        
        if( msgSocialSensor == null ){
            msgSocialSensor = (MessengerSocialSensor)robot.getSensorByType( MessengerSocialSensor.class );
        }
        
        msgSocialSensor.setFocus( focused );
        
    }
    
    
    
    
    
    
    
    
    
}
