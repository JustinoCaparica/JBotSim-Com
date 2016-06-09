/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.messenger.message;

import simulation.robot.Robot;

/**
 * Contains a message with 
 * associated information
 * @author gus
 */
public class MessageEnvelope {
    
    
    
    private Message msg;                    //the message
        
    private Robot emitter;                  //message emitter 

    
    
    
    
    /**
     * Initializes a new instance
     * @param msg the message
     * @param emitter the emitter
     */
    public MessageEnvelope( Message msg, Robot emitter ) {
        this.msg = msg;
        this.emitter = emitter;
    }

    public Message getMsg() {
        return msg;
    }

    public void setMsg( Message msg ) {
        this.msg = msg;
    }

    
    /**
     * Gets the message emitter
     * @return the message 
     * emitter 
     */
    public Robot getEmitter() {
        return emitter;
    }

    
    /**
     * Sets the message emitter
     * @param emitter the 
     * message emitter 
     */
    public void setRecipient( Robot emitter ) {
        this.emitter = emitter;
    }
    
    
    
    
    
    
    
    
    
    
    
}
