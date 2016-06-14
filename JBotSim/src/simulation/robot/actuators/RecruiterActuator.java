/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.actuators;

import simulation.Simulator;
import simulation.robot.Robot;
import simulation.robot.messenger.message.Message;
import simulation.robot.messenger.message.MessageType;
import simulation.util.Arguments;
import simulation.util.ArgumentsAnnotation;

/**
 * Actuator that recruits other robots
 * @author gus
 */
public class RecruiterActuator extends Actuator {

    //TODO get this parameter from the configuration file
    
    @ArgumentsAnnotation(name="range", defaultValue="0.8", help="The actuator can not recruit a robot that is further than this range.")
    private final static double RANGE_DEFAULT = 0.8;
    private double range;                   //the range of the actuator:
                                            //how far may a robot be to 
                                            //be possible for me to recruit him?
    
    
    private Robot recruit;                  //recruited robots
    
    private Message msg;                    //message to recruit robots
    
    private boolean recruiting;             //is the robot recruiting?
    
    
    
    
    /**
     * Initializes a new instance
     * @param simulator the simulator
     * @param id the actuator id
     * @param args the arguments
     */
    public RecruiterActuator( Simulator simulator, int id, Arguments args ) {
        super(simulator, id, args);
        
        msg = new Message( MessageType.REQUEST_FOCUS );
        
        recruiting = false;
        
        range = args.getArgumentAsDoubleOrSetDefault("range", RANGE_DEFAULT);
    }

    
    
    /**
     * Sets a recruit
     * @param recruit the 
     * new recruit
     */
    public void setRecruit( Robot recruit ){
        this.recruit = recruit;
    }

    
    /**
     * Gets the recruited robot
     * @return the recruit or
     * null if no robot is under 
     * recruitment
     */
    public Robot getRecruit(){
        return recruit;
    }
    
    
    
    
    /**
     * Determines if the robot
     * is recruiting
     * @return true if the
     * robot is recruiting, 
     * false otherwise
     */
    public boolean isRecruiting() {
        return recruiting;
    }

    /**
     * Sets the recruiting state 
     * of the robot
     * @param recruiting if true 
     * the robot is recruiting, if
     * false the robot is not
     * recruiting
     */
    public void setRecruiting( boolean recruiting ) {
        this.recruiting = recruiting;
    }
    
    
    
    
    
    
    
    
    @Override
    public void apply( Robot robot, double timeDelta ) {
        
        if ( !recruiting ) {                    //the robot is not recruiting
            return;                             //do nothing
        }
        
                                                //otherwise, the robot is either
                                                //recruiting or has a recruit
                 
        
        if ( recruit != null ) {                                    //there is a recruit
            recruit.getMsgBox().addMsgToInbox( msg, robot );        //send the recruit 
                                                                    //a message
        }
        else{                                                       //there is no recruit
            robot.broadcastMessage( msg, range );                   //broadcast recruitment
                                                                    //message
        }
            
        //we allways send recruitment msgs to already 
        //recruited robots to keep the recruitment 
        //relationship alive
    }

    
    
    

    
    
}
