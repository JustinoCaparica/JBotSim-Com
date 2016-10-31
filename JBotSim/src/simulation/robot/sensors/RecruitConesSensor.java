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
import simulation.util.ArgumentsAnnotation;

/**
 * A sensor to perceive the recruit
 * @author gus
 */
public class RecruitConesSensor extends RobotSensor {
    
    
    
    @ArgumentsAnnotation(name = "binarySensor", defaultValue = "0", help = "If this value is set to 1 the sensor will only have two possible input values: 1 or 0")
    private Boolean binarySensor;           //is the sensor input binary?
    
    
    private Robot target;                   //the robot to be perceived
                                            //by this sensor. If set to
                                            //null any robot within 
                                            //range can be perceived
    
    
    
    
    
    public RecruitConesSensor(Simulator simulator, int id, Robot robot, Arguments args) {
        super(simulator, id, robot, args);
        
        binarySensor = args.getArgumentAsIntOrSetDefault("binarySensor", 0)==1;
        
    }
    
    
    /**
     * Sets the recruit that the 
     * sensor should perceive
     * @param recruit the recruit
     */
    public void setRecruit( Robot recruit ){
        target = recruit;
    }

    @Override
    protected double calculateContributionToSensor(int sensorNumber, PhysicalObjectDistance source) {
        
        
        if ( target == null ||                                  //there is no target 
             target.getId() != source.getObject().getId() ) {   //target is not the source
            return 0.0;                                         //return 0.0
        }
        
                                                                //this point onwards
                                                                //there is a target
                                                                //which is the source
        if ( !binarySensor ) {
            return super.calculateContributionToSensor(sensorNumber, source); 
        }
        else{
            if ( super.calculateContributionToSensor(sensorNumber, source) > 0 ) {
                return 1.0;
            }

            return 0.0;
        }
        
        
        
    }
    
    
    /**
     * Gets the recruit being
     * perceived
     * @return the recruit
     * or null if there is
     * none
     */
    public Robot getRecruit() {
        return target;
    }
    
    
    
}
