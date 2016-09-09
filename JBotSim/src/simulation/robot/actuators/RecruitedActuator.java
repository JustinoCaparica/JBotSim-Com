/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.actuators;

import java.awt.Color;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import simulation.Simulator;
import simulation.robot.Robot;
import simulation.robot.messenger.message.Message;
import simulation.robot.messenger.message.MessageType;
import simulation.robot.sensors.RecruitSensor;
import simulation.robot.sensors.RecruiterSensor;
import simulation.robot.sensors.RobotSensor;
import simulation.util.Arguments;
import simulation.util.ArgumentsAnnotation;

/**
 * Actuator that accepts recruitment
 * requests from other robots
 * @author gus
 */
public class RecruitedActuator extends Actuator {

    
    
    
    private Robot recruiter;                    //the recruiter
    private List<Robot> recruitRequesters;     //robots requesting a recruit
    
    
    @ArgumentsAnnotation(name="range", defaultValue="0.8", help="A robot can not accept recruitment requests from another robot that is further than this range.")
    private final static double RANGE_DEFAULT = 0.8;
    private double range;                   //the range of the actuator:
                                            //how far may a robot be to 
                                            //be possible for me to accept his
                                            //recruitment request?
    
    
    private RobotSensor robotSensor;        //perceives other robots
    private RecruiterSensor recruiterSensor;//perceives the recruiter
                                            //these variables are created here
                                            //to avoid creating them everytime
                                            //apply() is called
    
    
    
    
    
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
        
        recruiter       = null;
        recruitRequesters= new LinkedList<>();
        
        recruitedState  = false;
        
        range = args.getArgumentAsDoubleOrSetDefault("range", RANGE_DEFAULT);
        
        
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
               
        if ( !robot.isEnabled() ) {                 //disabled robot
            return;                                 //does not act
        }
        
        RecruiterActuator recruiterAct;
        recruiterAct = (RecruiterActuator) robot.getActuatorByType( RecruiterActuator.class );
        
        if ( !recruitedState ||                     //NN decided not to be recruited
            recruiterAct.getRecruit() != null ) {   //or there is a recruit
            notRecruited( robot );
        }
        else{
            recruited( robot );
        }
        
        recruitRequesters.clear();
        
        
        
//        if ( robot.getId() == 0 ) {
//            System.out.print("Robot's 0 recruiter is robot");
//            if ( recruiter == null ) {
//                System.out.println(" null");
//            }else{
//                System.out.println( recruiter.getId() );
//            }
//        }
        
        
    }

    
    
    /**
     * Implements the actuator's behavior
     * when not recruited
     * @param robot the robot that owns
     * the actuator
     */
    private void notRecruited( Robot robot ) {
         
        recruiter = null;
         
        
        
        
        robotSensor = (RobotSensor) robot.getSensorByType( RobotSensor.class );
        
        if ( robotSensor != null ) {        //set the robot sensor 
            robotSensor.setTarget( null );  //to perceive all neighbor robots
        }
         
        
        
        recruiterSensor = ( RecruiterSensor ) robot.getSensorByType( RecruiterSensor.class );
        recruiterSensor.setRecruiter( null );   //tell the recruiter sensor
                                                //that there is no recruiter
        
    }
    
    
    

    
    /**
     * Implements the actuator's behavior
     * when recruited
     * @param robot the robot that owns
     * the actuator
     */
    private void recruited( Robot robot ) {
        
        
        if ( recruiter == null ) {          //there is no current recruiter
                                            
            recruiter = chooseRecruiter(robot); //choose one recruiter from the requesters
        }
        else{                               //there is a current recruiter but..
                                            //the current recruiter is not a requester
                                            //or is beyond range
            
            if ( !recruitRequesters.contains( recruiter ) 
                 || recruiter.getPosition().distanceTo( robot.getPosition() ) > range  ) {
                
                recruiter = chooseRecruiter(robot);//choose new recruiter
            }
        }
        
        
        if ( recruiter != null ) {                  //a recruiter is available
                                                    //send the recruiter an
            recruiter.getMsgBox().addMsg( msg, robot );  //acceptance msg
        }
        
        
           
        robotSensor = (RobotSensor) robot.getSensorByType( RobotSensor.class );
        if ( robotSensor != null ) {            //if recruiter is null
            robotSensor.setTarget( recruiter ); // perceive all neighbor robots
                                                // otherwise perceive recruiter robot
        }


        recruiterSensor = ( RecruiterSensor ) robot.getSensorByType( RecruiterSensor.class );
        if ( recruiterSensor != null ) {
            recruiterSensor.setRecruiter( recruiter );  //tell the recruiter sensor
                                                        //who the recruiter is
        }
        
        
        
    }

    

    /**
     * Chooses a recruiter from the recruit
     * requesters. The recruiter is randomly
     * selected
     * @param robot the robot that owns 
     * the actuator
     * @return a recruiter or null
     * if there are no recruit requesters
     */
    private Robot chooseRecruiter( Robot robot ) {
        
        
        Robot r;
        Iterator<Robot> it;
        it = recruitRequesters.iterator();   
        
        while( it.hasNext() ) {                 //iterate all accepters
            r = it.next();
            if( r.getPosition().distanceTo( robot.getPosition() ) <= range ){
                return r;                       //return 1st accepter within range
            }
        }
        
        return null;                            //no accepters within range
                                                //or no accepters at all
        
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
