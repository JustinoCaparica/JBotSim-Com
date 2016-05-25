/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolutionaryrobotics.neuralnetworks.outputs;

import simulation.robot.actuators.messenger.MessageType;
import simulation.robot.actuators.messenger.MessengerActuator;
import simulation.util.Arguments;

/**
 * Manipulates the MessengerActuator
 * according to the neural network output
 * @author gus
 */
public class MessengerNNOutput extends NNOutput {

    private MessengerActuator msgActuator;     //the actuator 
    
    
    /**
     * Initializes a new instance
     * @param msgActuator the 
     * actuator
     * @param args the arguments 
     */
    public MessengerNNOutput(   MessengerActuator msgActuator, 
                                Arguments args ) {
        
        super(msgActuator, args);
        
        this.msgActuator = msgActuator;
        
    }

    
    
    
    @Override
    public int getNumberOfOutputValues() {
        return 1;                   //number of output neurons
                                    //that are added to the NN
                                    //for the sake of this 
                                    //actuator only
    }

    
    
    
    @Override
    public void setValue( int index, double value ) {
        
        msgActuator.setValue( value );          //send the NN output value
                                                //to the msg actuator
    }

    
    
    @Override
    public void apply() {
        
        //the usefullness of this method is not clear
        //it is empty in other classes of ..NNOutput
        
    }
    
}
