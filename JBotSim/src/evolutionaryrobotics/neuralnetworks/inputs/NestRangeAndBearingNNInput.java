/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolutionaryrobotics.neuralnetworks.inputs;

import simulation.robot.sensors.NestRangeAndBearingSensor;
import simulation.robot.sensors.Sensor;

/**
 * NNInput to get the readings
 * from the NestRangeAndBearing sensor
 * @author gus
 */
public class NestRangeAndBearingNNInput extends NNInput {

    private final NestRangeAndBearingSensor nestSensor; //sensor that
                                                        //perceives the nest

    /**
     * Initializes a new instance
     * @param s the sensor
     * that this NNInput will
     * use
     */
    public NestRangeAndBearingNNInput( Sensor s ) {
        super(s);
        this.nestSensor = ( NestRangeAndBearingSensor ) s;
        
    }
    
    @Override
    public int getNumberOfInputValues() {
        return 2;
    }

    @Override
    public double getValue( int index ) {
        
        return nestSensor.getSensorReading( index );
        
    }
    
}
