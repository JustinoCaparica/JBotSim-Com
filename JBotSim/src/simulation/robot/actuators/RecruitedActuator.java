/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.actuators;

import simulation.Simulator;
import simulation.robot.Robot;
import simulation.robot.messenger.message.Message;
import simulation.robot.messenger.garbage.MessageActuator;
import simulation.robot.messenger.message.MessageType;
import simulation.util.Arguments;

/**
 * Actuator that accepts recruitment
 * requests from other robots
 * @author gus
 */
public class RecruitedActuator extends Actuator {

    
    private Robot recruiter;               //recruiter robot
    
    private Message msg;                    //message accept recruitment
                                            //requests 
    
    
    
    /**
     * Initializes a new instance
     * @param simulator the simulator
     * @param id the actuator id
     * @param args the arguments
     */
    public RecruitedActuator( Simulator simulator, int id, Arguments args ) {
        super(simulator, id, args);
        
        msg = new Message( MessageType.FOCUS_ACCEPTED );
        
        recruiter = null;
        
    }

    
    /**
     * Clears the recruited robot.
     * After this method finishes
     * there will be no robots 
     * recruiting this robot
     */
    public void clearRecruiter(){
        recruiter = null;
    }
    
    
    /**
     * Set a recruiter
     * @param recruiter the 
     * recruiter robot
     */
    public void setRecruiter( Robot recruiter ){
        this.recruiter = recruiter;
    }

    
    /**
     * Determines if the robot
     * is recruited
     * @return true if the
     * robot is recruited, 
     * false otherwise
     */
    public boolean isRecruited() {
        return recruiter != null;
    }

    
    
    
    
    
    
    
    @Override
    public void apply( Robot robot, double timeDelta ) {
        
        if ( recruiter == null ) {              //the robot is not recruited
            return;                             //do nothing
        }
        
                                                //the robot is recruited
        MessageActuator msgAct;                 //get the message actuator
        msgAct = ( MessageActuator )robot.getActuatorByType( MessageActuator.class );
        
        msgAct.setMessage( msg, recruiter );    //send the recruiter a message
                                                //we allways send recruitment 
                                                //msgs to keep the recruitment 
                                                //relationship alive
    }

    
    
    
    /**
     * Gets the robot that owns 
     * this actuator and stores
     * @param simulator the 
     * simulator
     * @param id the id of this 
     * actuator
     * @return the robot
     * that owns this actuator
     * or null if no robot
     * owns this actuator
     */
    private Robot getActuatorOwner( Simulator simulator, int id ) {
        
        for ( Robot robot : simulator.getRobots() ) {   //
            if( robot.getActuatorWithId(id) != null ){  //robot owns the actuator
                return robot;
            }
        }
        
        return null;
    }

    
    
    /**
     * Gets the recruiter robot
     * @return the robot
     * that has recruited this
     * robot or null if no robot
     * has recruited this robot
     */
    public Robot getRecruiter() {
        return recruiter;
    }
    
}
