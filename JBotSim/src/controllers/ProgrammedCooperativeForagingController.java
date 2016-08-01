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
    private Simulator simulator;
    
    public ProgrammedCooperativeForagingController(Simulator simulator, Robot robot, Arguments args) {
        super(simulator, robot, args);
        
        this.simulator = simulator;
        
        this.robot = robot;
        
    }
    
    
    
    @Override
    public void controlStep( double time ) {

        
        TwoWheelActuator wheels;
        wheels = (TwoWheelActuator) robot.getActuatorByType( TwoWheelActuator.class );
        
        double speed;
        
        if ( robot.getId() == 0 ){
            speed = 0.8;
        }
        else if ( robot.getId() == 1 ) {
            speed = 0.2;
        }else{
            speed = 0.0;
        }
        
        
        
        if ( time < 500 ) {    
            wheels.setLeftWheelSpeed( speed );
            wheels.setRightWheelSpeed( speed );
        }
        else{
            wheels.setLeftWheelSpeed( 0.5 );
            wheels.setRightWheelSpeed( 0.5 );
            
        }
        
        
        testRecruitment();



    }

    /**
     * Tests the recruitment ability of robots
     */
    private void testRecruitment() {
        
        
        
        
        if( robot.getId() == 0 ){
            RecruiterActuator recruiterAct;
            recruiterAct = (RecruiterActuator) robot.getActuatorByType( RecruiterActuator.class );
            recruiterAct.setRecruiting( true );
            
            
            RecruitedActuator recruitedAct;
            recruitedAct = (RecruitedActuator) robot.getActuatorByType( RecruitedActuator.class );
            recruitedAct.setRecruitedState( false );
        }
        else if ( robot.getId() == 1 ) {
            RecruitedActuator act;
            act = (RecruitedActuator) robot.getActuatorByType( RecruitedActuator.class );
            act.setRecruitedState( true );
            
            RecruiterActuator recruiterAct;
            recruiterAct = (RecruiterActuator) robot.getActuatorByType( RecruiterActuator.class );
            recruiterAct.setRecruiting( false );
            
        }else{
            
        }
        
        
        
    }
    
    
    
}
