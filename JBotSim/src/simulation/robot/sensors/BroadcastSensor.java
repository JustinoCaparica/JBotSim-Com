/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.sensors;

import mathutils.Vector2d;
import simulation.Simulator;
import simulation.physicalobjects.GeometricInfo;
import simulation.physicalobjects.PhysicalObjectDistance;
import simulation.physicalobjects.PhysicalObjectType;
import simulation.physicalobjects.checkers.AllowAllRobotsChecker;
import simulation.robot.Robot;
import simulation.robot.actuators.BroadcastActuator;
import simulation.robot.actuators.RoleActuator;
import simulation.util.Arguments;
import simulation.util.ArgumentsAnnotation;

/**
 * Sensor to perceive the output value of 
 * a BroadcasActuator of another robot
 * @author guest
 */
public class BroadcastSensor extends ConeTypeSensor {

    
    @ArgumentsAnnotation(name = "seeDisabledRobots", defaultValue = "1", help = "If this value is set to 0 the sensor will not see disabled robots")
    private boolean seeDisabledRobots;
    
    
//    
//    
//    private Simulator simulator;            //the simulator
//    
//    private Robot robot;                    //the robot that owns
//                                            //this sensor
    
    
    
    
    public BroadcastSensor( Simulator simulator, int id, Robot robot, Arguments args ) {
        super(simulator, id, robot, args);
        setAllowedObjectsChecker( new AllowAllRobotsChecker(robot.getId()) );
        
        
        //this.simulator  = simulator;
        //this.robot      = robot;
        
        
        seeDisabledRobots = args.getArgumentAsIntOrSetDefault("seeDisabledRobots", 1) == 1;
    }

    

    @Override
    protected double calculateContributionToSensor( int i, PhysicalObjectDistance source ) {
        
        
        if ( !source.getObject().getType().equals( PhysicalObjectType.ROBOT )) {
            System.out.println("BroadcastSensor:" + " source is NOT a robot. It should be.");
            System.exit( -1 );                      //source is not a robot but it should be
        }
        
        
        
        if ( !seeDisabledRobots && !source.getObject().isEnabled() ) {
            return 0.0;                             //source robot is disabled
        }
        

        GeometricInfo sensorInfo = getSensorGeometricInfo( i, source );

        
        
        
        if ((sensorInfo.getDistance() < getCutOff())
                && (sensorInfo.getAngle() < (openingAngle / 2.0))
                && (sensorInfo.getAngle() > (-openingAngle / 2.0))) {
            
            BroadcastActuator act;
            act = (BroadcastActuator) ((Robot) source.getObject()).getActuatorByType( BroadcastActuator.class );
            //TODO if act == null a NullPointerException is generated
            return act.getValue();
        }
        
        
        return 0;
        
    }
    
    
    

    
    
    
}
