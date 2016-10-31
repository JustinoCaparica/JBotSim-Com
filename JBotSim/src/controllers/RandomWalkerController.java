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
    
    
    
    
    
    public RandomWalkerController(Simulator simulator, Robot robot, Arguments args) {
        
        super(simulator, robot, args);
        
        this.simulator = simulator;
        
        maxSpeed = ((TwoWheelActuator)robot.getActuatorByType( TwoWheelActuator.class )).getMaxSpeed();
        
        
        
        
    }
    
    
    
    
    
    
    @Override
    public void controlStep( double time ) {
        
        if ( robot.getPosition().distanceTo( new Vector2d(0, 0) ) >= 2.5 ){     //too far away from nest
            robot.setOrientation( robot.getOrientation() + Math.PI );           //turn around
        }
        
        
        double deltaLeft, deltaRight;
        
        double deltaLeftMax, deltaRightMax;
        
        if ( robot.getId() % 2 == 0 ) {             //split robots in two groups
            deltaLeftMax     = 0.5;                 //one group turns right
            deltaRightMax    = 0.4;
        }else{
            deltaLeftMax     = 0.4;                 //other group turns left
            deltaRightMax    = 0.5;
        }
        
        
        deltaLeft   = simulator.getRandom().nextDouble() * deltaLeftMax;
        deltaRight  = simulator.getRandom().nextDouble() * deltaRightMax;
        
        ((DifferentialDriveRobot)robot).setWheelSpeed( maxSpeed * (1 - deltaLeftMax + deltaLeft), 
                                                       maxSpeed * (1 - deltaRightMax + deltaRight) );
        
            
    }
    
    
    
    
    
    
    
    
}
