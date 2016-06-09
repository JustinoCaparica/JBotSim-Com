/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolutionaryrobotics.neuralnetworks.inputs;

import simulation.robot.sensors.FocusedBySensor;
import simulation.robot.sensors.Sensor;

/**
 * An NNInput for the FocusedBySensor
 * @author gus
 */
public class FocusedByNNInput extends NNInput {
    
    private FocusedBySensor sensor;                 //the sensor

    /**
     * Initializes a new instance
     * @param s the sensor
     */
    public FocusedByNNInput( Sensor s ) {
        super(s);
    }

    
    
    @Override
    public int getNumberOfInputValues() {
        return 1;
    }

    
    
    @Override
    public double getValue( int index ) {
        return sensor.getSensorReading( index );
    }
    
    
    
    
    
    
}
