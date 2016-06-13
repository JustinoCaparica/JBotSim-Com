/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolutionaryrobotics.neuralnetworks.outputs;

import simulation.robot.actuators.RecruiterActuator;
import simulation.util.Arguments;

/**
 * Controls the RecruiterActuator
 * according to the Neural Network Output
 * @author gus
 */
public class RecruiterNNOutput extends NNOutput{

    private RecruiterActuator actuator;         //the actuator that is
                                                //controled by this NNOutput
    
    private double nnOutputValue;               //the neural network output
                                                //value
    
    
    public RecruiterNNOutput( RecruiterActuator actuator, Arguments args ) {
        super(actuator, args);
        this.actuator = ( RecruiterActuator ) actuator;
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
        
        if ( this.nnOutputValue > 0.5 ) {   
            actuator.setRecruiting( true );     //set the robot to recruiting
        }
        else{
            actuator.setRecruiting( false );    //set the robot to not recruiting
        }
            
    }
    
}
