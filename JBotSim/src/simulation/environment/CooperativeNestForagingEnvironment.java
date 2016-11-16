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
import simulation.robot.sensors.PreySensor;
import simulation.util.Arguments;
import simulation.util.ArgumentsAnnotation;


/**
 * An Environment where preys must be transported to the nest by 
 * more than one robot. Thus, robots must cooperate
 * to transport preys.
 * @author gus
 */
public class CooperativeNestForagingEnvironment extends Environment {

    private static final double PREY_RADIUS = 0.025;
    @ArgumentsAnnotation(name="preyRadius", help="prey radius", defaultValue="0.025")
    private Double preyRadius;
    
    
    
    private static final double PREY_MASS = 1;
    @ArgumentsAnnotation(name="preyMass", help="prey mass", defaultValue="1")
    private Double preyMass;
    
    
    
    private static final Double CLOSEST_RADIUS = 0.07;
    @ArgumentsAnnotation(name="closestRadius", help="radius around each prey that robots must occupy simultaneously to capture the prey", defaultValue="0.07")
    private Double closestRadius;
    
    private static final int TEAM_SIZE = 2;
    @ArgumentsAnnotation(name="teamSize", help="number of robots required to capture a prey", defaultValue="2")
    private int teamSize;
    
    
    private static final int WALLED = 0;
    @ArgumentsAnnotation(name="wall", defaultValue="1")     //does the environment
    private boolean walled;                                 //have a wall?
    
    

    @ArgumentsAnnotation(name="nestlimit", defaultValue="0.5")
    private double nestLimit;

    @ArgumentsAnnotation(name="foragelimit", defaultValue="2.0")
    private double forageLimit;

    
    private static final double FORAGE_START_LIMIT = 0.80;
    @ArgumentsAnnotation(name="forageStartLimit", defaultValue="0.8")
    private final double forageStartLimit;
    
    
    private static final int RANDOMIZE_NEST_POSITION = 0;
    @ArgumentsAnnotation(name="randomizeNestPosition", defaultValue="0", help="if set to 1, the nest position is randomized")
    private final boolean randomizeNestPosition;
    
    
    private static final int RANDOMIZE_ROBOTS_POSITION = 0;
    @ArgumentsAnnotation(name="randomizeRobotsPosition", defaultValue="0", help="if set to 1, all robots positions are randomized")
    private final boolean randomizeRobotsPosition;
    
    
    @ArgumentsAnnotation(name="forbiddenarea", defaultValue="5.0")
    private double forbiddenArea;

    private static final int NUMBER_OF_PREYS = 10;
    @ArgumentsAnnotation(name="numberofpreys", defaultValue="10", help="number of preys in the environment")
    private int numberOfPreys;

    
    private Nest nest;                              //the nest
    
    
    private Double lastPreyCaptureTime;             //moment when the last
                                                    //prey was captured
    
    private int numberOfFoodSuccessfullyForaged = 0;        
    private Random random;

    
    
    private Wall wall;                          //environment wall, if exists
    
    
    private Simulator simulator;
    private Arguments args;

    
    public CooperativeNestForagingEnvironment(Simulator simulator, Arguments arguments) {
        
            super(simulator, arguments);
            
            this.simulator      = simulator;
            this.args           = arguments;
            
            nestLimit           = arguments.getArgumentIsDefined("nestlimit") ? arguments.getArgumentAsDouble("nestlimit")       : .5;
            forageLimit         = arguments.getArgumentIsDefined("foragelimit") ? arguments.getArgumentAsDouble("foragelimit")       : 2.0;
            forageStartLimit    = arguments.getArgumentAsDoubleOrSetDefault("forageStartLimit", FORAGE_START_LIMIT);
            forbiddenArea       = arguments.getArgumentIsDefined("forbiddenarea") ? arguments.getArgumentAsDouble("forbiddenarea")       : 5.0;
            randomizeNestPosition = arguments.getArgumentAsIntOrSetDefault("randomizeNestPosition", RANDOMIZE_NEST_POSITION) == 1;
            
            
            randomizeRobotsPosition = arguments.getArgumentAsIntOrSetDefault("randomizeRobotsPosition", RANDOMIZE_ROBOTS_POSITION) == 1;
            
            
            closestRadius       = arguments.getArgumentAsDoubleOrSetDefault("closestRadius", CLOSEST_RADIUS);
            teamSize            = arguments.getArgumentAsIntOrSetDefault("teamSize", TEAM_SIZE);
            
            walled              = arguments.getArgumentAsIntOrSetDefault("wall", WALLED) == 1;
            
            
            preyMass            = arguments.getArgumentAsDoubleOrSetDefault("preyMass", PREY_MASS);
            preyRadius          = arguments.getArgumentAsDoubleOrSetDefault("preyRadius", PREY_RADIUS);
            
            lastPreyCaptureTime = 0.0;
    }
	
    
    
    
    @Override
    public void setup(Simulator simulator) {
            
        super.setup(simulator);

        if(simulator.getRobots().size() == 1) {
                Robot r = simulator.getRobots().get(0);
                r.setPosition(0, 0);
                r.setOrientation(0);
        }
        
        this.random = simulator.getRandom();

        
        if ( !randomizeNestPosition ) {                         
                                                                //place nest at center
            nest = new Nest(simulator, "Nest", 0, 0, nestLimit);
        }else{
                                                                //randomize nest position
            double x = random.nextDouble() * (forageLimit*2) - forageLimit;
            double y = random.nextDouble() * (forageLimit*2) - forageLimit;
            nest = new Nest(simulator, "Nest", x, y, nestLimit);
        }
        
        addObject(nest);
        
        
        
        
        
        
        
        
        //create preprogrammed robots
        Arguments argsPreprog = simulator.getArguments().get("--preprogrammed");
        if ( argsPreprog != null ) {
            int numberOfWalkers = argsPreprog.getArgumentAsIntOrSetDefault("numberofrobots",1);
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
                robot.setPosition( simulator.getRandom().nextDouble() * 2 * forageLimit - forageLimit, 
                                   simulator.getRandom().nextDouble() * 2 * forageLimit - forageLimit);
                robot.setOrientation( simulator.getRandom().nextDouble() * 2 * Math.PI );
            }
        }else{
            for ( Robot robot : simulator.getRobots() ) {       //place robots at nest
                robot.setPosition( nest.getPosition().getX(), nest.getPosition().getY());
                robot.setOrientation( simulator.getRandom().nextDouble() * 2 * Math.PI );
            }
        }
        
       
        
        
        



//        if(args.getArgumentIsDefined("densityofpreys")){
//                double densityoffood = args.getArgumentAsDouble("densityofpreys");
//                numberOfPreys = (int) ( densityoffood * Math.PI * forageLimit * forageLimit + .5 );
//        } 
//        else if ( args.getArgumentIsDefined("densityofpreysValues") ) {
//                String[] rawArray = args.getArgumentAsString("densityofpreysValues").split(",");
//
//                if(rawArray.length > 1){                        //randomize number of preys
//                        double densityoffood = Double.parseDouble(rawArray[simulator.getRandom().nextInt(rawArray.length)]);
//                        numberOfPreys = (int) ( densityoffood * Math.PI * forageLimit * forageLimit + .5 );
//                }
//        } else {
//                numberOfPreys = args.getArgumentIsDefined("numberofpreys") ? args.getArgumentAsInt("numberofpreys") : 20;
//        }

        
        numberOfPreys = args.getArgumentAsIntOrSetDefault("numberofpreys", NUMBER_OF_PREYS);


        for(int i = 0; i < numberOfPreys; i++ ){
                addPrey(new Prey(simulator, "Prey "+i, newRandomPosition(), 0, preyMass, preyRadius));
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

    
    private Vector2d newRandomPosition() {
        
        double radius = random.nextDouble() * (forageLimit-forageStartLimit) + forageStartLimit;
        //double radius = forageLimit;
        double angle = random.nextDouble()*2*Math.PI;
        
        return new Vector2d( radius * Math.cos(angle), radius * Math.sin(angle) );
    
    }
	
    
    
    
    
    
    
    
    @Override
    public void update(double time) {
//		nest.shape.getClosePrey().update(time, teleported);
//		CloseObjectIterator i = nest.shape.getClosePrey().iterator();

        List<Robot> closeRobots;    //robots within radius of prey
        List<Robot> enabledRobots;  //enabled robots within radius of prey
        enabledRobots = new LinkedList<>();
       
        for(Prey currentPrey : simulator.getEnvironment().getPrey() ) {

            enabledRobots.clear();
            
            if( currentPrey.getPosition().distanceTo( nest.getPosition() ) <= nest.getRadius() ) {            
                                                                //prey is at nest
                                                                //move prey to
                //currentPrey.teleportTo( newRandomPosition() );//new position

                currentPrey.teleportTo( new Vector2d(100, 100) );
                numberOfFoodSuccessfullyForaged++;              //account for
                                                                //foraged prey
                lastPreyCaptureTime = time;
                
                
                if ( numberOfFoodSuccessfullyForaged == numberOfPreys ) {
                    simulator.stopSimulation();         //no more preys:
                }                                       //stop simulation, save time
            }
        }



        
    }

    /**
     * Gets the time stamp of the moment
     * when the last prey was captured
     * @return the time stamp
     */
    public Double getLastPreyCaptureTime() {
        return lastPreyCaptureTime;
    }
	
    
    
    
    
    
    
    
    
    
    public int getNumberOfFoodSuccessfullyForaged() {
            return numberOfFoodSuccessfullyForaged;
    }

    public double getNestRadius() {
            return nestLimit;
    }

    public double getForageRadius() {
            return forageLimit;
    }

    public double getForbiddenArea() {
            return forbiddenArea;
    }
    
    /**
     * Gets the initial number of
     * preys
     * @return the initial number 
     * of preys 
     */
    public int getNumberOfPreys(){
        return numberOfPreys;
    }
    
    
    
}
