package simulation.environment;

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
import simulation.robot.sensors.PreySensor;
import simulation.util.Arguments;
import simulation.util.ArgumentsAnnotation;


/**
 * An Environment where preys must be transported to the nest.
 * Prey might be clustered in a location (TODO).
 * @author gus
 */
public class CooperativeNestForagingEnvironment extends Environment {

    
    /** Prey parameters **/
    private static final double PREY_RADIUS = 0.025;
    @ArgumentsAnnotation(name="preyRadius", help="prey radius", defaultValue="0.025")
    private Double preyRadius;
    
    private static final double PREY_MASS = 1;
    @ArgumentsAnnotation(name="preyMass", help="prey mass", defaultValue="1")
    private final Double preyMass;
    
    private static final int VARY_PREY_MASS = 0;
    @ArgumentsAnnotation(name="varyPreyMass", help="if set to 1 the prey mass varies betwee preyMass and preyMass+2", defaultValue="0")
    private final Boolean varyPreyMass;
    
    private static final int PREYS_GATHERED = 0;
    @ArgumentsAnnotation(name="preysGathered", defaultValue="0", help="if set to 1, all preys are close to a randomized location")
    private final boolean preysGathered;
    
    private static final int NUMBER_OF_PREYS = 10;
    @ArgumentsAnnotation(name="numberofpreys", defaultValue="10", help="number of preys in the environment")
    private int numberOfPreys;
    
    private static final double FORAGE_OUT_RADIUS = 2.0;
    @ArgumentsAnnotation(name="forageOutRadius", defaultValue="2.0", help="outer radius for prey positions")
    private final double forageOutRadius;
    
    private static final double FORAGE_IN_RADIUS = 0.80;
    @ArgumentsAnnotation(name="forageInRadius", defaultValue="0.8", help="inner radius for prey positions")
    private final double forageInRadius;
    /** Prey end **/
    
    
    
    
    
    private static final Double CLOSEST_RADIUS = 0.07;
    @ArgumentsAnnotation(name="closestRadius", help="radius around each prey that robots must occupy simultaneously to capture the prey", defaultValue="0.07")
    private Double closestRadius;
    
    private static final int TEAM_SIZE = 2;
    @ArgumentsAnnotation(name="teamSize", help="number of robots required to capture a prey", defaultValue="2")
    private int teamSize;
    
    
    
    
    private static final int WALLED = 0;
    @ArgumentsAnnotation(name="wall", defaultValue="1")     //does the environment
    private boolean walled;                                 //have a wall?
    
    
    private static final Double NEST_RADIUS = 0.25;
    @ArgumentsAnnotation(name="nestRadius", defaultValue="0.25", help="nest radius")
    private double nestRadius;

    private static final int RANDOMIZE_NEST_POSITION = 0;
    @ArgumentsAnnotation(name="randomizeNestPosition", defaultValue="0", help="if set to 1, the nest position is randomized")
    private final boolean randomizeNestPosition;
    
    
    
    
    
    
    
    
    
    
    
    private static final int RANDOMIZE_ROBOTS_POSITION = 0;
    @ArgumentsAnnotation(name="randomizeRobotsPosition", defaultValue="0", help="if set to 1, all robots positions are randomized")
    private final boolean randomizeRobotsPosition;
    
    
    
    
    
    
    
    @ArgumentsAnnotation(name="forbiddenarea", defaultValue="5.0")
    private double forbiddenArea;

    
    
    

    
    private Nest nest;                              //the nest
    
    
    private Double lastPreyCaptureTime;             //moment when the last
                                                    //prey was captured
    
    private int numberOfFoodSuccessfullyForaged;      
    private Random random;

    private Vector2d preysGatherLocation;       //location to gather preys 
                                                //if preys are to be gathered
    
    private Wall wall;                          //environment wall, if exists
    
    
    private Simulator simulator;
    private Arguments args;

    
    public CooperativeNestForagingEnvironment(Simulator simulator, Arguments arguments) {
        
            super(simulator, arguments);
            
            this.simulator      = simulator;
            this.args           = arguments;
            this.random         = simulator.getRandom();
            
            
            nestRadius          = arguments.getArgumentAsDoubleOrSetDefault("nestRadius", NEST_RADIUS);
            forageOutRadius     = arguments.getArgumentAsDoubleOrSetDefault("forageOutRadius", FORAGE_OUT_RADIUS);
            forageInRadius      = arguments.getArgumentAsDoubleOrSetDefault("forageInRadius", FORAGE_IN_RADIUS);
            forbiddenArea       = arguments.getArgumentIsDefined("forbiddenarea") ? arguments.getArgumentAsDouble("forbiddenarea")       : 5.0;
            randomizeNestPosition = arguments.getArgumentAsIntOrSetDefault("randomizeNestPosition", RANDOMIZE_NEST_POSITION) == 1;
            
            
            randomizeRobotsPosition = arguments.getArgumentAsIntOrSetDefault("randomizeRobotsPosition", RANDOMIZE_ROBOTS_POSITION) == 1;
            
            preysGathered = arguments.getArgumentAsIntOrSetDefault("preysGathered", PREYS_GATHERED) == 1; 
            if ( preysGathered ) {
                double radius = random.nextDouble() * (forageOutRadius-forageInRadius) + forageInRadius;
                double angle = (random.nextDouble() * 2 - 1) * Math.PI;
                preysGatherLocation = new Vector2d ( angle );
                preysGatherLocation.setLength( radius );
            }
            
            closestRadius       = arguments.getArgumentAsDoubleOrSetDefault("closestRadius", CLOSEST_RADIUS);
            teamSize            = arguments.getArgumentAsIntOrSetDefault("teamSize", TEAM_SIZE);
            
            walled              = arguments.getArgumentAsIntOrSetDefault("wall", WALLED) == 1;
            
            
            preyMass            = arguments.getArgumentAsDoubleOrSetDefault("preyMass", PREY_MASS);
            
            varyPreyMass        = arguments.getArgumentAsIntOrSetDefault("varyPreyMass", VARY_PREY_MASS) == 1;
            
            preyRadius          = arguments.getArgumentAsDoubleOrSetDefault("preyRadius", PREY_RADIUS);
            
            lastPreyCaptureTime = 0.0;
            numberOfFoodSuccessfullyForaged = 0; 
    }
	
    
    
    
    @Override
    public void setup(Simulator simulator) {
            
        super.setup(simulator);


        
        if ( !randomizeNestPosition ) {                         
                                                                //place nest at center
            nest = new Nest(simulator, "Nest", 0, 0, nestRadius);
        }else{
                                                                //randomize nest position
            double x = random.nextDouble() * (forageOutRadius*2) - forageOutRadius;
            double y = random.nextDouble() * (forageOutRadius*2) - forageOutRadius;
            nest = new Nest(simulator, "Nest", x, y, nestRadius);
        }
        
        addObject(nest);
        
        
        
        
        
        
        
        
//        //create preprogrammed robots
//        Arguments argsPreprog = simulator.getArguments().get("--preprogrammed");
//        if ( argsPreprog != null ) {
//            int numberOfWalkers = argsPreprog.getArgumentAsIntOrSetDefault("numberofrobots",1);
//            for(int i = 0; i < numberOfWalkers; i++){
//                    Robot walker = Robot.getRobot(simulator, argsPreprog);
//                    walker.setController(new RandomWalkerController(simulator, walker, argsPreprog));
//                    walker.setBodyColor( Color.yellow );
//                    walker.setPosition( nest.getPosition().getX() + simulator.getRandom().nextDouble() * 0.15 - 0.075, nest.getPosition().getY() + simulator.getRandom().nextDouble() * 0.15 - 0.075 );
//                    
//                    walker.setOrientation( simulator.getRandom().nextDouble() * 2 * Math.PI );
//                    addRobot(walker);
//            }
//        }
        
        
        if ( randomizeRobotsPosition ) {                        //randomize robots positions
            for ( Robot robot : simulator.getRobots() ) {       //within forage area
                robot.setPosition( simulator.getRandom().nextDouble() * 2 * forageOutRadius - forageOutRadius, 
                                   simulator.getRandom().nextDouble() * 2 * forageOutRadius - forageOutRadius);
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


        Double mass;
        for(int i = 0; i < numberOfPreys; i++ ){
            if ( varyPreyMass ) {
                mass = preyMass + simulator.getRandom().nextDouble() * 2;
                addPrey(new Prey(simulator, "Prey "+i, newRandomPreyPosition(), 0, mass.intValue(), preyRadius*(mass.intValue()) ));
            }else{
                addPrey(new Prey(simulator, "Prey "+i, newRandomPreyPosition(), 0, preyMass, preyRadius*(preyMass.intValue()) ));
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
     * Generates a new random position 
     * for a prey
     * @return a Vector2d representing
     * the generated position
     */
    private Vector2d newRandomPreyPosition() {
        
        double radius, angle;
        
        if ( preysGathered ) {                                  //gather preys at a spot
            double xDelta = (random.nextDouble() - 0.5) * 0.25;
            double yDelta = (random.nextDouble() - 0.5) * 0.25;
            return new Vector2d( preysGatherLocation.x + xDelta, preysGatherLocation.y + yDelta );
        }
        else{                                                   //randomly place preys anywhere
            radius = random.nextDouble() * (forageOutRadius-forageInRadius) + forageInRadius;
            angle = random.nextDouble()*2*Math.PI;
            return new Vector2d( radius * Math.cos(angle), radius * Math.sin(angle) );
        }
        
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
            
            if( currentPrey.getHolder() == null &&
                    currentPrey.getPosition().distanceTo( nest.getPosition() ) <= nest.getRadius() ) {            
                                                                //prey is at nest
                                                                //move prey to
                //currentPrey.teleportTo( newRandomPreyPosition() );//new position

                currentPrey.teleportTo( new Vector2d(100, 100) );
                numberOfFoodSuccessfullyForaged++;              //account for
                                                                //foraged prey
                lastPreyCaptureTime = time;
                
                
//                if ( numberOfFoodSuccessfullyForaged == numberOfPreys ) {
//                    simulator.stopSimulation();         //no more preys:
//                }                                       //stop simulation, save time
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
            return nestRadius;
    }

    public double getForageRadius() {
            return forageOutRadius;
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
