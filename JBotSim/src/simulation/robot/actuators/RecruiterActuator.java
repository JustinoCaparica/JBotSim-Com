/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.actuators;

import java.util.List;
import simulation.Simulator;
import simulation.robot.Robot;
import simulation.robot.actuators.messenger.Message;
import simulation.robot.actuators.messenger.MessageActuator;
import simulation.robot.actuators.messenger.MessageType;
import simulation.robot.sensors.MessageSocialSensor;
import simulation.util.Arguments;

/**
 * Actuator that recruits other robots
 * @author gus
 */
public class RecruiterActuator extends Actuator {

    
    private List<Robot> recruited;          //recruited robots
    
    private Message msg;                    //message to recruit robots
    
    
    
    /**
     * Initializes a new instance
     * @param simulator the simulator
     * @param id the actuator id
     * @param args the arguments
     */
    public RecruiterActuator(Simulator simulator, int id, Arguments args) {
        super(simulator, id, args);
        
        msg = new Message( MessageType.REQUEST_FOCUS );
    }

    
    /**
     * Clears the recruited robots.
     * After this method finishes
     * there will be no robots 
     * under recruitment
     */
    public void clearRecruited(){
        recruited.clear();
    }
    
    
    /**
     * Adds a recruit
     * @param recruit the 
     * new recruit
     */
    public void addRecruit( Robot recruit ){
        recruited.add( recruit );
    }
    
    
    
    
    @Override
    public void apply( Robot robot, double timeDelta ) {
        
        MessageActuator msgAct;
        msgAct = ( MessageActuator )robot.getActuatorByType( MessageActuator.class );
        
        
        if( recruited.size() == 0 ){            //none robot recruited
            msgAct.broadcastMsg( msg );         //broadcast recruitment msg
        }
        else{                                   //some robot(s) recruited
            sendMsgToRecruits( msg );           //send recruited robots a recruitment msg
        }
        
        //we allways send recruitment msgs to recruited 
        //robots to keep the recruitment relationship alive
    }

    
    
    /**
     * Sends a recruitment message
     * to all recruited robots
     * @param msg 
     */
    private void sendMsgToRecruits( Message msg ) {
        
        MessageSocialSensor msgSocSensor;
        
        for ( Robot robot : recruited ) {
            msgSocSensor = (MessageSocialSensor) robot.getSensorByType( MessageSocialSensor.class );
            msgSocSensor.addMessage( msg );
        }
        
    }
    
}
