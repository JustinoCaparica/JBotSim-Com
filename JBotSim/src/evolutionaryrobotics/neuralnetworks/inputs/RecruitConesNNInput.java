/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolutionaryrobotics.neuralnetworks.inputs;

import simulation.robot.sensors.RecruitConesSensor;
import simulation.robot.sensors.Sensor;

/**
 * NNInput to get the readings
 * from the recruit cones sensor
 * @author gus
 */
public class RecruitConesNNInput extends NNInput {

    private final RecruitConesSensor recruitSensor;     //sensor that
                                                        //perceives the recruited

    /**
     * Initializes a new instance
     * @param s the sensor
     * that this NNInput will
     * use
     */
    public RecruitConesNNInput( Sensor s ) {
        super(s);
        this.recruitSensor = ( RecruitConesSensor ) s;
    }
    
    @Override
    public int getNumberOfInputValues() {
        return 1;
    }

    @Override
    public double getValue( int index ) {
        
        return recruitSensor.getSensorReading( index );
        
    }
    
}
