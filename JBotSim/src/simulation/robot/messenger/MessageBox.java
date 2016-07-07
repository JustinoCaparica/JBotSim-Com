/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.messenger;

import simulation.robot.messenger.message.parser.MessageParser;
import java.util.LinkedList;
import java.util.List;
import simulation.robot.Robot;
import simulation.robot.actuators.RecruitedActuator;
import simulation.robot.actuators.RecruiterActuator;
import simulation.robot.messenger.message.Message;
import simulation.robot.messenger.message.MessageEnvelope;
import simulation.robot.messenger.message.MessageType;
import simulation.robot.sensors.RecruiterSensor;

/**
 * Offers an inbox and an outbox message queues
 * as well as a message parser
 * @author gus
 */
public class MessageBox {
    
    
    
    private MessageParser parser;                       //parses and interprets
                                                        //messages
    
    
    
    private List<MessageEnvelope> inbox;                //messages received
                                                        //in the last cycle
    
    
    private int messagesCount;                        //number of total
                                                        //messages received
    
    
    
    /**
     * Initializes a new instance
     * with a message parser
     * @param msgParser the 
     * message parser
     */
    public MessageBox( MessageParser msgParser ){
        
        inbox  = new LinkedList<>();
        
        parser = msgParser;
        
        messagesCount = 0;
        
    }
    
    
    
    
    
    
    
    
    /**
     * Clear the messages inbox.
     * The inbox will be empty
     * after this method returns
     */
    public void clearInbox(){
        inbox.clear();
    }
    
    
    
    
    
    
    /**
     * Adds a message to the end
     * of the inbox of this robot
     * @param msg the message
     * @param emitter the 
     * emitter robot
     */
    public void addMsgToInbox( Message msg, Robot emitter ){
        
        inbox.add( new MessageEnvelope( msg, emitter ) );
        
        messagesCount++;
        
    }
    
    
    
    
    
    
    
    /**
     * Gets the next message
     * from the inbox queue
     * @return a message envelope
     * containing the message
     * or null if the queue is empty
     */
    public MessageEnvelope getNextInboxMsg(){
        
        return getNextMsg( inbox );
        
    }
    
    
    
    
    /**
     * Gets the next message from the
     * message queue and removes the
     * message from the queue
     * @param queue the message queue
     * @return the message envelope 
     * that contains the message
     * or null if the queue is empty
     */
    private MessageEnvelope getNextMsg( List<MessageEnvelope> queue ){
        
        try{
            return queue.remove( 0 );           //return the 1st msg from the queue
        }
        catch( IndexOutOfBoundsException ex ){  //there is no 1st msg
            return null;                        //thus, return null
        }
        
    }

    
    /**
     * Processes the inbox messages 
     * @param receiver the robot that
     * has received the messages
     */
    public void processMessages( Robot receiver ) {
        
        ((RecruitedActuator)receiver.getActuatorByType( RecruitedActuator.class )).clearRecruitRequesters();
        ((RecruiterActuator)receiver.getActuatorByType( RecruiterActuator.class )).clearRecruitAccepters();
        
        
        MessageEnvelope envelope;
        
        for ( int i = 0; i < inbox.size(); i++ ) {
            
            envelope = getNextInboxMsg();               //get next message
                                                        //and remove from inbox
            parser.parse( receiver, envelope.getEmitter(), envelope.getMsg() );
            
        }
        
        
    }

    
    /**
     * Gets the number of total 
     * messages received
     */
    public int receivedMsgsCount() {
        return messagesCount;
    }

    
    
    
    
    
    
}
