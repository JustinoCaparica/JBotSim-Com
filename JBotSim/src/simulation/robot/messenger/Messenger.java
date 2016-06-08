/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.messenger;

import simulation.robot.messenger.message.parser.MessageParser;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import simulation.robot.Robot;
import simulation.robot.messenger.message.Message;
import simulation.robot.messenger.message.parser.SocialMessageParser;

/**
 * Sends and receives messages.
 * Parses received messages.
 * @author gus
 */
public class Messenger {
    
    
    
    private MessageParser parser;                       //parses and interprets
                                                        //messages
    
    
    
    
    private Set<Robot> robotsInRange;                   //robots within
                                                        //messenger's range
                                                        
    
    
    private HashMap<Robot, Message> outbox;             //messages to send
                                                        //in the next cycle
    
    
    private HashMap< Robot, Message> inbox;             //messages received
                                                        //in the last cycle
    
    
    
    
    /**
     * Initializes a new instance
     * with a message parser
     * @param msgParser the 
     * message parser
     */
    public Messenger( MessageParser msgParser ){
        
        robotsInRange = new HashSet<>();
        
        outbox = new HashMap<>();
        inbox  = new HashMap<>();
        
        parser = msgParser;
        
    }
    
    
    
    
    
    
    
    /**
     * Adds a robot to the
     * collection of robots in range
     * @param robotInRange the 
     * robot to be added
     */
    public void addRobotInRange( Robot robotInRange ) {
        
        robotsInRange.add( robotInRange );
    
    }
    
    
    /**
     * Clears the collection of
     * robots in range. The 
     * collection will be empty 
     * after this call returns.
     */
    public void clearRobotsInRange(){
        
        robotsInRange.clear();
        
    }
    
    
    
    /**
     * Clear the collection of
     * messages that will be
     * sent in the next cycle
     */
    public void clearMessages(){
        outbox.clear();
    }
    
    
    
    /**
     * Sets a message to be 
     * sent to a given robot 
     * within range.
     * If a message already exists
     * for the recipient robot, 
     * the existing message is
     * replaced by the new message
     * @param msg the message
     * @param recipientRobot the 
     * recipient robot
     */
    public void setMessage( Message msg, Robot recipientRobot ){
        outbox.put( recipientRobot, msg );
    }
    
    
    
    /**
     * Set a message to be 
     * broadcast to all robots 
     * within range. All previously
     * set messages are deleted
     * and will not be sent
     * @param msg the message
     */
    public void setBroadcastMessage( Message msg ){
        
        robotsInRange.stream().forEach( (recipientRobot) -> {
            outbox.put( recipientRobot, msg );
        });
        
    }
    
    
    
    
    
    
    
    
    
}
