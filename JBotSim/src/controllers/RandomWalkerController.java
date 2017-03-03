/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.util.Random;
import mathutils.Vector2d;
import simulation.Simulator;
import simulation.robot.DifferentialDriveRobot;
import simulation.robot.Robot;
import simulation.robot.actuators.TwoWheelActuator;
import simulation.util.Arguments;

/**
 * A controller of a DiferentiaDriveRobot
 * that moves the robot randomly
 * @author gus
 */
public class RandomWalkerController extends Controller {
    
    private double maxSpeed;                    //wheels maximum speed
    
    private Simulator simulator;                //the simulator
    
    private double maxDistanceFromCenter;       //maximum distance the robot
                                                //can be of the center; the robot
                                                //turns around when this distance
                                                //is reached
    
    
    
    public RandomWalkerController(Simulator simulator, Robot robot, Arguments args) {
        
        super(simulator, robot, args);
        
        this.simulator = simulator;
        
        maxSpeed = ((TwoWheelActuator)robot.getActuatorByType( TwoWheelActuator.class )).getMaxSpeed();
        
        maxDistanceFromCenter = args.getArgumentAsDouble("maxDistToCenter");
        
    }
    
    
    
    
    
    
    @Override
    public void controlStep( double time ) {
        
        if ( robot.getPosition().distanceTo( new Vector2d(0, 0) ) >= maxDistanceFromCenter ){   //too far away from nest
            robot.setOrientation( robot.getOrientation() + Math.PI );                           //turn around
        }
        
        
        double deltaLeft, deltaRight;
        
        double deltaLeftMax, deltaRightMax;
        
        if ( robot.getId() % 2 == 0 ) {             //split robots in two groups
            deltaLeftMax     = 0.6;                 //one group turns right
            deltaRightMax    = 0.5;
        }else{
            deltaLeftMax     = 0.3;                 //other group turns left
            deltaRightMax    = 0.4;
        }
        
        
        deltaLeft   = simulator.getRandom().nextDouble() * deltaLeftMax;
        deltaRight  = simulator.getRandom().nextDouble() * deltaRightMax;
        
        ((DifferentialDriveRobot)robot).setWheelSpeed( maxSpeed * (1 - deltaLeftMax + deltaLeft), 
                                                       maxSpeed * (1 - deltaRightMax + deltaRight) );
        
            
    }
    
    
    
    
    
    
    
    
}
