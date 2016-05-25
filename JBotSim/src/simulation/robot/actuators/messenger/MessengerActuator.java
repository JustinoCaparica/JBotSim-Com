/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.actuators.messenger;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import simulation.Simulator;
import simulation.robot.Robot;
import simulation.robot.actuators.Actuator;
import simulation.robot.sensors.MessengerSensor;
import simulation.util.Arguments;

/**
 * An array of MessengerActuator 
 * actuators placed around the 
 * body of a robot, evenly spaced.
 * Each actuator is identified by 
 * its position in the array
 * @author gus
 */
public class MessengerActuator extends Actuator {
    
    
    
    private HashMap<Integer, List<Robot> > robotsInRange;   //robots within
                                                            //actuator's range
                                                            //<SensorID, Robots>
                                                            //SensorID: id of the
                                                            //sensor where the 
                                                            //robots were perceived
    
    
    private HashMap<Robot, Message> msgs;                   //messages to send
                                                            //in the next cycle
    
    
    
    
    private final MessageType possibleMsgs;                 //possible message
                                                            //types
                                                            
    
    
    
    
    
    /**
     * Initializes a new object
     * @param simulator the simulator
     * @param id the actuator id
     * @param args arguments (TODO: describe arguments)
     * @param numOfSensors number 
     * of individual sensors
     * @param possibleMessages possible
     * message types <MessageTypeID, Message>
     */
    public MessengerActuator( Simulator simulator, int id, Arguments args,
                                    int numOfSensors,
                                    MessageType possibleMessages ) {
        
        super(simulator, id, args);
        
        robotsInRange = new HashMap();
        
        msgs = new HashMap<>();
        
        this.possibleMsgs = possibleMessages;
        
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
        
        Message outboundMsg;                        //message to send
        
        
        int slotsCount;                             //number of possible
        slotsCount = MessageType.values().length;   //message types/slots
        
        
        Double slotSize = 1.0 / slotsCount;         //slot size
        
        
        boolean found = false;                      //was the message type found yet?
        double val;                                 //top limit value 
                                                    //of the current slot
        
                                                    
        for ( int slotID = 0; slotID < slotsCount && !found; slotID++ ) {
                                                    //search the possible slots
                                                    //to determine the slot
                                                    //where the NN output fits
            val = slotID * slotSize + slotSize;
            
            if ( nnOutput <= val ) {                   
                outboundMsg = new Message( MessageType.values()[slotID], null);
                found = true;
            }
            
        }
        
        
        
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
        
        robotsInRange.get( id ).add( robotInRange );
    
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
     * If a message already existed
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
     * within range. 
     * @param msg the message
     */
    public void setBroadcastMessage( Message msg ){
        
        List< List<Robot> > listsOfRobots;
        listsOfRobots = ( List< List<Robot> > ) robotsInRange.values();
        
        for (List<Robot> listOfRobot : listsOfRobots) {
            for (Robot recipientRobot : listOfRobot) {
                msgs.put( recipientRobot, msg );
            }
        }
        
    }
    
    
    
    @Override
    public void apply( Robot robot, double timeDelta ) {
        
        MessengerSensor msgSensor;
        
        List<Robot> robots;
        Set<Integer> sensorsID = robotsInRange.keySet();
        
        for ( Integer sensorID : sensorsID ) {            
            
            robots = robotsInRange.get(sensorID);       //get the robots in range
            for (Robot robotInRange : robots) {         //of each individual actuator
                
                                                        //get the recipient robot
                                                        //messages sensor
                msgSensor = (MessengerSensor) robotInRange.getSensorByType(MessengerSensor.class);
                
                                                        //if there's a msg for the
                if ( msgs.get(robotInRange) != null )   //recipient robot, send the msg
                    msgSensor.setMessage( robot, msgs.get(robotInRange) );
            }
        }
    }

   
    
    
    
    
    
    
    
}
