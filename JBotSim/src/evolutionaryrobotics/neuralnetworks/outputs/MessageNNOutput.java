/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolutionaryrobotics.neuralnetworks.outputs;

import simulation.robot.messenger.message.MessageType;
import simulation.robot.messenger.garbage.MessageActuator;
import simulation.util.Arguments;

/**
 * Manipulates the MessageActuator
 * according to the neural network output
 * @author gus
 */
public class MessageNNOutput extends NNOutput {

    private MessageActuator msgActuator;      //the actuator 
    
    
    
    private double nnOutputValue;               //NN output value
    
    
    /**
     * Initializes a new instance
     * @param msgActuator the 
     * actuator
     * @param args the arguments 
     */
    public MessageNNOutput( MessageActuator msgActuator, Arguments args ) {
        
        super(msgActuator, args);
        
        this.msgActuator = msgActuator;
        
    }

    
    
    
    @Override
    public int getNumberOfOutputValues() {
        return 1;                   //number of output neurons
                                    //that are added to the NN
                                    //for the sake of this 
                                    //actuator alone
    }

    
    
    
    @Override
    public void setValue( int index, double value ) {
        
        this.nnOutputValue = value;
        
    }

    
    
    @Override
    public void apply() {
        
        msgActuator.setValue( nnOutputValue );  //send the NN output value
                                                //to the msg actuator
        
    }
    
}
