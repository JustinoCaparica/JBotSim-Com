/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolutionaryrobotics.neuralnetworks.inputs;

import simulation.robot.sensors.RecruitSensor;
import simulation.robot.sensors.RoleSensor;
import simulation.robot.sensors.Sensor;

/**
 * NNInput to get the readings
 * from the RoleSensor
 * @author gus
 */
public class RoleNNInput extends NNInput {

    private final RoleSensor roleSensor;        //sensor that
                                                //perceives the role of other 
                                                //robots

    /**
     * Initializes a new instance
     * @param s the sensor
     * that this NNInput will
     * use
     */
    public RoleNNInput( Sensor s ) {
        super(s);
        this.roleSensor = ( RoleSensor ) s;
    }
    
    @Override
    public int getNumberOfInputValues() {
        return 1;
    }

    @Override
    public double getValue( int index ) {
        
        return roleSensor.getSensorReading( index );
        
    }
    
}
