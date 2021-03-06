/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolutionaryrobotics.neuralnetworks.outputs;

import simulation.robot.actuators.Actuator;
import simulation.robot.actuators.RoleActuator;
import simulation.util.Arguments;

/**
 * The NNOutput that controls the RoleActuator
 * @author guest
 */
public class RoleNNOutput extends NNOutput {

    
    private final RoleActuator act;             //the actuator controlled 
                                                //by this NNOutput
    
    private Double outputValue;                 //the neuron's output value

    
    
    
    public RoleNNOutput(Actuator actuator, Arguments args) {
        super(actuator, args);
        
        outputValue = 0.0;
        this.act = (RoleActuator) actuator;
    }
    
    
    
    
    @Override
    public int getNumberOfOutputValues() {
        return 1;
    }

    @Override
    public void setValue(int index, double value) {
        this.outputValue = value;
    }

    @Override
    public void apply() {
        act.setValue( outputValue );
    }
    
}
