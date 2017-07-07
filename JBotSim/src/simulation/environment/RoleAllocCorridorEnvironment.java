package simulation.environment;

import java.util.ArrayList;
import java.util.Random;
import mathutils.Vector2d;
import simulation.Simulator;
import simulation.physicalobjects.Nest;
import simulation.physicalobjects.Wall;
import simulation.robot.Robot;
import simulation.robot.actuators.RoleActuator;
import simulation.robot.actuators.TwoWheelActuator;
import simulation.util.Arguments;
import simulation.util.ArgumentsAnnotation;


/**
 * An Environment where robots must designate
 * one of them to get to the nest while
 * while all other keep way from the nest
 * @author gus
 */
public class RoleAllocCorridorEnvironment extends Environment {

  
    
    
    
    
    
    
    private static final int WALLED = 1;
    @ArgumentsAnnotation(name="wall", defaultValue="1")     //does the environment
    private boolean walled;                                 //have a wall?
    
    
    
                                                            //minimum distance to wall for any robot
    private static final double DIST_FROM_WALL = 0.0;
    @ArgumentsAnnotation(name="distFromWall", defaultValue="0.0", help="minimum distance to wall for any robot")   
    private double distFromWall = 0.0;                              
    
    
    
    private Nest nest;                          //the nest 
    
    
    private Random random;

    
    
    private Wall wall;                          //environment wall, if exists
    
    
    private Simulator simulator;
    

    
    public RoleAllocCorridorEnvironment(Simulator simulator, Arguments arguments) {
        
            super(simulator, arguments);
            
            this.simulator      = simulator;
            
            
            
            walled              = arguments.getArgumentAsIntOrSetDefault("wall", WALLED) == 1;
            distFromWall        = arguments.getArgumentAsDoubleOrSetDefault("distFromWall", DIST_FROM_WALL);
            
            //distFromWall = 0.18;//test line; remove when test is done
            
    }
	
    
    
    
    @Override
    public void setup(Simulator simulator) {
            
        super.setup(simulator);

       
        this.random = simulator.getRandom();

        
        
       
        
       
        
        
        double arenaWidth       = width;
        double arenaHeight      = arenaWidth * (6.0/11.0);
        
        double corridorLength   = arenaWidth * (5.0/11.0);
        double corridorHeight   = arenaHeight / 3;
        
        double corridorStartX   = (arenaWidth/2.0) - corridorLength;
        
        double wallThick = 0.0025;
        
        
        double leftWallX = 0.0;
        
        if ( walled ) {
            
            leftWallX = -arenaWidth / 2;
            wall = new Wall( simulator, leftWallX, 0, wallThick, arenaHeight);
            super.addObject( wall );                        //left wall
            
            
            
            double topWallLength = arenaHeight;
            double topWallX = corridorStartX - ( (topWallLength) / 2.0 );
            wall = new Wall( simulator, topWallX, arenaHeight/2, topWallLength, wallThick );
            super.addObject( wall );                        //top wall
            

            wall = new Wall( simulator, topWallX, -arenaHeight/2, topWallLength, wallThick );
            super.addObject( wall );                        //bottom wall
            
            
            double sideWallsLength = (arenaHeight - corridorHeight) / 2.0;
            wall = new Wall( simulator, corridorStartX, corridorHeight/2 + sideWallsLength/2, wallThick, sideWallsLength );
            super.addObject( wall );                        //arena right top wall
            
            wall = new Wall( simulator, corridorStartX, -corridorHeight/2 - sideWallsLength/2, wallThick, sideWallsLength );
            super.addObject( wall );                        //arena right bottom wall
            
            
            
            
            //* corridor walls
            double rightWallX = arenaWidth/2.0;
            wall = new Wall( simulator, rightWallX, 0, wallThick, corridorHeight );
            super.addObject( wall );                        //corridor right wall
            
            
            wall = new Wall( simulator, corridorStartX + (corridorLength/2), corridorHeight/2, corridorLength, wallThick );
            super.addObject( wall );                        //corridor top wall
            
            
            wall = new Wall( simulator, corridorStartX + (corridorLength/2), -corridorHeight/2, corridorLength, wallThick );
            super.addObject( wall );                        //corridor bottom wall
            
            
        }
        
        
        nest = new Nest(simulator, null, corridorLength - 0.01, 0, 0.02);
        super.addObject(nest);

        
//        simulator.getRobots().get(0).setPosition(0.3, 0.00);
//        simulator.getRobots().get(1).setPosition(-0.4, 0.00);
        //simulator.getRobots().get(0).setOrientation( Math.PI );
       
//        simulator.getRobots().get(2).setPosition(-0.4, 0.05);
//        simulator.getRobots().get(3).setPosition(-0.4, -0.05);
        
        
//        for (Robot robot : simulator.getRobots()) {
//            ((TwoWheelActuator) robot.getActuatorByType(TwoWheelActuator.class)).setMaxSpeed(0.01);
//        }
        
        
//        for (Robot robot : simulator.getRobots()) {
//            robot.setPosition(0, 0);
//        }
        
        //TODO use the robot diameter to set the minimum distance 
        //from obstacles
        for (Robot robot : simulator.getRobots()) {
//            
//            //test code start
//            if ( robot.getId() == 0 ) {
//                robot.setPosition( -0.20 , 0.00 );
//                robot.setOrientation( 0.0 * -Math.PI );
//                ((TwoWheelActuator) robot.getActuatorByType(TwoWheelActuator.class)).setMaxSpeed(0.0);
//            }
//            else if( robot.getId() == 1 ){
//                robot.setPosition( -0.274 , 0.00 );
//                robot.setOrientation( 0.0 * -Math.PI );
//                ((TwoWheelActuator) robot.getActuatorByType(TwoWheelActuator.class)).setMaxSpeed(0.0);
//            }
//            else if( robot.getId() == 2 ){
//                robot.setPosition( -0.513 , 0.00 );
//                robot.setOrientation( 1.0 * Math.PI );
//                ((TwoWheelActuator) robot.getActuatorByType(TwoWheelActuator.class)).setMaxSpeed(0.0);
//            }
//            else if( robot.getId() == 3 ){
//                robot.setPosition( -0.38 , -0.13 );
//                robot.setOrientation( 0.0 * Math.PI );
//                //((TwoWheelActuator) robot.getActuatorByType(TwoWheelActuator.class)).setMaxSpeed(0.0);
//            }
//            else{
//                do{
//                    robot.setPosition( (random.nextDouble() * 2 - 1) * 0.1 - 0.2, ( random.nextDouble() * 2 - 1) * 0.075 );
//                    robot.setOrientation( random.nextDouble() * Math.PI);
//                }
//                while( !robotAtSafePosition(robot, getRobots(), robot.getDiameter()*1.5 ) );
//            }
            //test code end
            
            
            //TODO change to code to allow safe placement of robots
            // in the environment without entering infinite cycle
            if ( distFromWall != 0.0 ) {
                //make sure robot is positioned at least
                //a given distance from any wall
                do{
                    robot.setPosition( (random.nextDouble() * 2 - 1) * (arenaHeight/2  - distFromWall) - Math.abs(leftWallX + arenaHeight/2) ,
                                     (  random.nextDouble() * 2 - 1) * (arenaHeight/2 - distFromWall) );
                    robot.setOrientation( random.nextDouble() * Math.PI);
                }
                while( !robotAtSafePosition(robot, getRobots(), robot.getDiameter()*1.5 ) );
            }
            else{
                //make sure robot is positioned at least
                //at half its own body diameter from any other robot
                //do{
                //    robot.setPosition( (random.nextDouble() * 2 - 1) * (arenaHeight/2  - robot.getDiameter()) - Math.abs(leftWallX + arenaHeight/2) ,
                //                     (  random.nextDouble() * 2 - 1) * (arenaHeight/2 - robot.getDiameter()) );
                //    robot.setOrientation( random.nextDouble() * Math.PI);
                //}
                //while( !robotAtSafePosition(robot, getRobots(), robot.getDiameter()*1.5 ) );
                robot.setPosition( (random.nextDouble() * 2 - 1) * (arenaHeight/2  - robot.getDiameter()) - Math.abs(leftWallX + arenaHeight/2) ,
                                   (  random.nextDouble() * 2 - 1) * (arenaHeight/2 - robot.getDiameter()) );
                robot.setOrientation( random.nextDouble() * Math.PI);
            }
            
            
            
            
            
            
               
            //while( false );    
            
            //((TwoWheelActuator) robot.getActuatorByType(TwoWheelActuator.class)).setMaxSpeed(0.0);
        }
        
        
            
    }

    
    private Vector2d newRandomPosition() {
        
        
        double radius = random.nextDouble() * width;
        double angle = random.nextDouble()*2*Math.PI;
        
        return new Vector2d( radius * Math.cos(angle), radius * Math.sin(angle) );
    
    }
	
    
    
    
    
    
    
    
    @Override
    public void update(double time) {

    }

    /**
     * Gets the nest on the
     * environment
     * @return the nest or
     * null if there is none
     */
    public Nest getNest() {
        return nest;
    }

    
    /**
     * Determines if a robot position
     * is at more than a distance to
     * all other robots
     * @param robot the robot
     * @param robots all other robots
     * @param distance the distance, excl
     * @return true if a position is at
     * more than the distance to all
     * other robots
     */
    private boolean robotAtSafePosition( Robot robot, 
                                         ArrayList<Robot> robots,
                                         Double distance ) {
        
        boolean atSafePosition = true;
        
        for (int i = 0; i < robots.size() && atSafePosition; i++) {
            
            if ( !robot.equals(robots.get(i)) ) {
                double dist = robot.getPosition().distanceTo(robots.get(i).getPosition());
                if ( robot.getPosition().distanceTo(robots.get(i).getPosition()) <= distance ) {
                    atSafePosition = false;
                }
            }
            
        }
        
        return atSafePosition;
        
    }


    
    
    
    
    
    
    
    
    
    
    
    
    
}
