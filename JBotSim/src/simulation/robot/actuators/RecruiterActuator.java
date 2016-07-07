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
    private Set<Robot> recruitAccepters;    //robots that sent recruit 
                                            //acceptance messages
    
    
    private RobotSensor robotsSensor;       //perceives neighboring robots
    
    
    
    private final Message msg;              //message to recruit robots
    
    private boolean recruiting;             //is the robot recruiting?
    
    
    private static Color recruitingColor;   //robot color when recruiting
    private static Color nonRecruitingColor;//robot color when not recruiting
    
    
    
    
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
        
        recruitingColor = Color.RED;
        nonRecruitingColor = Color.WHITE;
        
        recruitAccepters = new HashSet<>();
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
        
        //note that we allways send recruitment msgs to already 
        //recruited robots to keep the recruitment relationship alive
        
        
        robotsSensor = (RobotSensor)robot.getSensorByType( RobotSensor.class );
        
        
        if ( !recruiting ) {                        //the robot is not recruiting                  
            
            robot.setLedColor( nonRecruitingColor );//change color to signal that 
                                                    //no message is sent
            
            recruit = null;                         //forget last recruit
            recruitAccepters.clear();               //and all recruit accepters
                                        
            
            if ( robotsSensor != null ) {           //set the robots sensor
                robotsSensor.setTarget( null );     //to target all neighbors
            }
            
            return;                                 //.. and all is done
        }
        
        
        
        robot.setLedColor( recruitingColor );       //use LED to signal recruitment 
                                                    //message being sent                
                                                                    
        
                                                    //otherwise, the robot:
                                                    // a) is recruiting; or 
                                                    // b) has a recruit
                           
                                                    
                                                    
        
        if ( recruit != null                        // a) there is a recruit, in range
             && recruit.getPosition().distanceTo( robot.getPosition() ) <= range ) {
                
            robotsSensor.setTarget( recruit );      //set robots sensor 
                                                    //to target this recruit
            
            recruit.getMsgBox().addMsgToInbox( msg, robot );    //send message 
                                                                // to recruit                                                            
        }
        else{                                       // b) there is no recruit
                                                    // or the recruit is outside range
            recruit = null;                         //forget the recruit
            
            robot.broadcastMessage( msg, range );   //broadcast recruitment
                                                    //message to all robots in range    
        }
            
        
        
    }


    

    
    
}
