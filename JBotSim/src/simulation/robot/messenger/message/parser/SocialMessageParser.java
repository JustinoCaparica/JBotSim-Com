/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.messenger.message.parser;

import simulation.robot.Robot;
import simulation.robot.actuators.RecruiterActuator;
import simulation.robot.messenger.message.Message;
import simulation.robot.sensors.RecruitSensor;
import simulation.robot.sensors.RecruiterSensor;

/**
 * A message parser. When the robot receiving the message
 * is already recruited, the message parser ignores all 
 * messages from robots that are not the recruiter
 * @author gus
 */
public class SocialMessageParser implements MessageParser {

    
    
    @Override
    public void parse( Robot receiver, Robot emitter, Message msg ) {
        
        
        
        
        
        
        
        //TODO check if message type is null before using it
        
        switch ( msg.getMsgType() ) {
            
            case REQUEST_FOCUS:                 
                processRequestFocusMsg( receiver, emitter );
                break;
                
            case FOCUS_ACCEPTED:
                processFocusAcceptedMsg( receiver, emitter );
                break;
                
            default:
                throw new RuntimeException( "Parsing a message of an unknown type" );
                
        }
        
    }

    
    
    /**
     * Processes a focus accepted
     * message
     * @param receiver the message
     * receiver
     * @param emitter the message
     * emitter
     */
    private void processFocusAcceptedMsg(   Robot receiver, 
                                            Robot emitter ) {
        
        RecruitSensor recruitSensor;
        recruitSensor = (RecruitSensor) receiver.getSensorByType(RecruitSensor.class );
        
        RecruiterActuator recruiterActuator;
        recruiterActuator = (RecruiterActuator) receiver.getActuatorByType(RecruiterActuator.class );
        
        
        if ( recruitSensor.getRecruit() == null         //this robot does not have a recruit yet
             && recruiterActuator.isRecruiting() ) {    //and is recruiting
            
            recruitSensor.setRecruit( emitter );        //accept the emitter as a recruit
        }
        
                                                        //otherwise, there is a recruit
                                                        //ignore the message
    }
    
    
    
    
    
    
    /**
     * Processes a request focus message
     * @param receiver the robot that
     * received the message
     * @param emitter the robot that
     * emitted the message
     */
    private void processRequestFocusMsg( Robot receiver, 
                                         Robot emitter ) {
        
            
        RecruiterSensor recruiterSensor;
        recruiterSensor = (RecruiterSensor) receiver.getSensorByType( RecruiterSensor.class );

        
        
        
        
        if ( recruiterSensor.getRecruiter() == null ){              //there is no recruiter
             
            if ( recruiterSensor.getRecruitRequester() == null ) {  //there is no recruit requester
                recruiterSensor.setRecruitRequester( emitter );     //set the emitter as the recruit requester
            }
            else{                                                   //there is already a recruit requester
                                                                    //ignore this request
            }
        }
        else{
            //if this robot is already recruited, i.e., there is a recruiter
            //we ignore this request
        }
        
        
    }

    
    
    
    
    
}
