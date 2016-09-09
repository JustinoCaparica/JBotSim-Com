/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.sensors;

import java.util.ArrayList;
import simulation.Simulator;
import simulation.physicalobjects.ClosePhysicalObjects;
import simulation.physicalobjects.Nest;
import simulation.physicalobjects.PhysicalObject;
import simulation.physicalobjects.checkers.AllowNestChecker;
import simulation.robot.Robot;
import simulation.robot.actuators.TwoWheelActuator;
import simulation.util.Arguments;
import simulation.util.ArgumentsAnnotation;

/**
 * A sensor to sense the energy level
 * @author gus
 */
public class TwoWheelActuatorEnergySensor extends Sensor {
    
    
    @ArgumentsAnnotation(name = "wheelSpeedUnits", defaultValue = "150", help="units of wheel speed that can be provided by a full charge of energy, for all robot's wheels, usually in (m/s).s")
    private double wheelSpeedUnits;             //number of units of wheel speed
                                                //that can be provided by a 
                                                //full charge of energy
    
    
    @ArgumentsAnnotation(name = "wheelsMaxSpeedLowEnergyFactor", defaultValue = "0.1", help="when energy is low the wheels maximum speed is temporarily multiplied by this value")
    private double wheelsMaxSpeedLowEnergyFactor;   //multiply the wheels maximum speed
                                                    //by this factor when energy is low
    
    
    private double energyMax;                   //starting and maximum energy level
    private double currentEnergy;               //current energy level
    private double lastSpentEnergyValue;        //to store a temporary value 
    
    
    private Robot robot;                        //robot that owns this sensor
    
    private TwoWheelActuator wheelActuator;     //two wheel actuator whose
                                                //energy level is being sensed
    
    private double wheelsMaxSpeed;              //wheels maximum speed
    
    
    private ClosePhysicalObjects closeObjects;  //variables to determine
    private Simulator simulator;                //if the robot is in the nest
    
    
    
    /**
     * Initializes a new sensor
     * @param simulator the simulator
     * @param id the sensor id
     * @param robot the robot that owns
     * the sensor
     * @param args arguments 
     */
    public TwoWheelActuatorEnergySensor( Simulator simulator, int id, Robot robot, Arguments args ) {
            super(simulator, id, robot, args);
            this.simulator = simulator;
            
            this.robot = robot;
            
            
            wheelSpeedUnits = args.getArgumentAsDoubleOrSetDefault("wheelSpeedUnits", 150);
            wheelsMaxSpeedLowEnergyFactor = args.getArgumentAsDoubleOrSetDefault("wheelsMaxSpeedLowEnergyFactor", 0.1);
            
            energyMax = 1.0;
            currentEnergy = energyMax;
    }
    
    
    
    @Override
    public void update( double time, ArrayList<PhysicalObject> teleported ) {
        
        if(closeObjects != null)                        //these lines seam
            closeObjects.update(time, teleported);      //mandatory; I dont know why
        
        
        
        
        //initialize the wheel actuator
        if ( wheelActuator == null ) {
            wheelActuator = (TwoWheelActuator) robot.getActuatorByType( TwoWheelActuator.class );
            wheelsMaxSpeed = wheelActuator.getMaxSpeed();
            lastSpentEnergyValue = wheelActuator.getSpentEnergy();
        }
        
        
        if ( isInNest() ) {                     //robot in nest
            currentEnergy = energyMax;          //recharge energy
            lastSpentEnergyValue = wheelActuator.getSpentEnergy();
            wheelActuator.setMaxSpeed( wheelsMaxSpeed );
        }
        else{
            currentEnergy = energyMax - ( ( wheelActuator.getSpentEnergy() - lastSpentEnergyValue ) / wheelSpeedUnits);
            if ( currentEnergy <= 0 ) {
                currentEnergy = 0;
                wheelActuator.setMaxSpeed( wheelsMaxSpeed * wheelsMaxSpeedLowEnergyFactor );
                robot.setEnabled( false );
            }

        }
        
        
        
        
    }

    /**
     * Gets the current energy level
     * @return the current
     * energy level
     */
    public double getCurrentEnergy() {
        return currentEnergy;
    }
    
    
    
    
    
    
    @Override
    public double getSensorReading( int sensorNumber ) {
        return currentEnergy;
    }
    
    
    
    /**
     * Determines if the robot
     * is in the nest
     * @return true if the
     * robot is in the nest,
     * false otherwise
     */
    public boolean isInNest() {
		
		if(closeObjects == null) {
			//first call
			double radius = 0;
			for(PhysicalObject p : simulator.getEnvironment().getAllObjects()) {
				if(p instanceof Nest)
					radius = Math.max(radius, p.getRadius());
			}
			
			this.closeObjects = new ClosePhysicalObjects(simulator.getEnvironment(),
					radius,
					new AllowNestChecker());
		}
		
		ClosePhysicalObjects.CloseObjectIterator nestIterator = closeObjects.iterator();

		while (nestIterator.hasNext()) {
			PhysicalObject nest = nestIterator.next().getObject();
			if (nest.getPosition().distanceTo(robot.getPosition()) < nest
					.getRadius())
				return true;
		}
		return false;
	}
    
    
}
