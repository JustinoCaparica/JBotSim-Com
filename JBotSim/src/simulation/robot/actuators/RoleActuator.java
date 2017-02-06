/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.actuators;

import simulation.Simulator;
import simulation.robot.Robot;
import simulation.util.Arguments;

/**
 * An actuator that communicates the
 * role value to other robots
 * @author guest
 */
public class RoleActuator extends Actuator {

    
                 
    
    private Double value;                   //the value that this actuator
                                            //stores, representing the role
    private Double valueTemp;               //value set by the NN only 
                                            //available when apply is called
    
    
    
    private Double lastValue;               //variable to store the value
                                            //from the last time step
    private Double lastValueTemp;           //value set by the NN only 
                                            //available when apply is called
    
    
    
    private Robot robot;                    //robot that owns the actuator
    
    
    public RoleActuator(Simulator simulator, int id, Arguments args) {
        super(simulator, id, args);
        
        value       = 0.0;
        lastValue   = 0.0;
        
        
        valueTemp       = 0.0;
        lastValueTemp   = 0.0;
    }

    
    @Override
    public void apply( Robot robot, double timeDelta ) {
        
        this.robot = robot;
        
        lastValue = lastValueTemp;          //store current value in a variable
        
        this.value = valueTemp;             //replace current value 
                                            //for the new value
        
    }

    
    /**
     * Gets the value that this actuator
     * stores, representing the role
     * @return the stored value,
     * representing the role
     */
    public Double getValue() {
        return value;
    }

    
    /**
     * Sets the value that this actuator
     * stores, representing the role. The 
     * current value is stored in a variable
     * and is replaced by the new value
     * @param value the new value
     */
    public void setValue( Double value ) {
        
        lastValueTemp = valueTemp;          //store current value in a variable
        
        valueTemp = value;                 //replace current value 
                                           //for the new value
        
    }

    
    /**
     * Gets the value that this actuator
     * stored in the previous time step
     * @return the value
     */
    public Double getLastValue() {
        return lastValue;
    }

    
    
    
    
    
    
}
