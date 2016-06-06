/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.sensors;

import mathutils.Vector2d;
import simulation.Simulator;
import simulation.robot.Robot;
import simulation.robot.actuators.RecruitedActuator;
import simulation.util.Arguments;

/**
 * Sensor to perceive the angle between the robot that owns
 * the sensor (the recruited) to the robot requesting a recruit
 * (the recruiter)
 * @author gus
 */
public class RecruiterAngleSensor extends Sensor {

    private final RecruitedActuator actuator;   //actuator that 
                                                //accepts recruitment requests
    
    
    /**
     * Initializes a new instance
     * @param simulator the simulator
     * @param id the sensor id
     * @param robot the robot
     * that owns this sensor
     * @param args the arguments
     */
    public RecruiterAngleSensor(Simulator simulator, int id, Robot robot, Arguments args) {
        super(simulator, id, robot, args);
        
        actuator = (RecruitedActuator) robot.getActuatorByType( RecruitedActuator.class );
    }

    
    
    @Override
    public double getSensorReading( int sensorNumber ) {
        
        Robot recruiter;                        //recruiter robot
        recruiter = actuator.getRecruiter();        
        
        if ( recruiter == null ) {              //if there is no recruiter
            return 0.0;                         //the sensor reading is 0
        }
        
        
        Robot recruited;                        //recruited robot
        recruited = super.robot;                //(the sensor owner!)
        
        
        double recruitedOrientation;
        recruitedOrientation = recruited.getOrientation();  //orientation in range [-PI,PI]
        
        
                                                            //vector representation
        Vector2d v = new Vector2d( recruitedOrientation );  //of recruited orientation
        
        
        double angle;                                       //angle between robots
        angle = v.getSignedAngle( recruiter.getPosition() );//in range [-PI, PI]  
        angle += Math.PI;                                   
        angle /= Math.PI;                                   //orientation in range [0,1]
        
        
        return angle;
        
    }
    
    
    
}
