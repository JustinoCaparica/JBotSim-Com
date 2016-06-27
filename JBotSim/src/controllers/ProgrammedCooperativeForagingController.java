/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import simulation.Simulator;
import simulation.robot.Robot;
import simulation.robot.actuators.RecruitedActuator;
import simulation.robot.actuators.RecruiterActuator;
import simulation.robot.actuators.TwoWheelActuator;
import simulation.util.Arguments;

/**
 * A programmed controller to test the recruitment protocol
 * @author gus
 */
public class ProgrammedCooperativeForagingController extends Controller {
    
    private Robot robot;
    
    
    public ProgrammedCooperativeForagingController(Simulator simulator, Robot robot, Arguments args) {
        super(simulator, robot, args);
        
        this.robot = robot;
        
    }
    
    
    
    @Override
    public void controlStep( double time ) {

        
        TwoWheelActuator wheels;
        wheels = (TwoWheelActuator) robot.getActuatorByType( TwoWheelActuator.class );
        
        int speed;
        
        if ( robot.getId() == 0 ){
            speed = 1;
        }
        else{
            speed = 0;
        }
        
        
        if ( time < 20 ) {    
            wheels.setLeftWheelSpeed( speed );
            wheels.setRightWheelSpeed( speed );
        }
        else{
            wheels.setLeftWheelSpeed( 0.5 );
            wheels.setRightWheelSpeed( 0.5 );
            testRecruitment();
        }




    }

    /**
     * Tests the recruitment ability of robots
     */
    private void testRecruitment() {
        
         
        
        
        if( robot.getId() == 0 ){
            RecruiterActuator act;
            act = (RecruiterActuator) robot.getActuatorByType( RecruiterActuator.class );
            act.setRecruiting( true );
            
        }
        else{
            RecruitedActuator act;
            
            
        }
        
        
        
    }
    
    
    
}
