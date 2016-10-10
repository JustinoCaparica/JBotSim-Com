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
import simulation.robot.sensors.RecruitConesSensor;
import simulation.robot.sensors.RecruitSensor;
import simulation.robot.sensors.RecruiterConesSensor;
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
 * exchanging messages
 * @author gus
 */
public class RecruitmentImmediateActuator extends Actuator {

    
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
    private RecruiterConesSensor recruiterSensor;
    private RecruitConesSensor recruitSensor;    
    private RecruitmentImmediateActuator recruitmentImmediateAct;
    /***************************************************/
     
    
    
    private boolean recruiting;             //is the robot recruiting?
    private boolean beRecruited;            //is the robot accepting 
                                            //recruitment requests?
    
    
    private Robot actOwner;                 //robot that owns the actuator
    
    
    private final Environment env;          //the environment
    
    
    /**
     * Initializes a new instance
     * @param simulator the simulator
     * @param id the actuator id
     * @param args the arguments
     */
    public RecruitmentImmediateActuator( Simulator simulator, int id, Arguments args ) {
        super(simulator, id, args);
        
        
        recruiting = false;
        beRecruited = false;
        
        range = args.getArgumentAsDoubleOrSetDefault("range", RANGE_DEFAULT);
        
        env = simulator.getEnvironment();
        
        actOwner = null;
        
    }

    
    
    
    

    
    
    
    
    
    
    
    
    
    @Override
    public void apply( Robot robot, double timeDelta ) {
          
        actOwner = robot;
        
        
        if ( !robot.isEnabled() ) {                 //disabled robot
            notRecruiting( robot );
            //notBeRecruited( robot );
            return;                                 //does not act
        }
        
                      
        if ( recruiting ) {                 //NN decided to recruit
            recruiting( robot );
            //notBeRecruited( robot );      //can not be recruited in simultaneous
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
                        
                if ( recruit != null ) {
                    recruiterSensor = getRecruiterSensor( recruit );                        //tell the recruit
                    recruiterSensor.setRecruiter( null );                                   //he has no recruiter anymore
                }
                
                
                recruitSensor.setRecruit( chooseCloseRecruit( robot ) );                    //choose another recruit
                                                                                            //which can be null if 
                                                                                            //no recruit is available
                                                                                                                  
                if ( recruitSensor.getRecruit() != null ) {                                 //if a recruit
                    recruiterSensor = getRecruiterSensor( recruitSensor.getRecruit() );     //was found
                    recruiterSensor.setRecruiter( robot );                                  //tell the recruit
                }                                                                           //who the recruiter is
                                       
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
        
//        if ( robot.getId() == 0 ) {
//            System.out.println("");
//            System.out.println("Robot 0 choosing a new recruit");
//        }
        
        
        RecruitmentImmediateActuator act;
        for (Robot closeRobot : closeRobots) {
            if ( closeRobot.getId() != robot.getId() ) {            //avoid recruiting self
                //System.out.println("Robot " + robot.getId() + " analysing robot " + closeRobot.getId() );
                act = getRecruitmentImmediateActuator( closeRobot );    
                if ( act.isAvailableToBeRecruited( closeRobot ) ) { //robot is available
                    //System.out.println("    - YES available");
                    //System.out.println("Robot " + robot.getId() + " recruited robot " + closeRobot.getId() );
                    return closeRobot;                              //choose it!
                }
                else{
                    //System.out.println("    - not available");
                }
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
        
        recruitmentImmediateAct = getRecruitmentImmediateActuator( r );
        recruiterSensor = getRecruiterSensor( r );
        recruitSensor = getRecruitSensor( r );
        if ( recruiterSensor != null && recruitSensor != null ) {
            return  recruitSensor.getRecruit() == null &&                   //does not have a recruit AND
                    recruitmentImmediateAct.isBeingRecruited() &&           //accepting recruiter AND
                    recruiterSensor.getRecruiter() == null;                 //does not have a recruiter yet
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
    private RecruitConesSensor getRecruitSensor( Robot robot ) {
        return (RecruitConesSensor) robot.getSensorByType( RecruitConesSensor.class );
    }

    
    
    
    /**
     * Gets the recruiter sensor 
     * of a robot
     * @param robot the robot
     * @return the recruiter sensor
     * or null if none is found
     */
    private RecruiterConesSensor getRecruiterSensor( Robot robot ) {
        return (RecruiterConesSensor) robot.getSensorByType( RecruiterConesSensor.class );
    }

    
    
    
    /**
     * Gets the RecruitmentImmediateActuator
     * of a robot
     * @param robot the robot
     * @return the actuator or null
     * if there is none
     */
    private RecruitmentImmediateActuator getRecruitmentImmediateActuator( Robot robot ) {
        return (RecruitmentImmediateActuator) robot.getActuatorByType(RecruitmentImmediateActuator.class );
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
        //DEBUG
//        if ( actOwner != null ) {
//            if ( actOwner.getId() == 0 || actOwner.getId() == 1 ) {
//                this.beRecruited = beRecruited;
//            }else{
//                this.beRecruited = false;
//            }
//        }else{
//            this.beRecruited = false;
//        }
            
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
