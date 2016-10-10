/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolutionaryrobotics.neuralnetworks.inputs;

import simulation.robot.sensors.PreyLocationSensor;
import simulation.robot.sensors.Sensor;

/**
 *
 * @author gus
 */
public class PreyLocationNNInput extends NNInput{

    
    private final PreyLocationSensor preyLocationSensor;    //sensor that
                                                            //perceives the prey
    
    
    
    public PreyLocationNNInput(Sensor s) {
        super(s);
        
        this.preyLocationSensor = (PreyLocationSensor) s;
        
    }

    
    
    
    
    @Override
    public int getNumberOfInputValues() {
         return 2;
    }

    @Override
    public double getValue( int index ) {
        return preyLocationSensor.getSensorReading( index );
    }
    
}
