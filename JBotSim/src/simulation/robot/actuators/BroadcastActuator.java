/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.actuators;

import simulation.Simulator;
import simulation.robot.Robot;
import simulation.util.Arguments;
import simulation.util.ArgumentsAnnotation;

/**
 * An Actuator that broadcasts a 
 * decimal number.
 * @author guest
 */
public class BroadcastActuator extends Actuator {

    
    private Double output;                  //the actuator output
    
    
    
    
    private static final Double MIN_VALUE = 0.0;
    @ArgumentsAnnotation(name = "minValue", defaultValue = "0.0", help="minimum value output")
    protected Double minValue;
    
    
    private static final Double MAX_VALUE = 1.0;
    @ArgumentsAnnotation(name = "maxValue", defaultValue = "1.0", help="maximum value output")
    protected Double maxValue;
    
    
    
    
    /**
     * Initializes a new BroadcastActuator
     * @param simulator the simulator where 
     * the experiments are run
     * @param id actuator id
     * @param args arguments for the actuator
     */
    public BroadcastActuator(Simulator simulator, int id, Arguments args) {
        super(simulator, id, args);
        
        
        minValue = args.getArgumentAsDoubleOrSetDefault("minValue", MIN_VALUE);
        maxValue = args.getArgumentAsDoubleOrSetDefault("maxValue", MAX_VALUE);
       
        output = 0.0;
        
    }

    
    
    
    
    
    
    
    
    
    @Override
    public void apply(Robot robot, double timeDelta) {
        ;
    }
    
    
    
    /**
     * Gets the value that this 
     * actuator is broadcasting
     * @return the value being 
     * broadcasted
     */
    public Double getValue() {
        return output;
    }

    
    /**
     * Sets the value to be broadcast
     * @param value the new broadcasted 
     * value
     */
    public void setValue( Double value ) {
        output = value;
    }
    
    
    
    
    
}
