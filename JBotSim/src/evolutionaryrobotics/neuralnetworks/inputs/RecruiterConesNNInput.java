/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolutionaryrobotics.neuralnetworks.inputs;

import simulation.robot.sensors.RecruiterConesSensor;
import simulation.robot.sensors.Sensor;

/**
 * NNInput to get the readings
 * from the recruiter cones sensor
 * @author gus
 */
public class RecruiterConesNNInput extends NNInput {

    private RecruiterConesSensor recruiterSensor;           //sensor that
                                                            //perceives the recruiter

    /**
     * Initializes a new instance
     * @param s the sensor
     * that this NNInput will
     * use
     */
    public RecruiterConesNNInput( Sensor s ) {
        super(s);
        this.recruiterSensor = ( RecruiterConesSensor ) s;
    }
    
    @Override
    public int getNumberOfInputValues() {
        return 1;
    }

    @Override
    public double getValue( int index ) {
        
        return recruiterSensor.getSensorReading( index );
        
    }
    
}
