/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolutionaryrobotics.neuralnetworks.inputs;

import simulation.robot.sensors.RecruitSensor;
import simulation.robot.sensors.Sensor;

/**
 * NNInput to get the readings
 * from the recruited sensor
 * @author gus
 */
public class RecruitNNInput extends NNInput {

    private final RecruitSensor recruitedSensor;      //sensor that
                                                        //perceives the recruited

    /**
     * Initializes a new instance
     * @param s the sensor
     * that this NNInput will
     * use
     */
    public RecruitNNInput( Sensor s ) {
        super(s);
        this.recruitedSensor = ( RecruitSensor ) s;
    }
    
    @Override
    public int getNumberOfInputValues() {
        return 3;
    }

    @Override
    public double getValue( int index ) {
        
        return recruitedSensor.getSensorReading( index );
        
    }
    
}
