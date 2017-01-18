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

    
                 
    
    private Double value;                    //the value that this actuator
                                            //stores, representing the role
    
    
    private Double lastValue;                //variable to store the value
                                            //from the last time step
    
    
    public RoleActuator(Simulator simulator, int id, Arguments args) {
        super(simulator, id, args);
        
        value = 0.0;
        lastValue = 0.0;
    }

    @Override
    public void apply( Robot robot, double timeDelta ) {
        
        //meh.. do nothing
        
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
        
        lastValue = this.value;             //store current value in a variable
        
        this.value = value;                 //replace current value 
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
