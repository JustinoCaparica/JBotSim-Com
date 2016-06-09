/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.messenger.message.parser;

import simulation.robot.Robot;
import simulation.robot.messenger.message.Message;
import simulation.robot.sensors.FocusedBySensor;
import simulation.robot.sensors.FocusingOnSensor;
import simulation.robot.sensors.RecruitedSensor;
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
        
        
        FocusingOnSensor focusingOnSensor;                    
        focusingOnSensor = (FocusingOnSensor)receiver.getSensorByType(FocusingOnSensor.class );
        
        FocusedBySensor focusedBySensor;
        focusedBySensor = (FocusedBySensor) receiver.getSensorByType(FocusedBySensor.class );
        
        
        
        
        //TODO check if message type is null before using it
        
        switch ( msg.getMsgType() ) {
            
            case REQUEST_FOCUS:                 
                if ( focusingOnSensor.isFocused() &&                        //the receiver is recruited and
                    !focusingOnSensor.getRecruiter().equals( emitter ) ) {  //the emitter is not the recruiter
                    return;                                                 //ignore the received message
                }
                processRequestFocusMsg( focusingOnSensor, receiver, emitter );
                break;
                
            case FOCUS_ACCEPTED:
                processFocusAcceptedMsg( focusedBySensor, receiver, emitter );
                break;
                
            default:
                throw new RuntimeException( "Parsing a message of an unknown type" );
                
        }
        
    }

    
    
    /**
     * Processes a focus accepted
     * message
     * @param focusedBySensor the 
     * sensor that knows the recruit,
     * owned by the receiver
     * @param receiver the message
     * receiver
     * @param emitter the message
     * emitter
     */
    private void processFocusAcceptedMsg(   FocusedBySensor focusedBySensor, 
                                            Robot receiver, 
                                            Robot emitter ) {
        
        if ( !focusedBySensor.isFocused() ) {           //no other robot is focused on this robot
            RecruitedSensor recruitedSensor;
            recruitedSensor = (RecruitedSensor) receiver.getSensorByType( RecruitedSensor.class );
            
            if ( recruitedSensor.getRecruit() != null ) {   //this robot does not have a recruit yet
                recruitedSensor.setRecruit( emitter );      //accept the emitter as a recruit
            }
            
        }
        else{                                               //there is already a recruit
            //ignore the message
            
            
        }
    }
    
    
    
    
    
    
    /**
     * Processes a request focus message
     * @param focusedOnSensor the focus 
     * sensor
     * @param receiver the robot that
     * received the message
     * @param emitter the robot that
     * emitted the message
     */
    private void processRequestFocusMsg( FocusingOnSensor focusedOnSensor, 
                                         Robot receiver, 
                                         Robot emitter ) {
        
        if ( !focusedOnSensor.isFocused() ) {               //this robot is not recruited yet
                    
            RecruiterSensor recruiterSensor;
            recruiterSensor = (RecruiterSensor) receiver.getSensorByType( RecruiterSensor.class );

            if ( recruiterSensor.getRecruitRequester() == null ) {  //there isn't a previous recruit requester
                recruiterSensor.setRecruitRequester( emitter );     //set the emitter as the recruit requester
            }else{
                 //there is a previous recruit requester
                 //ignore this recruit requester
            }
        }
        else{                                       //this robot is already recruited by the emitter



        }
    }

    
    
    
    
    
}
