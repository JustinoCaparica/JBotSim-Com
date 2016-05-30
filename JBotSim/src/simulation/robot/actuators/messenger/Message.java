/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.actuators.messenger;

/**
 * A typified message to be exchanged 
 * between robots.
 * @author gus
 */
public class Message {
    
    
    private MessageType msgType;    //message type
    
    
    
    /**
     * Initializes a new instance
     * with a given type and content
     * @param msgType the message
     * type
     * @param content the content
     */
    public Message(MessageType msgType) {
        this.msgType = msgType;
    }

    /**
     * Gets the message type
     * @return the message 
     * type
     */
    public MessageType getMsgType() {
        return msgType;
    }

    
    /**
     * Sets the message type
     * @param msgType the 
     * message type
     */
    public void setMsgType(MessageType msgType) {
        this.msgType = msgType;
    }

    
    
    
    
    
    
    
}
