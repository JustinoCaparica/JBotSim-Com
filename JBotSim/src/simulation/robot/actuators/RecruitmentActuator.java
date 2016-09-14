/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.actuators;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import simulation.Simulator;
import simulation.robot.Robot;
import simulation.robot.messenger.message.Message;
import simulation.robot.messenger.message.MessageType;
import simulation.robot.sensors.RecruiterSensor;
import simulation.util.Arguments;
import simulation.util.ArgumentsAnnotation;



/**
 * Actuator that recruits or accepts to
 * be recruited by other robots. This actuator 
 * can not recruit and be recruited at the same
 * time. To recruit another robot takes
 * precedence over accepting to be recruited
 * by another robot
 * @author gus
 */
public class RecruitmentActuator extends Actuator {

    
    private final static double RANGE_DEFAULT = 0.8;
    @ArgumentsAnnotation(name="range", defaultValue="0.8", help="The actuator can not recruit a robot that is further than this range.")
    private final double range;             //the range of the actuator:
                                            //how far may a robot be to 
                                            //be possible for me to recruit him?
    
    
    private Robot recruiter;                //the recruiter
    private List<Robot> requesters;         //list of recruit requesters
    
    
    private RecruiterSensor recruiterSensor;//sensor to perceive the recruiter
    
    
    
    
    private final Message msgREQ;           //message to recruit robots
    
    
    
    private boolean recruiting;             //is the robot recruiting?
    private boolean beRecruited;            //is the robot accepting 
                                            //recruitment requests?
    
    
    
    /**
     * Initializes a new instance
     * @param simulator the simulator
     * @param id the actuator id
     * @param args the arguments
     */
    public RecruitmentActuator( Simulator simulator, int id, Arguments args ) {
        super(simulator, id, args);
        
        msgREQ = new Message( MessageType.REQUEST_FOCUS );
        
        recruiting = false;
        beRecruited = false;
        
        range = args.getArgumentAsDoubleOrSetDefault("range", RANGE_DEFAULT);
        
        requesters = new LinkedList<>();
        
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
    
    
    
    /**
     * Sets the robot's availability
     * to be recruited
     * @param beRecruited if true 
     * the robot will accept to be 
     * recruited if a recruitment
     * request is received
     */
    public void setBeRecruited( boolean beRecruited ) {
        this.beRecruited = beRecruited;
    }
    
    
    /**
     * Checks if the robot is available
     * to be recruited
     * @return true if the robot
     * is available to be recruited,
     * false otherwise
     */
    public boolean isBeingRecruited(){
        return beRecruited;
    }
    
    
    
    @Override
    public void apply( Robot robot, double timeDelta ) {
          
        if ( !robot.isEnabled() ) {                 //disabled robot
            return;                                 //does not act
        }
        
                      
                                            
        if ( !recruiting ) {                //NN decided not to recruit
            
            notRecruiting( robot );
            
            if ( beRecruited ) {            
                beRecruited( robot );       //NN decided not to recruit but be recruited
            }else{
                notBeRecruited( robot );    //NN decided not to recruit and not to be recruited
            }
        
        }
        else{                               //NN decided to recruit and not be recruited
            recruiting(robot);
            notBeRecruited( robot );
        }
        
        
        requesters.clear();                 //forget recruit requesters
                                            //this list is filled by the 
                                            //message parser when the robot 
                                            //is updating sensors and
                                            //processing messages
        
        
    }

    
    
    /**
     * Implements the actuator's behavior
     * when not recruiting
     * @param robot the robot that owns
     * the actuator
     */
    private void notRecruiting( Robot robot ){
        //do nothing?
    }
    
    
    /**
     * Implements the actuator's behavior
     * when recruiting
     * @param robot the robot that owns
     * the actuator
     */
    private void recruiting( Robot robot ) {
        
        robot.broadcastMessage( msgREQ, range );
     
    }
    
    
    
         

    
    /**
     * Implements the behavior when
     * the robot is not accepting to be 
     * recruited
     * @param robot the robot that 
     * owns this actuator
     */
    private void notBeRecruited( Robot robot ) {
        
        recruiterSensor = (RecruiterSensor) robot.getSensorByType( RecruiterSensor.class );
        if ( recruiterSensor != null ) {
            recruiterSensor.setRecruiter( null );
        }
        
    }

    
    /**
     * Implements the behavior when
     * the robot is accepting to be
     * recruited
     * @param robot the robot that 
     * owns the actuator
     */
    private void beRecruited( Robot robot ) {
        
        
        recruiterSensor = (RecruiterSensor) robot.getSensorByType( RecruiterSensor.class );
        if ( recruiterSensor != null ) {            
            recruiterSensor.setRecruiter( null );       //reset recruiter sensor
        }                                               //and let the code
                                                        //ahead find a recruiter
                                                        //(which can be the current recruiter)
                                                        
                                                        
        if ( recruiter != null && 
             requesters.contains( recruiter ) ) {           //current recruiter is a
            if ( recruiterSensor != null ) {
                recruiterSensor.setRecruiter( recruiter );  //recruit requester
            }
        }
        else{
            
            recruiter = null;                           
            
            
            Robot requester;
            Iterator<Robot> it;
            it = requesters.iterator();  

            while( it.hasNext() && recruiter == null ){
                
                requester = it.next();
                
                if ( requester.getPosition().distanceTo( robot.getPosition() ) < range ) {
                    recruiter = requester;                      //requester in range
                    if ( recruiterSensor != null ) {            //found
                        recruiterSensor.setRecruiter( recruiter );  
                    }
                }
                
            }
            
        }
        
    }

    
    /**
     * Adds a recruit requester to the
     * collection of recruit requesters
     * @param robot the requester
     */
    public void addRecruitRequester( Robot robot ) {
        requesters.add( robot );
    }



    

    
    
}
