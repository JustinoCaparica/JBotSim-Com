/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolutionaryrobotics.neuralnetworks.outputs;

import simulation.robot.actuators.RecruitmentActuator;
import simulation.robot.actuators.RecruiterActuator;
import simulation.util.Arguments;

/**
 * Sets the state of the RecruitmentActuator
 according to the Neural Network Output
 * @author gus
 */
public class RecruitmentNNOutput extends NNOutput{

    private RecruitmentActuator actuator;      //the actuator that is
                                                //controled by this NNOutput
    
    private double nnOutputValue;               //the neural network output
                                                //value
    
    
    public RecruitmentNNOutput( RecruitmentActuator actuator, Arguments args ) {
        super(actuator, args);
        this.actuator = actuator;
    }

    @Override
    public int getNumberOfOutputValues() {
        return 1;
    }

    @Override
    public void setValue( int index, double value ) {
        this.nnOutputValue = value;
    }

    @Override
    public void apply() {
        
        if ( this.nnOutputValue < 0.3333 ) {   
            actuator.setRecruiting( true );
            actuator.setBeRecruited( false );
        }else if ( this.nnOutputValue < 0.6666 ) {
            actuator.setRecruiting( false );
            actuator.setBeRecruited( false );
        }else {
            actuator.setRecruiting( false );
            actuator.setBeRecruited( true );
        }
            
    }
    
    
    
}
