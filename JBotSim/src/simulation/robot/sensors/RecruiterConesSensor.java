/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.sensors;

import simulation.Simulator;
import simulation.physicalobjects.PhysicalObjectDistance;
import simulation.robot.Robot;
import simulation.util.Arguments;

/**
 * A sensor to perceive the recruiter
 * @author gus
 */
public class RecruiterConesSensor extends RobotSensor {
    
    private Robot target;                   //the robot to be perceived
                                            //by this sensor. If set to
                                            //null any robot within 
                                            //range can be perceived
    
    public RecruiterConesSensor(Simulator simulator, int id, Robot robot, Arguments args) {
        super(simulator, id, robot, args);
    }
    
    
    /**
     * Sets the recruiter that the 
     * sensor should perceive
     * @param recruiter the recruiter
     */
    public void setRecruiter( Robot recruiter ){
       target = recruiter;
    }

    @Override
    protected double calculateContributionToSensor(int sensorNumber, PhysicalObjectDistance source) {
        
        
        if ( target == null ||                                  //there is no target 
             target.getId() != source.getObject().getId() ) {   //target is not the source
            return 0.0;                                         //return 0.0
        }
        
                                                                //there is a target
                                                                //which is the source
        return super.calculateContributionToSensor(sensorNumber, source); 
    }
    
    
    
    
    /**
     * Gets the recruiter being
     * perceived
     * @return the recruiter
     * or null if there is
     * none
     */
    public Robot getRecruiter() {
        return target;
    }
    
    
}
