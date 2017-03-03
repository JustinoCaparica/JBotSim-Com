package simulation.environment;

import controllers.RandomRobotController;
import controllers.RandomWalkerController;
import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import mathutils.Vector2d;
import simulation.Simulator;
import simulation.physicalobjects.Nest;
import simulation.physicalobjects.Prey;
import simulation.physicalobjects.Wall;
import simulation.robot.Robot;
import simulation.robot.actuators.TwoWheelActuator;
import simulation.robot.sensors.NestSensor;
import simulation.robot.sensors.PreySensor;
import simulation.util.Arguments;
import simulation.util.ArgumentsAnnotation;


/**
 * An Environment where robots must get back to the nest
 * @author gus
 */
public class ReturnToNestEnvironment extends Environment {

    
    
    private static final int TEAM_SIZE = 2;
    @ArgumentsAnnotation(name="teamSize", help="number of robots required to capture a prey", defaultValue="2")
    private int teamSize;
    
    
    private static final int WALLED = 1;
    @ArgumentsAnnotation(name="walled", defaultValue="1")     //does the environment
    private boolean walled;                                 //have a wall?
    
    
    private static final int ARENA_SIZE = 4;
    @ArgumentsAnnotation(name="arenaSize", help="arena size, the arena is a square", defaultValue="4")
    private int arenaSize;
    
    

    @ArgumentsAnnotation(name="nestlimit", defaultValue="0.5")
    private double nestLimit;


    
    
    private static final int RANDOMIZE_NEST_POSITION = 0;
    @ArgumentsAnnotation(name="randomizeNestPosition", defaultValue="0", help="if set to 1, the nest position is randomized")
    private final boolean randomizeNestPosition;
    
    
    private static final int RANDOMIZE_ROBOTS_POSITION = 0;
    @ArgumentsAnnotation(name="randomizeRobotsPosition", defaultValue="0", help="if set to 1, all robots positions are randomized")
    private final boolean randomizeRobotsPosition;
    
    
    
    

    
    private Nest nest;                              //the nest
    
    private Random random;

    
    private Wall wall;                          //environment wall, if exists
    
    
    private Simulator simulator;
    private Arguments args;

    
    public ReturnToNestEnvironment(Simulator simulator, Arguments arguments) {
        
            super(simulator, arguments);
            
            this.simulator      = simulator;
            this.args           = arguments;
            this.random         = simulator.getRandom();
            
            
            nestLimit           = arguments.getArgumentIsDefined("nestlimit") ? arguments.getArgumentAsDouble("nestlimit")       : .5;
            
            randomizeNestPosition = arguments.getArgumentAsIntOrSetDefault("randomizeNestPosition", RANDOMIZE_NEST_POSITION) == 1;
            
            
            randomizeRobotsPosition = arguments.getArgumentAsIntOrSetDefault("randomizeRobotsPosition", RANDOMIZE_ROBOTS_POSITION) == 1;
            
            
            teamSize            = arguments.getArgumentAsIntOrSetDefault("teamSize", TEAM_SIZE);
            
            walled              = arguments.getArgumentAsIntOrSetDefault("walled", WALLED) == 1;
            
            
            arenaSize          = arguments.getArgumentAsIntOrSetDefault("arenaSize", ARENA_SIZE);
    }
	
    
    
    
    @Override
    public void setup(Simulator simulator) {
            
        super.setup(simulator);

        
        for (int i = 0; i < teamSize; i++) {            //place team robots
            getRobots().get(i).setPosition(0, 0);       //at the center
            
            if ( i != 0 ) {                             //turn off nest sensor
                ((NestSensor)getRobots().get(i).getSensorByType( NestSensor.class )).setEnabled( false );
            }
        }
        
        

        
        if ( !randomizeNestPosition ) {                         
                                                                //place nest at center
            nest = new Nest(simulator, "Nest", 0, 0, nestLimit);
        }else{
                                                                //randomize nest position
            double x = random.nextDouble() * (arenaSize*2) - arenaSize;
            double y = random.nextDouble() * (arenaSize*2) - arenaSize;
            nest = new Nest(simulator, "Nest", x, y, nestLimit);
        }
        
        addObject(nest);
        
        
        
        
        
        
        
        
        //create preprogrammed robots
        Arguments argsPreprog = simulator.getArguments().get("--preprogrammed");
        if ( argsPreprog != null ) {
            int numberOfWalkers = argsPreprog.getArgumentAsIntOrSetDefault("numberofrobots",1);
            argsPreprog.setArgument("maxDistToCenter", arenaSize);
            for(int i = 0; i < numberOfWalkers; i++){
                    Robot walker = Robot.getRobot(simulator, argsPreprog);
                    walker.setController(new RandomWalkerController(simulator, walker, argsPreprog));
                    walker.setBodyColor( Color.yellow );
                    walker.setPosition( nest.getPosition().getX() + simulator.getRandom().nextDouble() * 0.15 - 0.075, nest.getPosition().getY() + simulator.getRandom().nextDouble() * 0.15 - 0.075 );
                    
                    walker.setOrientation( simulator.getRandom().nextDouble() * 2 * Math.PI );
                    addRobot(walker);
            }
        }
        
        
        if ( randomizeRobotsPosition ) {                        //randomize robots positions
            for ( Robot robot : simulator.getRobots() ) {       //within forage area
                robot.setPosition( simulator.getRandom().nextDouble() * 2 * arenaSize - arenaSize, 
                                   simulator.getRandom().nextDouble() * 2 * arenaSize - arenaSize);
                robot.setOrientation( simulator.getRandom().nextDouble() * 2 * Math.PI );
            }
        }else{
            for ( Robot robot : simulator.getRobots() ) {       //place robots at nest
                //robot.setPosition( nest.getPosition().getX(), nest.getPosition().getY());
                robot.setPosition( 0, 0);                       //place robots at center
                robot.setOrientation( simulator.getRandom().nextDouble() * 2 * Math.PI );
            }
        }
        
       
        
        
        
        
        int wallSize = 2;
        if ( walled ) {
            wall = new Wall( simulator, -wallSize, 0, 0.10, 2*wallSize);
            super.addObject( wall );
            
            wall = new Wall( simulator, wallSize, 0, 0.10, 2*wallSize);
            super.addObject( wall );
            
            wall = new Wall( simulator, 0, -wallSize, 2*wallSize, 0.10);
            super.addObject( wall );
            
            wall = new Wall( simulator, 0, wallSize, 2*wallSize, 0.10);
            super.addObject( wall );
        }
        
            
    }

    
    /**
     * Generates a new random position within
     * the environment
     * @return a Vector2d representing
     * the position
     */
    private Vector2d newRandomPosition() {
        
        double radius, angle;
        
        radius = random.nextDouble() * arenaSize;
        angle = random.nextDouble()*2*Math.PI;
        return new Vector2d( radius * Math.cos(angle), radius * Math.sin(angle) );
    
    }
	
    
    
    @Override
    public void update(double time) {

        
        //stop simulation if all team robots are inside the nest
        if ( isTeamInNest() ) {
            simulator.stopSimulation();
        }
        

    }

    

    public double getNestRadius() {
            return nestLimit;
    }

    
    
    /**
     * Determines if all team elements
     * are inside the nest
     * @return True if all team
     * elements are inside the nest
     */
    public Boolean isTeamInNest() {
        
        
        Robot r;
        
        for (int i = 0; i < teamSize; i++) {        //if there is a team robot
            r = robots.get(i);                      //outside the nest
            if ( r.getPosition().distanceTo(nest.getPosition()) > nestLimit ) {
                return false;                       //return false
            }
        }
        
                                                    //all team robots are
        return true;                                //inside the nest
        
    }

    
    
    
    
}
