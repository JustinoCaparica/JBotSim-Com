/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolutionaryrobotics.neuralnetworks.inputs;

import simulation.robot.sensors.FocusingOnSensor;
import simulation.robot.sensors.Sensor;

/**
 * An NNInput for the FocusingOnSensor
 * @author gus
 */
public class FocusingOnNNInput extends NNInput {
    
    private FocusingOnSensor sensor;                 //the sensor

    /**
     * Initializes a new instance
     * @param s the sensor
     */
    public FocusingOnNNInput( Sensor s ) {
        super(s);
        this.sensor = ( FocusingOnSensor ) s;
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
