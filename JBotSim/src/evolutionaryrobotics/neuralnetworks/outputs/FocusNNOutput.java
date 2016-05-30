/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolutionaryrobotics.neuralnetworks.outputs;

import simulation.robot.actuators.FocusActuator;
import simulation.util.Arguments;

/**
 * Controls the FocusActuator according
 * to the neural network output value
 * @author gus
 */
public class FocusNNOutput extends NNOutput {

    private FocusActuator focusActuator;            //actuator controlled
                                                    //by this FocusNNOutput
    
    
    private Double nnOutput;                        //NN output value
    
    
    /**
     * Initializes a new instance
     * with an Actuator and arguments
     * @param focusActuator the 
     * actuator of type FocusActuator
     * @param args the arguments
     */
    public FocusNNOutput( FocusActuator focusActuator, Arguments args ) {
        
        super(focusActuator, args);

        this.focusActuator = focusActuator;
    
    }

    
    @Override
    public int getNumberOfOutputValues() {
        return 1;
    }

    @Override
    public void setValue( int index, double value ) {
        
        nnOutput = value;                           //store the nn output value
        
    }

    
    @Override
    public void apply() {
        
        focusActuator.setFocused( nnOutput > 0.5 ); //set to focused 
                                                    //if NN output value > 0.5
                                                    //set to defocused otherwise
        
    }
    
}
