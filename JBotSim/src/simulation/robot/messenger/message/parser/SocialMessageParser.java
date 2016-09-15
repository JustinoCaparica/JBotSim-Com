/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.messenger.message.parser;

import simulation.robot.Robot;
import simulation.robot.actuators.RecruitmentActuator;
import simulation.robot.actuators.RecruiterActuator;
import simulation.robot.messenger.message.Message;

/**
 * A message parser. When the robot receiving the message
 * is already recruited, the message parser ignores all 
 * messages from robots that are not the recruiter
 * @author gus
 */
public class SocialMessageParser implements MessageParser {

    
    
    @Override
    public void parse( Robot receiver, Robot emitter, Message msg ) {
        
        
        
        
        if ( msg.getMsgType() == null ) {
            throw new RuntimeException( SocialMessageParser.class.getSimpleName() + ": msg.getMsgType() == null" );
        }
        
        
        
        switch ( msg.getMsgType() ) {
            
            case REQUEST_FOCUS:                 
                processRequestFocusMsg( receiver, emitter );
                break;
                
            case FOCUS_ACCEPTED:
                processFocusAcceptedMsg( receiver, emitter );
                break;
                
            default:
                throw new RuntimeException( SocialMessageParser.class.getSimpleName() + ": msg.getMsgType() returns unknown type" );
                
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
        //the time step before the previous time step
        if ( recruiterActuator.getPreviousReceivers().contains( emitter ) ) {
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
        
        
        
        RecruitmentActuator recruitmentActuator;
        recruitmentActuator = (RecruitmentActuator) receiver.getActuatorByType( RecruitmentActuator.class );
        if ( recruitmentActuator != null ) {
            recruitmentActuator.addRecruitRequester( emitter );
        }
        
        
        
        
//        RecruitedActuator recruitedActuator;
//        recruitedActuator = (RecruitedActuator) receiver.getActuatorByType( RecruitedActuator.class );
//        if ( recruitedActuator != null ) {
//            recruitedActuator.addRecruitRequester( emitter );
//        }
        
        
        
        

        
        
    }

    
    
    
    
    
}
