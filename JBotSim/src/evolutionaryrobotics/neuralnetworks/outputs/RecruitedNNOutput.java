/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolutionaryrobotics.neuralnetworks.outputs;

import simulation.robot.actuators.RecruitedActuator;
import simulation.robot.actuators.RecruiterActuator;
import simulation.util.Arguments;

/**
 * Controls the RecruitedActuator
 * according to the Neural Network Output
 * @author gus
 */
public class RecruitedNNOutput extends NNOutput{

    private RecruitedActuator actuator;         //the actuator that is
                                                //controled by this NNOutput
    
    private double nnOutputValue;               //the neural network output
                                                //value
    
    
    public RecruitedNNOutput( RecruitedActuator actuator, Arguments args ) {
        super(actuator, args);
        this.actuator = ( RecruitedActuator ) actuator;
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
            actuator.setRecruitedState( true );     //set the robot to recruited
            
        }
        else{
            actuator.setRecruitedState( false );    //set the robot to not recruited
        }
            
    }
    
}
