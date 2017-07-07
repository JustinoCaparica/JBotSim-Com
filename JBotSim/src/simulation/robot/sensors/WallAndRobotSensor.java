/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.sensors;

import java.util.ArrayList;
import simulation.Simulator;
import simulation.physicalobjects.PhysicalObject;
import simulation.physicalobjects.PhysicalObjectDistance;
import simulation.physicalobjects.checkers.AllowWallRobotChecker;
import simulation.robot.Robot;
import simulation.util.Arguments;

/**
 * A sensor to detect robots
 * and walls. This sensor detects
 * a robot or a wall without knowing
 * if it is detecting a robot or
 * a wall
 * @author guest
 */
public class WallAndRobotSensor extends Sensor {

    private RobotSensor robotSensor;            //a sensor to detect robots
    private WallRaySensor wallSensor;           //a sensor to detect walls

    
    private Double robotSensorContrib;          //variables to store readings
    private Double wallSensorContrib;           //from both sensors
                                                //declared here for efficiency
    
    
    public WallAndRobotSensor(Simulator simulator, int id, Robot robot, Arguments args) {
        super(simulator, id, robot, args);
        //setAllowedObjectsChecker( new AllowWallRobotChecker(robot.getId()) );
        
        //* the id argument passed to the sensors created bellow
        // should not be the same for both sensors
        robotSensor = new RobotSensor(simulator, id, robot, args);
        wallSensor  = new WallRaySensor(simulator, id+5000, robot, args);
        
    }
    
    public void update(double time, ArrayList<PhysicalObject> teleported) {
        robotSensor.update(time, teleported);
        wallSensor.update(time, teleported);
    }
    

    @Override
    public double getSensorReading(int sensorNumber) {
        robotSensorContrib = robotSensor.getSensorReading(sensorNumber);
        wallSensorContrib  = wallSensor.getSensorReading(sensorNumber);

//        System.out.println("sensorNumber:" + sensorNumber);
//        System.out.println("robotSensorContrib:" + robotSensorContrib);
//        System.out.println("wallSensorContrib:" + wallSensorContrib);
//        System.out.println("");

        if ( robotSensorContrib > wallSensorContrib ) {     //return higher
            return robotSensorContrib;                      //contribution
        }else{
            return wallSensorContrib;
        }
    }

    
    /**
     * Gets the number of individual
     * sensors around the robot's body
     * @return an integer representing
     * the number of sensors
     */
    public int getNumberOfSensors() {
        return robotSensor.getNumberOfSensors();
    }
    
    
    
    
}
