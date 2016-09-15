/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.actuators;

import java.util.List;
import simulation.Simulator;
import simulation.environment.Environment;
import simulation.robot.Robot;
import simulation.robot.sensors.RecruitSensor;
import simulation.robot.sensors.RecruiterSensor;
import simulation.util.Arguments;
import simulation.util.ArgumentsAnnotation;



/**
 * Actuator that recruits or accepts to
 * be recruited by other robots. This actuator 
 * can not recruit and be recruited at the same
 * time. To recruit another robot takes
 * precedence over accepting to be recruited
 * by another robot. Recruitment is immediate,
 * that is, when recruiting, the actuator
 * searches for another robot that is available 
 * to be recruited and recruits the
 * available robot immediately without
 * sending any message
 * @author gus
 */
public class RecruitmentActuatorImmediate extends Actuator {

    
    private final static double RANGE_DEFAULT = 0.8;
    @ArgumentsAnnotation(name="range", defaultValue="0.8", help="The actuator can not recruit a robot that is further than this range.")
    private final double range;             //the range of the actuator:
                                            //how far may a robot be to 
                                            //be possible for me to recruit him?
    
    
    /***************************************************/
    /** Convenience references declared here to avoid **/
    /** creating them every time a method is called   **/
    private Robot recruiter;                
    private Robot recruit;                  
    private RecruiterSensor recruiterSensor;
    private RecruitSensor recruitSensor;    
    /***************************************************/
     
    
    
    private boolean recruiting;             //is the robot recruiting?
    private boolean beRecruited;            //is the robot accepting 
                                            //recruitment requests?
    
    
    private final Environment env;          //the environment
    
    
    /**
     * Initializes a new instance
     * @param simulator the simulator
     * @param id the actuator id
     * @param args the arguments
     */
    public RecruitmentActuatorImmediate( Simulator simulator, int id, Arguments args ) {
        super(simulator, id, args);
        
        
        recruiting = false;
        beRecruited = false;
        
        range = args.getArgumentAsDoubleOrSetDefault("range", RANGE_DEFAULT);
        
        env = simulator.getEnvironment();
        
    }

    
    
    
    

    
    
    
    
    
    
    
    
    
    @Override
    public void apply( Robot robot, double timeDelta ) {
          
        
        if ( !robot.isEnabled() ) {                 //disabled robot
            notRecruiting( robot );
            //notBeRecruited( robot );
            return;                                 //does not act
        }
        
                      
        if ( recruiting ) {                 //NN decided to recruit
            recruiting( robot );
            //notBeRecruited( robot );        //can not be recruited in simultaneous
        }
        else{                               //NN decided not to recruit
            notRecruiting( robot );         
        }
        
        
        
        
        
    }

    
    
    /**
     * Implements the actuator's behavior
     * when not recruiting
     * @param robot the robot that owns
     * the actuator
     */
    private void notRecruiting( Robot robot ) {
        
        recruitSensor = getRecruitSensor( robot );
        
        if ( recruitSensor != null ) {
            recruit = recruitSensor.getRecruit();
            if ( recruit != null ) {
                recruiterSensor = getRecruiterSensor( recruit );
                if ( recruiterSensor != null ) {            //force the recruit
                    recruiterSensor.setRecruiter( null );   //to forget me
                }
            }
            
            recruitSensor.setRecruit( null );               //forget the recruit
        }
        
    }
    
    
    /**
     * Implements the actuator's 
     * behavior when recruiting
     * @param robot the robot that 
     * owns the actuator
     */
    private void recruiting( Robot robot ) {
        
        recruitSensor = getRecruitSensor( robot );
        if ( recruitSensor != null ) {
            recruit = recruitSensor.getRecruit();
            if ( recruit == null ||                                                         //there is no current recruit
                 (recruit != null &&                                                        // OR
                  robot.getPosition().distanceTo( recruit.getPosition() ) > range ) ) {     //current recruit is beyond range
                        
                recruitSensor.setRecruit( chooseCloseRecruit( robot ) );                    //choose another recruit
                                                                                            //which can be null if 
                                                                                            //no recruit is available
            }
        }
        
    }

    
    
    
    /**
     * Chooses a recruit
     * @param robot the robot
     * that owns this actuator
     * @return returns the first 
     * recruit that is available 
     * to be recruited or null
     * of there is none
     */
    private Robot chooseCloseRecruit( Robot robot ) {
        
        List<Robot> closeRobots;                                //get robots
        closeRobots = env.getClosestRobots( robot.getPosition(),//within actuator's 
                                            range);             //range
        
        
        RecruitmentActuatorImmediate act;
        for (Robot closeRobot : closeRobots) {                  
            act = getRecruitmentActuatorImmediate( closeRobot );    
            if ( act.isAvailableToBeRecruited( closeRobot ) ) { //robot is available
                return closeRobot;                              //choose it!
            }
        }
        
        return null;
        
    }
        
        
        
    
         

    
    
    
    
    
    /**
     * Checks if the robot is available
     * to be recruited.
     * @param r the robot that owns the
     * actuator calling this method
     * @return true if the robot is 
     * not recruiting and is accepting
     * a recruiter and does not have a 
     * recruiter, false otherwise
     */
    private Boolean isAvailableToBeRecruited( Robot r ) {
        
        recruiterSensor = getRecruiterSensor( r );
        if ( recruiterSensor != null ) {
            return !recruiting &&                           //not recruiting
                    beRecruited &&                          //accepting recruiter
                    recruiterSensor.getRecruiter() != null; //does not have a recruiter yet
        }
        
        return false;                   //there is no recruiter sensor
                                        //so the robot is not available to be 
                                        //recruited
        
    }

    
    
    
    
    
    
    
    /**
     * Gets the recruit sensor 
     * of a robot
     * @param robot the robot
     * @return the recruit sensor
     * or null if none is found
     */
    private RecruitSensor getRecruitSensor( Robot robot ) {
        return (RecruitSensor) robot.getSensorByType( RecruitSensor.class );
    }

    
    
    
    /**
     * Gets the recruiter sensor 
     * of a robot
     * @param robot the robot
     * @return the recruiter sensor
     * or null if none is found
     */
    private RecruiterSensor getRecruiterSensor( Robot robot ) {
        return (RecruiterSensor) robot.getSensorByType( RecruiterSensor.class );
    }

    
    
    
    /**
     * Gets the RecruitmentActuatorImmediate
     * of a robot
     * @param robot the robot
     * @return the actuator or null
     * if there is none
     */
    private RecruitmentActuatorImmediate getRecruitmentActuatorImmediate( Robot robot ) {
        return (RecruitmentActuatorImmediate) robot.getActuatorByType( RecruitmentActuatorImmediate.class );
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
    

    
    
}
