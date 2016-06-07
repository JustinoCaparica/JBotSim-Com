/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.actuators;

import java.util.List;
import simulation.Simulator;
import simulation.robot.Robot;
import simulation.robot.messenger.message.Message;
import simulation.robot.messenger.garbage.MessageActuator;
import simulation.robot.messenger.message.MessageType;
import simulation.util.Arguments;

/**
 * Actuator that recruits other robots
 * @author gus
 */
public class RecruiterActuator extends Actuator {

    
    private List<Robot> recruited;          //recruited robots
    
    private Message msg;                    //message to recruit robots
    
    
    private boolean recruiting;             //is the robot recruiting?
    
    
    /**
     * Initializes a new instance
     * @param simulator the simulator
     * @param id the actuator id
     * @param args the arguments
     */
    public RecruiterActuator(Simulator simulator, int id, Arguments args) {
        super(simulator, id, args);
        
        msg = new Message( MessageType.REQUEST_FOCUS );
        
        recruiting = false;
    }

    
    /**
     * Clears the recruited robots.
     * After this method finishes
     * there will be no robots 
     * under recruitment
     */
    public void clearRecruited(){
        recruited.clear();
    }
    
    
    /**
     * Adds a recruit
     * @param recruit the 
     * new recruit
     */
    public void addRecruit( Robot recruit ){
        recruited.add( recruit );
    }

    
    /**
     * Gets the recruited robot
     * @return the recruit or
     * null if no robot is under 
     * recruitment
     */
    public Robot getRecruit(){
        
        if ( recruited.isEmpty() ) {
            return null;
        }
        
        return recruited.get( 0 );
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
        
                                                //the robot is recruiting
        MessageActuator msgAct;                 //get the message actuator
        msgAct = ( MessageActuator )robot.getActuatorByType( MessageActuator.class );
        
        
        if( recruited.isEmpty() ){              //none robot recruited
            msgAct.setBroadcastMessage( msg );  //broadcast recruitment msg
        }
        else{                                   //some robot(s) recruited
            sendMsgToRecruits( msg );           //send recruited robots a recruitment msg
        }                                       //we allways send recruitment msgs to already 
                                                //recruited robots to keep the recruitment 
                                                //relationship alive
    }

    
    
    /**
     * Sends a recruitment message
     * to all recruited robots
     * @param msg 
     */
    private void sendMsgToRecruits( Message msg ) {
        
        MessageActuator msgAct;                             //the message actuator
        
        for ( Robot robot : recruited ) {                   //send ALL recruited robots
            msgAct = ( MessageActuator )robot.getActuatorByType( MessageActuator.class );
            msgAct.setMessage( msg, robot );                //a message
        }
        
    }

    
    
}
