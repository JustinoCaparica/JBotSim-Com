/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolutionaryrobotics.neuralnetworks.inputs;

import simulation.robot.sensors.RoleOwnSensor;
import simulation.robot.sensors.RoleSensor;
import simulation.robot.sensors.Sensor;

/**
 * NNInput to get the readings
 * from the RoleOwnSensor
 * @author gus
 */
public class RoleOwnNNInput extends NNInput {

    private final RoleOwnSensor roleOwnSensor;  //sensor that
                                                //perceives the role of the 
                                                //robot that owns the sensor,
                                                //in the previous time step

    /**
     * Initializes a new instance
     * @param s the sensor
     * that this NNInput will
     * use
     */
    public RoleOwnNNInput( Sensor s ) {
        super(s);
        this.roleOwnSensor = ( RoleOwnSensor ) s;
    }
    
    @Override
    public int getNumberOfInputValues() {
        return 1;
    }

    @Override
    public double getValue( int index ) {
        
        return roleOwnSensor.getSensorReading( index );
        
    }
    
}
