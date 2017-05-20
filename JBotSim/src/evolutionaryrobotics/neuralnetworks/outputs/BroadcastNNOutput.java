/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolutionaryrobotics.neuralnetworks.outputs;

import simulation.robot.actuators.Actuator;
import simulation.robot.actuators.BroadcastActuator;
import simulation.util.Arguments;

/**
 * The NNOutput that controls the BroadcastActuator
 * @author guest
 */
public class BroadcastNNOutput extends NNOutput {

    
    private final BroadcastActuator act;        //the actuator controlled 
                                                //by this NNOutput
    
    private Double outputValue;                 //the neuron's output value

    
    
    
    public BroadcastNNOutput(Actuator actuator, Arguments args) {
        super(actuator, args);
        
        outputValue = 0.0;
        this.act = (BroadcastActuator) actuator;
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
