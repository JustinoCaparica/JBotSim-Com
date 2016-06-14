/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.messenger.garbage;

import simulation.robot.messenger.message.Message;
import simulation.robot.messenger.message.MessageType;
import java.util.ArrayList;
import java.util.HashMap;
import mathutils.Vector2d;
import simulation.Simulator;
import simulation.physicalobjects.GeometricInfo;
import simulation.physicalobjects.PhysicalObject;
import simulation.physicalobjects.PhysicalObjectDistance;
import simulation.physicalobjects.checkers.AllowAllRobotsChecker;
import simulation.robot.Robot;
import simulation.robot.sensors.ConeTypeSensor;
import simulation.robot.sensors.Sensor;
import simulation.util.Arguments;

/**
 * A sensor to receive messages
 * from MessageActuator
 * @author gus
 */
public class MessageSocialSensor extends Sensor {

    
    private HashMap< Robot, Message > msgs;     //last received messages
                                                //<EmitterRobot, Message>
    
    
    
    private Robot focusedRobot;                 //robot to whom
                                                //this sensor is focused on
    
    
    private Double distance;                    //distance and direction
    private Double direction;                   //to the location
                                                //of the focusedRobot

    
    /**
     * Initializes a new instance
     * @param simulator the simulator
     * @param id this sensor id
     * @param robot the robot 
     * that owns this sensor
     * @param args the arguments
     */
    public MessageSocialSensor( Simulator simulator, int id, Robot robot, Arguments args) {
        super(simulator, id, robot, args);
    }
                                                
                                                
                                                
    
    @Override
    public double getSensorReading( int sensorNumber ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
    
    @Override
    public void update( double time, ArrayList<PhysicalObject> teleported ) {
    
    }
    
    
    
    
    

    
    /**
     * Gets the distance to
     * the focused robot
     * @return the distance
     */
    public Double getDistance() {
        return distance;
    }

    /**
     * Gets the direction 
     * of the focused robot
     * location
     * @return the direction
     * in radians
     */
    public Double getDirection() {
        return direction;
    }
    
        
     
    
    
    
    
    
    

    
    /**
     * Adds a message in the sensor
     * to be read later in the
     * next simulation cycle.
     * @param emitter the emitter
     * robot. If a message already
     * exists from the emitter
     * robot, the existing 
     * message is replaced
     * @param msg the message
     */
    public void setMessage( Robot emitter, Message msg ) {
        msgs.put(robot, msg);
    }

    
    /**
     * Clears all messages
     */
    public void clearMessages(){
        msgs.clear();
    }
    
    
    
    /**
     * Sets the state of
     * the sensor
     * @param focused set
     * to true to focus 
     * or to false to
     * defocus. If the sensor
     * is defocused, it focus 
     * on a random robot
     * amongst the robots that 
     * asked for focus. If the 
     * sensor is already focused, 
     * it remains focused on the
     * same robot; (TODO what happens
     * when the robot is out of range)
     */
    public void setFocus( Boolean focused ) {
        
        if( !focused ){                             //let us defocus 
            focusedRobot = null;
            distance     = null;
            direction    = null;
        }
        else{                                       //let us focus.. 
            if ( focusedRobot != null ) {           //(if we are not focused already)
                
                for ( Robot robot : msgs.keySet() ){//..on a random robot..
                    
                    if ( msgs.get(robot).getMsgType() == MessageType.REQUEST_FOCUS ) {
                        focusedRobot = robot;       //..that sent a requestFocus message
                        setFocusedRobotPosition( robot );
                        break;  //horrible, i know
                    }
                    
                }
                
            }
        }
    }

    
    /**
     * Determines and stores the
     * focused robot's position
     * @param focusedRobot the focused
     * robot
     */
    private void setFocusedRobotPosition( Robot focusedRobot ) {
        
                                        
        Vector2d myPos;                                         //robot's own
        myPos = super.robot.getPosition();                      //location
        
                                                                //determine distance 
        distance = focusedRobot.getDistanceBetween( myPos );    //between robots
                              
        
        Vector2d focusedRobotPos;                               //get focused 
        focusedRobotPos = focusedRobot.getPosition();           //robot's position
        
        
        Vector2d heading = new Vector2d( 1, 0 );                //robot's own
        heading.rotate( super.robot.getOrientation() );         //orientation
        
                                                                //diretion of the
        //direction = heading.getSignedAngle( focusedRobotPos );  //focused robot's 
                                                                //position relative 
                                                                //to own robot's
                                                                //heading        
    }

    
    

    
    
    
    
    
    
    
    
    
}
