/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

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
    public void controlStep(double time) {
        
        if ( robot.getId() % 3 == 0 ) {
            ((DifferentialDriveRobot)robot).setWheelSpeed( maxSpeed * 0.54, maxSpeed * 0.58 );
        }
        else{
            if ( robot.getId() % 2 == 0 ) {
            ((DifferentialDriveRobot)robot).setWheelSpeed( maxSpeed * 0.75, maxSpeed * 0.84 );
            }
            else{
                ((DifferentialDriveRobot)robot).setWheelSpeed( maxSpeed * 0.98, maxSpeed * 0.91 );
            }
        }
        
        
        
        
        
            
    }
    
    
    
    
    
    
    
    
}
