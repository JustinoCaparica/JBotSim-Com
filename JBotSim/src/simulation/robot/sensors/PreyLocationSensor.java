/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.sensors;

import simulation.Simulator;
import simulation.physicalobjects.GeometricCalculator;
import simulation.physicalobjects.GeometricInfo;
import simulation.physicalobjects.Prey;
import simulation.robot.Robot;
import simulation.util.Arguments;

/**
 * Sensor to perceive the distance
 * and angle to a prey
 * @author gus
 */
public class PreyLocationSensor extends Sensor {

    
    
    
    private Prey prey;                          //the target prey

    
    private Simulator sim;                      //the simulator
    
    private GeometricCalculator calc;           //geometric calculator
    
    
    
    
    public PreyLocationSensor(Simulator simulator, int id, Robot robot, Arguments args) {
        super(simulator, id, robot, args);
        
        this.sim = simulator;
        
        
        
        calc = new GeometricCalculator();
        
        
    }
    
    
    
    @Override
    public double getSensorReading( int sensorNumber ) {
        
        prey = sim.getEnvironment().getPrey().get( 0 );
        
//        if ( prey == null ) {                   //no prey being sensed
//            return 0.0;                         //return value is 0
//        }
        
        
        
        
        double value;
        
        GeometricInfo sensorInfo = calc.getGeometricInfoBetween( super.robot.getPosition(), 
                                                                 super.robot.getOrientation(), 
                                                                 prey, sim.getTime() );
			
        switch (sensorNumber) {
            
            case 0:
                //range
                value = super.robot.getPosition().distanceTo( prey.getPosition() );
                value = value + 1.0;                        //making sure distance >= 1
                                                            //to avoid distance = 0
                value = 1 / value;
                break;
            
            case 1:
                //angle
                value = sensorInfo.getAngle() / Math.PI / 2 + 0.5;
                break;
                
            default:
                throw new RuntimeException( "Invalid sensor number in PreyLocationSensor" );
        }

            
        return value;
        
        
    }
    
    
    
    
    
    
    
    
}
