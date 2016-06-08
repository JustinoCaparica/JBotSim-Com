/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolutionaryrobotics.neuralnetworks.inputs;

import simulation.robot.sensors.RecruiterSensor;
import simulation.robot.sensors.Sensor;

/**
 *
 * @author gus
 */
public class RecruiterNNInput extends NNInput {

    private RecruiterSensor recruiterSensor;            //sensor that
                                                        //perceives the recruiter

    /**
     * Initializes a new instance
     * @param s the sensor
     * that this NNInput will
     * use
     */
    public RecruiterNNInput( Sensor s ) {
        super(s);
    }
    
    @Override
    public int getNumberOfInputValues() {
        return 2;
    }

    @Override
    public double getValue( int index ) {
        
        return recruiterSensor.getSensorReading( index );
        
    }
    
}
