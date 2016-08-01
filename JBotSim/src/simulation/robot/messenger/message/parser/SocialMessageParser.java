/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.messenger.message.parser;

import simulation.robot.Robot;
import simulation.robot.actuators.RecruitedActuator;
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
        
        
        RecruiterActuator recruiterActuator;
        recruiterActuator = (RecruiterActuator) receiver.getActuatorByType(RecruiterActuator.class );
               
        //recruiter can only accept a recruit that received
        //a recruitment request message from the recruiter in the
        //previous time step
        if ( recruiterActuator.getReceivers().contains( emitter ) ) {
            recruiterActuator.addRecruitAccepter( emitter );
        }
        
        
        
        
        
        
        
//        RecruitedActuator recruitedActuator;
//        recruitedActuator = (RecruitedActuator) receiver.getActuatorByType( RecruitedActuator.class );
//        recruitedActuator.addRecruitRequester(emitter);
        
//        if ( recruitedActuator.getRecruiter() != null ) {   //robot is a recruit, thus 
//            return;                                         //robot can not accept to be recruiter
//        }
//        
//        
//        
////        RecruitSensor recruitSensor;
////        recruitSensor = (RecruitSensor) receiver.getSensorByType(RecruitSensor.class );
//        
//        RecruiterActuator recruiterActuator;
//        recruiterActuator = (RecruiterActuator) receiver.getActuatorByType(RecruiterActuator.class );
//        
//        
//        if ( recruiterActuator.getRecruit() == null     //this robot does not have a recruit yet
//             && recruiterActuator.isRecruiting() ) {    //and is recruiting
//            
//            recruiterActuator.setRecruit( emitter );    //accept the emitter as a recruit
//        }
//        
//                                                        //otherwise, there is a recruit
//                                                        //ignore the message
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
        
        
        RecruitedActuator recruitedActuator;
        recruitedActuator = (RecruitedActuator) receiver.getActuatorByType( RecruitedActuator.class );
        if ( recruitedActuator != null ) {
            recruitedActuator.addRecruitRequester( emitter );
        }
        
        
        
        
//        RecruiterActuator recruiterActuator;
//        recruiterActuator = (RecruiterActuator) receiver.getActuatorByType( RecruiterActuator.class );
////        if ( recruiterActuator.isRecruiting() ) {   //robot is a recruiter or trying to recruit, thus 
////            return;                                 //robot can not accept to be recruited
////        }
//        
//        
//        RecruitedActuator recruitedActuator;
//        recruitedActuator = (RecruitedActuator) receiver.getActuatorByType( RecruitedActuator.class );
//        
//        
//        
//        
//        if ( recruitedActuator.getRecruiter() == null ){              //there is no recruiter
//             
//            if ( recruitedActuator.getRecruitRequester() == null ) {  //there is no recruit requester
//                recruitedActuator.setRecruitRequester( emitter );     //set the emitter as the recruit requester
//            }
//            else{                                                   //there is already a recruit requester
//                                                                    //ignore this request
//            }
//        }
//        else{
//            //if this robot is already recruited, i.e., there is a recruiter
//            //we ignore this request
//        }
        
        
    }

    
    
    
    
    
}
