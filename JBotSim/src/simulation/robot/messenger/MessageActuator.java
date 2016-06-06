/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.messenger;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import simulation.Simulator;
import simulation.robot.Robot;
import simulation.robot.actuators.Actuator;
import simulation.robot.messenger.MessageSocialSensor;
import simulation.util.Arguments;

/**
 * Actuator to send messages
 * @author gus
 */
public class MessageActuator extends Actuator {
    
    
    
    private HashMap<Integer, List<Robot> > robotsInRange;   //robots within
                                                            //actuator's range
                                                            //<SensorID, Robots>
                                                            //SensorID: id of the
                                                            //sensor where the 
                                                            //robots were perceived
    
    
    private HashMap<Robot, Message> msgs;                   //messages to send
                                                            //in the next cycle
    
    
    
    
    
    
    
    
    /**
     * Initializes a new object
     * @param simulator the simulator
     * @param id the actuator id
     * @param args arguments 
     */
    public MessageActuator( Simulator simulator, int id, Arguments args ) {
        
        super(simulator, id, args);
        
        robotsInRange = new HashMap();
        
        msgs = new HashMap<>();
        
        
    }
    
    
    
    /**
     * Prepares the outgoing messages
     * for the next cycle, according
     * to the neural network output 
     * value
     * @param nnOutput the NN output
     * value in the range [0,1]
     */
    public void setValue( Double nnOutput ){
        
        Message outboundMsg = null;                 //message to send
        
        
        int slotsCount;                             //number of possible
        slotsCount = MessageType.values().length;   //message types
                                                    //each message matchs
                                                    //a slot
        
        
        Double slotSize = 1.0 / slotsCount;         //slot size
        
        
        boolean found = false;                      //was the message type/slot
                                                    //found yet?
        
        double slotTopLimit;                        //top limit value 
                                                    //of the current slot
        
                                                    
        for ( int slotID = 0; slotID < slotsCount && !found; slotID++ ) {
                                                    //search the possible slots
                                                    //to determine the slot
                                                    //where the NN output fits
            slotTopLimit = slotID * slotSize + slotSize;
            
            if ( nnOutput <= slotTopLimit ) {                   
                outboundMsg = new Message( MessageType.values()[slotID] );
                found = true;
            }
            
        }
        
        clearMessages();
        setBroadcastMessage( outboundMsg );
        
    }
    
    
    
    
    /**
     * Adds a robot to the
     * collection of robots in range
     * @param robotInRange the 
     * robot to be added
     * @param sensorId the
     * sensor id where the
     * robot was perceived
     */
    public void addRobotInRange( Robot robotInRange, int sensorId ) {
        
        if ( robotsInRange.get( sensorId ) == null ) {          //if theres no
            robotsInRange.put( sensorId, new LinkedList<>() );  //mapping for the
        }                                                       //sensor, create it
        
        robotsInRange.get( sensorId ).add( robotInRange );      //map the robot 
                                                                //to the sensor
    
    }
    
    
    /**
     * Clears the collection of
     * robots in range. The 
     * list will be empty 
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
        msgs.clear();
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
        msgs.put( recipientRobot, msg );
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
        
        Collection< List<Robot> > listsOfRobots;
        listsOfRobots = ( Collection< List<Robot> > ) robotsInRange.values();
        
        for ( List<Robot> listOfRobots : listsOfRobots ) {
            for ( Robot recipientRobot : listOfRobots ) {
                msgs.put( recipientRobot, msg );
            }
        }
        
    }
    
    
    
    @Override
    public void apply( Robot robot, double timeDelta ) {
        
        MessageSocialSensor msgSensor;
        
        List<Robot> robots;
        Set<Integer> sensorsID = robotsInRange.keySet();
        
        for ( Integer sensorID : sensorsID ) {            
            
            robots = robotsInRange.get(sensorID);       //get the robots in range
            for ( Robot robotInRange : robots ) {       //of each individual actuator
                
                                                        //get the recipient robot
                                                        //messages sensor
                msgSensor = (MessageSocialSensor) robotInRange.getSensorByType(MessageSocialSensor.class);
                
                                                        //if there's a msg for the
                if ( msgs.get(robotInRange) != null )   //recipient robot, send the msg
                    msgSensor.setMessage( robot, msgs.get(robotInRange) );
            }
        }
        
        clearMessages();                                //empty the outbox
    }

    
    
    
    
    
    
    
    
}
