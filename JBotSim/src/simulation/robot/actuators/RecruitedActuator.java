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
import simulation.robot.sensors.RecruiterSensor;
import simulation.util.Arguments;
import simulation.util.ArgumentsAnnotation;

/**
 * Actuator that accepts recruitment
 * requests from other robots
 * @author gus
 */
public class RecruitedActuator extends Actuator {

    
    private RecruiterSensor recruiterSensor;    //sensor that knows
                                                //the recruiter
    
    private Robot recruiter;                    //the recruiter
    
    
    @ArgumentsAnnotation(name="range", defaultValue="0.8", help="The actuator can not accept recruitment requests from a robot that is further than this range.")
    private final static double RANGE_DEFAULT = 0.8;
    private double range;                   //the range of the actuator:
                                            //how far may a robot be to 
                                            //be possible for me to accept his
                                            //recruitment request?
    
    
    
    
    private final Message msg;                  //message to accept 
                                                //recruitment requests 
    
    private boolean recruitedState;             //recruited state of the robot
                                                //if true the robot is recruitedState
    
    
    
    /**
     * Initializes a new instance
     * @param simulator the simulator
     * @param id the actuator id
     * @param args the arguments
     */
    public RecruitedActuator( Simulator simulator, int id, Arguments args ) {
        
        super(simulator, id, args);
        
        msg     = new Message( MessageType.FOCUS_ACCEPTED );
        
        recruiterSensor = null;
        recruiter       = null;
        recruitedState  = false;
        
        range = args.getArgumentAsDoubleOrSetDefault("range", RANGE_DEFAULT);
        
    }

    

    
    /**
     * Determines if the robot
     * is recruited
     * @return true if the
     * robot is recruited, 
     * false otherwise
     */
    public boolean isRecruited() {
        return recruitedState;
    }

    
    
    
    
    
    
    
    @Override
    public void apply( Robot robot, double timeDelta ) {
        
        
        recruiterSensor = (RecruiterSensor)robot.getSensorByType( RecruiterSensor.class );
        
        
        if ( !recruitedState ) {                        //robot is not recruited
            recruiterSensor.setRecruiter( null );       //clear recruiter
            recruiterSensor.setRecruitRequester( null );//and recruitment requester
            return;                                     
        }
                                                        //otherwise,
                                                        //the robot is recruited
                                          
                                                
        boolean found = false;                          //was a recruiter found?
        
        
        if ( recruiterSensor.getRecruiter() != null ) {             //there is a recruiter
            recruiter = recruiterSensor.getRecruiter();  
            found = true;
        }                                                       
        else{                                           
            if ( recruiterSensor.getRecruitRequester() != null ) {  //there is a recruit requester
                recruiter = recruiterSensor.getRecruitRequester();
                recruiterSensor.setRecruitRequester( null );        //promote the recruit requester
                recruiterSensor.setRecruiter( recruiter );          //to recruit
                found = true;
            }
        }
         
        
                                                    //at this point, if found == false
        if ( found ) {                              //there is no recruiter nor recruit requester
                                                    //and no message is sent
                                          
            if ( recruiter.getDistanceBetween( robot.getPosition() ) <= range ) {   //recruiter within range
                recruiter.getMsgBox().addMsgToInbox( msg, robot );
                                                            //inform the recruiter
                                                            //we allways send recruitment 
                                                            //msgs to keep the recruitment 
                                                            //relationship alive
            }
            
        }
        
    }

    
    
    
    /**
     * Gets the recruiter robot
     * @return the robot
     * that has recruitedState this
     * robot or null if no robot
     * has recruitedState this robot
     */
    public Robot getRecruiter() {
        return recruiter;
    }

    
    /**
     * Sets the recruitedState state 
     * of the robot
     * @param recruited  if set 
     * to true the robot becomes
     * recruited, if set to false
     * the robot becomes not
     * recruited
     */
    public void setRecruitedState( boolean recruited ) {
        this.recruitedState = recruited;
    }
    
}
