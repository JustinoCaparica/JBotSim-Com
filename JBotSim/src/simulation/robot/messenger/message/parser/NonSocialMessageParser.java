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
 * A message parser. The receiving robot does not ignore
 * messages from other robots even when it is already
 * recruited
 * @author gus
 */
public class NonSocialMessageParser implements MessageParser {

    
    
    @Override
    public void parse( Robot receiver, Robot emitter, Message msg ) {
        
        
        FocusingOnSensor focusingOnSensor;                    
        focusingOnSensor = (FocusingOnSensor)receiver.getSensorByType(FocusingOnSensor.class );
        
        FocusedBySensor focusedBySensor;
        focusedBySensor = (FocusedBySensor) receiver.getSensorByType(FocusedBySensor.class );
        
        
        
        
        //TODO check if message type is null before using it
        
        switch ( msg.getMsgType() ) {
            
            case REQUEST_FOCUS:                 
                processRequestFocusMsg( receiver, emitter );
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
    private void processRequestFocusMsg( Robot receiver, 
                                         Robot emitter ) {
                    
        RecruiterSensor recruiterSensor;
        recruiterSensor = (RecruiterSensor) receiver.getSensorByType( RecruiterSensor.class );
        recruiterSensor.setRecruitRequester( emitter );
        
    }

    
    
    
    
    
}
