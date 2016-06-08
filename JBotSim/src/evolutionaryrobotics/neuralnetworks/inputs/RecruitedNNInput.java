/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolutionaryrobotics.neuralnetworks.inputs;

import simulation.robot.sensors.RecruitedSensor;
import simulation.robot.sensors.RecruiterSensor;
import simulation.robot.sensors.Sensor;

/**
 * NNInput to get the readings
 * from the recruited sensor
 * @author gus
 */
public class RecruitedNNInput extends NNInput {

    private RecruitedSensor recruitedSensor;            //sensor that
                                                        //perceives the recruited

    /**
     * Initializes a new instance
     * @param s the sensor
     * that this NNInput will
     * use
     */
    public RecruitedNNInput( Sensor s ) {
        super(s);
    }
    
    @Override
    public int getNumberOfInputValues() {
        return 2;
    }

    @Override
    public double getValue( int index ) {
        
        return recruitedSensor.getSensorReading( index );
        
    }
    
}
