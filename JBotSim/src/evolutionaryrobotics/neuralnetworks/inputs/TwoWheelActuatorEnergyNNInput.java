/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolutionaryrobotics.neuralnetworks.inputs;

import simulation.robot.sensors.Sensor;
import simulation.robot.sensors.TwoWheelActuatorEnergySensor;

/**
 * NNInput to get the readings
 * from the TwoWheelActuatorEnergySensor
 * @author gus
 */
public class TwoWheelActuatorEnergyNNInput extends NNInput {

    private TwoWheelActuatorEnergySensor sensor;        //the sensor

    public TwoWheelActuatorEnergyNNInput(Sensor s) {
        super(s);
        this.sensor = (TwoWheelActuatorEnergySensor) s;
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
