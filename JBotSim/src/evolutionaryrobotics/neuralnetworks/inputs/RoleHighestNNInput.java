/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolutionaryrobotics.neuralnetworks.inputs;

import simulation.robot.sensors.RoleHighestSensor;
import simulation.robot.sensors.Sensor;

/**
 * NNInput to get the readings
 * from the RoleHighestSensor
 * @author gus
 */
public class RoleHighestNNInput extends NNInput {

    private final RoleHighestSensor roleHighestSensor;  //sensor that
                                                        //perceives the highest role 
                                                        //of other robots

    /**
     * Initializes a new instance
     * @param s the sensor
     * that this NNInput will
     * use
     */
    public RoleHighestNNInput( Sensor s ) {
        super(s);
        this.roleHighestSensor = ( RoleHighestSensor ) s;
    }
    
    @Override
    public int getNumberOfInputValues() {
        return 1;
    }

    @Override
    public double getValue( int index ) {
        
        return roleHighestSensor.getSensorReading( index );
        
    }
    
}
