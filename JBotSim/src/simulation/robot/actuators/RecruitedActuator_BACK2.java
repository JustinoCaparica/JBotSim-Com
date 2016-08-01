/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.actuators;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;
import simulation.Simulator;
import simulation.robot.Robot;
import simulation.robot.messenger.message.Message;
import simulation.robot.messenger.message.MessageType;
import simulation.robot.sensors.RobotSensor;
import simulation.util.Arguments;
import simulation.util.ArgumentsAnnotation;

/**
 * Actuator that accepts recruitment
 * requests from other robots
 * @author gus
 */
public class RecruitedActuator_BACK2 extends Actuator {

    
    
    private Robot recruiter;                    //the recruiter
    private Set<Robot> recruitRequesters;       //robots requesting a recruit
    
    
    @ArgumentsAnnotation(name="range", defaultValue="0.8", help="A robot can not accept recruitment requests from another robot that is further than this range.")
    private final static double RANGE_DEFAULT = 0.8;
    private double range;                   //the range of the actuator:
                                            //how far may a robot be to 
                                            //be possible for me to accept his
                                            //recruitment request?
    
    private RobotSensor robotsSensor;       //perceives neighboring robots
    
    
    private final Message msg;                  //message to accept 
                                                //recruitment requests 
    
    private boolean recruitedState;             //recruited state of the robot
                                                //if true the robot is recruitedState
    
    
    private static Color recruitedColor;        //robot color when recruited
    
    
    
    
    /**
     * Initializes a new instance
     * @param simulator the simulator
     * @param id the actuator id
     * @param args the arguments
     */
    public RecruitedActuator_BACK2( Simulator simulator, int id, Arguments args ) {
        
        super(simulator, id, args);
        
        msg     = new Message( MessageType.FOCUS_ACCEPTED );
        
        recruiter       = null;
        recruitRequesters= new HashSet<>();
        
        recruitedState  = false;
        
        range = args.getArgumentAsDoubleOrSetDefault("range", RANGE_DEFAULT);
        
        recruitedColor = Color.GREEN;
        
    }

    
    /**
     * Clears the recruit requesters
     * collection. The collection
     * becomes empty.
     */
    public void clearRecruitRequesters(){
        recruitRequesters.clear();
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
        
        
        
        //note that we allways send recruitment msgs to keep the recruitment 
        //relationship alive
        
        
        //if recruiterActuator is going to recruit
        //then this robot can not be recruited
        RecruiterActuator recruiterActuator;
        recruiterActuator = (RecruiterActuator) robot.getActuatorByType( RecruiterActuator.class );
        
        
        
        
        robotsSensor = (RobotSensor)robot.getSensorByType( RobotSensor.class );
        
        
        
        if ( !recruitedState                            //the NN decided not to be recruited
                || (recruiterActuator != null && recruiterActuator.isRecruiting()) ) { //or the recruiter actuator is trying to recruit
            
            recruiter = null;                           //clear recruiter
            recruitRequesters.clear();                  //and recruit requester
            
            robot.setBodyColor( Color.BLACK );
            
            if ( robotsSensor != null ) {               //set the robots sensor
                robotsSensor.setTarget( null );         //to target all neighbors
            }
            
            return;                                     //.. and all is done
        }
            
        
                                                        //otherwise,
                                                        //the NN decided to be recruited
                                   
                                                        
        boolean found = false;
        if ( recruiter != null && 
                recruitRequesters.contains( recruiter ) ) {//there is a current recruiter
           found = true;
        }
        else{                                       //there is no current recruiter
            if ( !recruitRequesters.isEmpty() ) {   //but there are recruit requester
                                                    //promote any requester to recruiter
                recruiter = recruitRequesters.iterator().next();
                found = true;
            }
            else{                                   //and there are no recruit requesters
                recruiter = null;
                found = false;
            }
        }
                                                        
                              
        if ( found ) {                              //there is no recruiter nor recruit requester
                                                    //and no message is sent
                                          
            if ( recruiter.getDistanceBetween( robot.getPosition() ) <= range ) {   //recruiter within range
                recruiter.getMsgBox().addMsgToInbox( msg, robot );
                                                            //inform the recruiter
                                                            
                if ( robotsSensor != null ) {           //set the robots sensor
                    robotsSensor.setTarget( recruiter );//to target the recruiter
                }
                                                            
                robot.setBodyColor( recruitedColor );
            }
            else{                                                                  //recruiter outside range
                robot.setBodyColor( Color.BLACK );
                recruiter = null;                       //clear recruiter
                recruitRequesters.clear();              //and recruitment requester
                
                if ( robotsSensor != null ) {           //set the robots sensor
                    robotsSensor.setTarget( null );     //to all neighbors
                }
            }
            
        }
        else{
            
        }
                                                        
                                                        
                                                        
                                                        
                                                        
                                                        
//                                                
//        boolean found = false;                          //was a recruiter found?
//                                                        //no, not so far, but let's see..
//        
//        
//                                                        
//                                                        
//                                                        
//                                                        
//        if ( recruiter != null ) {                      //there is a recruiter
//            found = true;
//        }                                                       
//        else{                                           
//            if ( recruitRequesters != null ) {           //there is a recruit requester
//                recruiter = recruitRequesters;
//                recruitRequesters = null;                //promote the recruit requester
//                found = true;
//            }
//        }
//         
//        robot.setBodyColor( Color.BLACK );
//        
//        
//                                                    //at this point, if found == false
//        if ( found ) {                              //there is no recruiter nor recruit requester
//                                                    //and no message is sent
//                                          
//            if ( recruiter.getDistanceBetween( robot.getPosition() ) <= range ) {   //recruiter within range
//                recruiter.getMsgBox().addMsgToInbox( msg, robot );
//                                                            //inform the recruiter
//                                                            
//                if ( robotsSensor != null ) {           //set the robots sensor
//                    robotsSensor.setTarget( recruiter );//to target the recruiter
//                }
//                                                            
//                robot.setBodyColor( recruitedColor );
//            }else{                                                                  //recruiter outside range
//                robot.setBodyColor( Color.BLACK );
//                recruiter = null;                       //clear recruiter
//                recruitRequesters = null;                //and recruitment requester
//                
//                if ( robotsSensor != null ) {           //set the robots sensor
//                    robotsSensor.setTarget( null );     //to all neighbors
//                }
//            }
//            
//        }
        
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

    
    
    /**
     * Adds a recruit requester
     * @param requester the 
     * recruit requester
     */
    public void addRecruitRequester( Robot requester ) {
        recruitRequesters.add( requester );
    }
    
}
