/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.sensors;

import simulation.Simulator;
import simulation.physicalobjects.GeometricInfo;
import simulation.physicalobjects.PhysicalObjectDistance;
import simulation.robot.Robot;
import simulation.robot.actuators.messenger.Message;
import simulation.robot.actuators.messenger.MessageActuator;
import simulation.util.Arguments;

/**
 * A sensor to receive messages
 from MessageActuator
 * @author gus
 */
public class MessageSensor extends ConeTypeSensor {

    
    private Message msg;                //last received message
    private Robot emitter;              //last message's emitter robot 
    
    
    
    public MessageSensor( Simulator simulator, int id, Robot robot, Arguments args ) {
        super(simulator, id, robot, args);
    }

    
    
    @Override
    protected double calculateContributionToSensor( int sensorNumber, 
                                                    PhysicalObjectDistance source ) {
        
        
        
        GeometricInfo sensorInfo = getSensorGeometricInfo(sensorNumber, source);
		
            if( (sensorInfo.getDistance() < getCutOff()) && 
                (sensorInfo.getAngle() < (openingAngle / 2.0)) && 
                (sensorInfo.getAngle() > (-openingAngle / 2.0)) ) {

                    Robot msgEmitter;
                    msgEmitter = (Robot)source.getObject();            //emitting robot

                    MessageActuator act;
                    Class type = MessageActuator.class;
                    act = (MessageActuator)robot.getActuatorByType( type );

                    act.addRobotInRange( msgEmitter, sensorNumber );    //register emitting
                                                                        //robot

                    return (getRange() - sensorInfo.getDistance()) / getRange();

            }
            return 0;
    }

    
    
    
    
    @Override
    protected void calculateSourceContributions(PhysicalObjectDistance source) {
            for(int j=0; j<numberOfSensors; j++) {
                    readings[j] = Math.max(calculateContributionToSensor(j, source), readings[j]);
            }
    }
    
        
     

    
    /**
     * Stores a message in the sensor
     * to be read later in the simulation
     * cycle
     * @param emitter the emitter
     * robot
     * @param msg the message
     */
    public void setMessage( Robot emitter, Message msg ) {
        this.msg = msg;
        this.emitter = emitter;
    }

    
    
    
    
    
    
    
    
    
}
