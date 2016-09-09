/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.actuators;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import simulation.Simulator;
import simulation.robot.Robot;
import simulation.robot.messenger.message.Message;
import simulation.robot.messenger.message.MessageType;
import simulation.robot.sensors.RecruitSensor;
import simulation.robot.sensors.RobotSensor;
import simulation.util.Arguments;
import simulation.util.ArgumentsAnnotation;



/**
 * Actuator that recruits other robots
 * @author gus
 */
public class RecruiterActuator extends Actuator {

    //TODO get this parameter from the configuration file
    
    private final static double RANGE_DEFAULT = 0.8;
    @ArgumentsAnnotation(name="range", defaultValue="0.8", help="The actuator can not recruit a robot that is further than this range.")
    private final double range;             //the range of the actuator:
                                            //how far may a robot be to 
                                            //be possible for me to recruit him?
    
    
    private Robot recruit;                  //the recruit
    
    
    private List<Robot> recruitAccepters;   //robots that sent recruit 
                                            //acceptance messages
    
    
    private List< List<Robot> > receiversQueue;
    //private LinkedList<Robot> receivers;  //robots that received a
                                            //recruit request message
                                            //in the previous time step
    
    
    
    private RobotSensor robotSensor;        //perceives other robots
    private RecruitSensor recruitSensor;    //perceives the recruit
                                            //these variables are created here
                                            //to avoid creating them everytime
                                            //apply() is called
    
    
    private final Message msg;              //message to recruit robots
    
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
        
        
        recruitAccepters = new LinkedList<>();
        //receivers = new LinkedList<Robot>();
        
        receiversQueue = new LinkedList();
        receiversQueue.add( new LinkedList<Robot>() ); //no receivers in     
        receiversQueue.add( new LinkedList<Robot>() ); //the begining
     
                                                        
    }

    
    /**
     * Clears the recruit accepters
     * collection. The collection
     * becomes empty
     */
    public void clearRecruitAccepters(){
        recruitAccepters.clear();
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
          
        if ( !robot.isEnabled() ) {                 //disabled robot
            return;                                 //does not act
        }
        
        
        
        //receivers.clear();                //forget all receivers
                                            //from previous time step
        
                                            
                                            
                                            
        if ( !recruiting ) {                //NN decided not to recruit
            notRecruiting(robot);
        }
        else{                               //NN decided to recruit
            recruiting(robot);
        }
        
        recruitAccepters.clear();           //forget recruit accepters
        
        
        receiversQueue.remove(0);           //remove the head of 
                                            //the receivers queue
        
        
//        if ( robot.getId() == 0 ) {
//            System.out.print("Robot's 0 recruit is robot");
//            if ( recruit == null ) {
//                System.out.println(" null");
//            }else{
//                System.out.println( recruit.getId() );
//            }
//        }
        
        
    }

    
    
    /**
     * Implements the actuator's behavior
     * when not recruiting
     * @param robot the robot that owns
     * the actuator
     */
    private void notRecruiting( Robot robot ){
        
        recruit = null;                     //forget recruit
        
                                                       
        robotSensor = (RobotSensor) robot.getSensorByType( RobotSensor.class );
        
        if ( robotSensor != null ) {        //set the robot sensor 
            robotSensor.setTarget( null );  //to perceive all neighbor robots
        }
        
        
        recruitSensor = ( RecruitSensor ) robot.getSensorByType( RecruitSensor.class );
        recruitSensor.setRecruit( null );       //tell the recruit sensor
                                                //that there is no recruit
        
        receiversQueue.add( new LinkedList<Robot>() );     //no receivers
                                                        //because no message
                                                        //was sent
    }
    
    
    /**
     * Implements the actuator's behavior
     * when recruiting
     * @param robot the robot that owns
     * the actuator
     */
    private void recruiting( Robot robot ) {
        
        
        
        if ( recruit == null ) {            //there is no current recruit
                                            
            recruit = chooseRecruit(robot); //choose one recruit from the accepters
        }
        else{                               //there is a current recruit but..
                                            //the current recruit is not an accepter
                                            //or is beyond range
            
                                            
            //TODO is this if ok?                                
            if ( !recruitAccepters.contains( recruit ) 
                 || recruit.getPosition().distanceTo( robot.getPosition() ) > range  ) {
                
                recruit = chooseRecruit(robot);  //choose new recruit
                
            }
        }
        
        
        
        if ( recruit == null ) {                    //no recruit is available
                                                    //broadcast recruitment msg
            //receivers = robot.broadcastMessage( msg, range );
            receiversQueue.add( robot.broadcastMessage( msg, range ) );
        }
        else{                                       //a recruit is available
                                                    //send the recruit a
            recruit.getMsgBox().addMsg( msg, robot );  //recruitment msg
            //receivers.add( recruit );
            List<Robot> receivers = new LinkedList<>();
            receivers.add( recruit );
            receiversQueue.add( receivers );
        }
        
        
        
        
        
        
        robotSensor = ( RobotSensor ) robot.getSensorByType( RobotSensor.class );
        if ( robotSensor != null ) {            // if recruit is null
            robotSensor.setTarget( recruit );   // perceive all neighbor robots
                                                // otherwise perceive recruit robot
        }
        
        
        
        recruitSensor = ( RecruitSensor ) robot.getSensorByType( RecruitSensor.class );
        if ( recruitSensor != null ) {
            recruitSensor.setRecruit( recruit );    //tell the recruit sensor
                                                    //who the recruit is
        }
    
    }
    
    
    
    
    
    
    /**
     * Gets the robots that received 
     * a recruitment request in the
     * the time step before the 
     * previous time step
     * @return 
     */
    public List<Robot> getPreviousReceivers() {
        return receiversQueue.get( 0 );
    }

    
    
    
    
    /**
     * Adds a recruit accepter to
     * the collection of recruit
     * accepters
     * @param recruitAccepter 
     */
    public void addRecruitAccepter( Robot recruitAccepter ) {
        recruitAccepters.add( recruitAccepter );
    }

    
    /**
     * Chooses a recruit from the recruit
     * accepters. The recruit is randomly
     * selected
     * @param robot the robot that owns 
     * the actuator
     * @return a recruit or null
     * if there are no recruit accepters
     * or all the recruit accepters moved
     * out of range
     */
    private Robot chooseRecruit(Robot robot) {
        
        Robot accepter;
        Iterator<Robot> it;
        it = recruitAccepters.iterator();   
        
//        Map<Double, Robot> receiversWithinRange;
//        receiversWithinRange = new HashMap<>();
//        
//        Double minDistance = 0.0;
        
        while( it.hasNext() ) {                                 //iterate over accepters
            accepter = it.next();
            
            if ( receiversQueue.get(0).contains( accepter ) ) { //accepter is at head
                                                                //of receivers queue.
                                                                //head of queue contains
                                                                //receivers of the 
                                                                //time step prior
                                                                //to the last
                                                                
                if( accepter.getPosition().distanceTo( robot.getPosition() ) <= range ){
                                                                //AND accepter within range
                    return accepter;                            //return accepter
                }
            }
        }        
                
                
                
//        while( it.hasNext() ) {                 //iterate all accepters
//            accepter = it.next();
//            if( accepter.getPosition().distanceTo( robot.getPosition() ) <= range ){
//                return accepter;                       //return 1st accepter within range
//            }
//        }
        
        return null;                            //no accepters within range
                                                //or no accepters at all
        
    }



    

    
    
}
