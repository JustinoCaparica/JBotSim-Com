/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.messenger.message.parser;

import simulation.robot.Robot;
import simulation.robot.messenger.message.Message;
import simulation.robot.messenger.message.MessageType;
import simulation.robot.sensors.FocusSensor;
import simulation.robot.sensors.RecruiterAngleSensor;
import simulation.robot.sensors.RecruiterDistanceSensor;

/**
 * A social message parser
 * @author gus
 */
public class SocialMessageParser implements MessageParser {

    
    
    @Override
    public void parse( Robot receiver, Robot emitter, Message msg ) {
        
        //TODO check if message type is null before using it
        
        switch ( msg.getMsgType() ) {
            
            case REQUEST_FOCUS:                             //recruitment request 
                                                            //received
                
                FocusSensor focusSensor;                    
                focusSensor = (FocusSensor)receiver.getSensorByType( FocusSensor.class );
                
                if ( focusSensor.getRecruiter() == null ) { //this robot is not recruited yet
                    
                    RecruiterDistanceSensor recruiterDistSensor;
                    recruiterDistSensor = (RecruiterDistanceSensor) receiver.getSensorByType( RecruiterDistanceSensor.class );
                    
                    RecruiterAngleSensor recruiterAngleSensor;
                    recruiterAngleSensor = (RecruiterAngleSensor) receiver.getSensorByType( RecruiterAngleSensor.class );
                    
                    
                    if ( recruiterDistSensor.getRecruitRequester() == null ) {  //make sure a previous recruit requester
                        recruiterDistSensor.setRecruitRequester( emitter );     //is not overwritten
                    }
                    if ( recruiterAngleSensor.getRecruitRequester() == null ) {  //make sure a previous recruit requester
                        recruiterAngleSensor.setRecruitRequester( emitter );     //is not overwritten
                    }
                    
                    
                    
                }
                
                if ( focusSensor.getRecruiter().equals( emitter ) ) { //this robot is recruited 
                    
                }
                break;
                
            case FOCUS_ACCEPTED:
                
                break;
                
            default:
                throw new RuntimeException( "Parsing a message of an unknown type" );
                
        }
        
    }
    
    
    
    
    
}
