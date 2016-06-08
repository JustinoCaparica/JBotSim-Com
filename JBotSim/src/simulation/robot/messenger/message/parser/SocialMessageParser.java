/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.messenger.message.parser;

import simulation.robot.Robot;
import simulation.robot.messenger.message.Message;
import simulation.robot.sensors.FocusSensor;
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
        
        
        FocusSensor focusSensor;                    
        focusSensor = (FocusSensor)receiver.getSensorByType( FocusSensor.class );
        
        
        if ( focusSensor.isFocused() &&                         //the receiver is recruited and
             !focusSensor.getRecruiter().equals( emitter ) ) {  //the emitter is not the recruiter
            return;                                             //ignore the received message
        }
        
        
        
        //TODO check if message type is null before using it
        
        switch ( msg.getMsgType() ) {
            
            case REQUEST_FOCUS:                             
                processRequestFocusMsg( focusSensor, receiver, emitter );
                break;
                
            case FOCUS_ACCEPTED:
                processFocusAcceptedMsg();
                break;
                
            default:
                throw new RuntimeException( "Parsing a message of an unknown type" );
                
        }
        
    }

    
    /**
     * Processes a request focus message
     * @param focusSensor the focus 
     * sensor
     * @param receiver the robot that
     * received the message
     * @param emitter the robot that
     * emitted the message
     */
    private void processRequestFocusMsg( FocusSensor focusSensor, 
                                         Robot receiver, 
                                         Robot emitter ) {
        
        if ( !focusSensor.isFocused() ) {           //this robot is not recruited yet
                    
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

    
    /**
     * Processes a focus accepted
     * message
     */
    private void processFocusAcceptedMsg() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    
    
}
