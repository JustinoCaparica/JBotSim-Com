package simulation.environment;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import mathutils.Vector2d;
import simulation.Simulator;
import simulation.physicalobjects.Nest;
import simulation.physicalobjects.Prey;
import simulation.physicalobjects.Wall;
import simulation.robot.Robot;
import simulation.robot.actuators.RecruitmentImmediateActuator;
import simulation.robot.actuators.TwoWheelActuator;
import simulation.robot.sensors.PreySensor;
import simulation.util.Arguments;
import simulation.util.ArgumentsAnnotation;


/**
 * An Environment where preys must be captured by 
 * more than one robot. Robots are placed in a circle 
 * around the nest with a given radius
 * @author gus
 */
public class CooperativeCircleForagingEnvironment extends Environment {

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

    @ArgumentsAnnotation(name="forbiddenarea", defaultValue="5.0")
    private double forbiddenArea;

    private final int NUMBER_OF_PREYS = 10;
    @ArgumentsAnnotation(name="numberofpreys", defaultValue="10")
    private int numberOfPreys;

    private final double ROBOTS_CIRCLE_RADIUS = 0.5;
    @ArgumentsAnnotation(name="robotsCircleRadius", defaultValue="0.5", help="radius of the circle where the robots are placed")
    private double robotsCircleRadius;
    
    
    private final int ROBOTS_IN_CIRCLE = 1;
    @ArgumentsAnnotation(name="robotsInCircle", defaultValue="1", help="number of robots in the robots circle. robots are evenly spaced. Number of robots in the center equals the total number of robots minus the robots in the circle")
    private int robotsInCircle;
    
    private final int ROBOTS_IN_CIRCLE_CAN_MOVE = 0;
    @ArgumentsAnnotation(name="robotsInCircleCanMove", defaultValue="0", help="can robots in circle move?")
    private final Boolean robotsInCircleCanMove;
    
    
    
    
    @ArgumentsAnnotation(name="densityofpreys", defaultValue="")
    private Nest nest;
    
    @ArgumentsAnnotation(name="densityofpreysValues", help="list of possible values for the preys density. One value is randomly chosen from the list when the environment is created", defaultValue="")
    
    
    private Double lastPreyCaptureTime;             //moment when the last
                                                    //prey was captured
    
    private int numberOfFoodSuccessfullyForaged = 0;        
    private Random random;

    
    
    private Wall wall;                              //environment wall, if exists
    
    
    private Simulator simulator;
    private Arguments args;

    
    public CooperativeCircleForagingEnvironment(Simulator simulator, Arguments arguments) {
        
            super(simulator, arguments);
            
            this.simulator      = simulator;
            this.args           = arguments;
            
            nestLimit           = arguments.getArgumentIsDefined("nestlimit") ? arguments.getArgumentAsDouble("nestlimit")       : .5;
            forageLimit         = arguments.getArgumentIsDefined("foragelimit") ? arguments.getArgumentAsDouble("foragelimit")       : 2.0;
            forbiddenArea       = arguments.getArgumentIsDefined("forbiddenarea") ? arguments.getArgumentAsDouble("forbiddenarea")       : 5.0;
            
            closestRadius       = arguments.getArgumentAsDoubleOrSetDefault("closestRadius", CLOSEST_RADIUS);
            teamSize            = arguments.getArgumentAsIntOrSetDefault("teamSize", TEAM_SIZE);
            robotsCircleRadius  = arguments.getArgumentAsDoubleOrSetDefault("robotsCircleRadius", ROBOTS_CIRCLE_RADIUS);
            robotsInCircle      = arguments.getArgumentAsIntOrSetDefault("robotsInCircle", ROBOTS_IN_CIRCLE);
            numberOfPreys       = arguments.getArgumentAsIntOrSetDefault("numberOfPreys", NUMBER_OF_PREYS);
            
            robotsInCircleCanMove = arguments.getArgumentAsIntOrSetDefault("robotsInCircleCanMove", ROBOTS_IN_CIRCLE_CAN_MOVE) != 0;
            
            
            walled              = arguments.getArgumentAsIntOrSetDefault("wall", WALLED) == 1;
            
            
            preyMass            = arguments.getArgumentAsDoubleOrSetDefault("preyMass", PREY_MASS);
            preyRadius          = arguments.getArgumentAsDoubleOrSetDefault("preyRadius", PREY_RADIUS);
            
            lastPreyCaptureTime = 0.0;
    }
	
    
    
    
    @Override
    public void setup(Simulator simulator) {
            
        
        super.setup(simulator);

        this.random = simulator.getRandom();
        int numberOfRobots = simulator.getRobots().size();
        
        
                                                                        //randomize central
        for (int i = 0; i <  numberOfRobots - robotsInCircle; i++) {    //robots orientations
            simulator.getRobots().get(i).setOrientation( random.nextDouble()*2*Math.PI );
        }
        
        
        
        
        Set<Integer> preyPositions = new HashSet<>();
        int preyPosition;
        while ( preyPositions.size() < numberOfPreys ) {        //randomize 
            preyPosition = random.nextInt( robotsInCircle );    //prey
            if ( !preyPositions.contains(preyPosition) ) {      //positions
                preyPositions.add( preyPosition );
            }
        }
        
        
        
        double base = ( random.nextDouble()*2*Math.PI );
        
        TwoWheelActuator wheelAct;
        RecruitmentImmediateActuator recruitmentAct;
        Vector2d robotPos, preyPos;
        for (int i = 0; i < robotsInCircle; i++) {                       
            robotPos = new Vector2d( base + i * (2*Math.PI/robotsInCircle) );   //place
            robotPos.setLength( robotsCircleRadius );                           //robots in
                                                                                //the circle
            simulator.getRobots().get( i + numberOfRobots - robotsInCircle ).setPosition( robotPos );               
            simulator.getRobots().get( i + numberOfRobots - robotsInCircle ).setOrientation( base + i * (2*Math.PI/robotsInCircle) );
            
            if ( !robotsInCircleCanMove ) {
                wheelAct = (TwoWheelActuator) simulator.getRobots().get( i + numberOfRobots - robotsInCircle ).getActuatorByType( TwoWheelActuator.class );
                if ( wheelAct != null ) {               //robots in circle 
                    wheelAct.setMaxSpeed( 0 );          //can not move
                }
            }
            
            
            if ( preyPositions.contains(i) ) {          
                preyPos = new Vector2d( base + i * (2*Math.PI/robotsInCircle) ); 
                preyPos.setLength( robotsCircleRadius + 0.065);                 //place preys                
                //preyPos.setLength( robotsCircleRadius + 0.1);                 //place preys                
                addPrey(new Prey(simulator, "Prey "+i, preyPos, 0, preyMass, preyRadius));
            }else{
                recruitmentAct = (RecruitmentImmediateActuator) simulator.getRobots().get( i + numberOfRobots - robotsInCircle ).getActuatorByType( RecruitmentImmediateActuator.class );
                if ( recruitmentAct != null ) {             //there is no prey near robot,
                    recruitmentAct.setEnableBeingRecruited( false );     //robot can not use recruitment actuator
                }
            }
            
        }
        
        
        
        
        



        if(args.getArgumentIsDefined("densityofpreys")){
                double densityoffood = args.getArgumentAsDouble("densityofpreys");
                numberOfPreys = (int) ( densityoffood * Math.PI * forageLimit * forageLimit + .5 );
        } 
        else if ( args.getArgumentIsDefined("densityofpreysValues") ) {
                String[] rawArray = args.getArgumentAsString("densityofpreysValues").split(",");

                if(rawArray.length > 1){                        //randomize number of preys
                        double densityoffood = Double.parseDouble(rawArray[simulator.getRandom().nextInt(rawArray.length)]);
                        numberOfPreys = (int) ( densityoffood * Math.PI * forageLimit * forageLimit + .5 );
                }
        } else {
                numberOfPreys = args.getArgumentIsDefined("numberofpreys") ? args.getArgumentAsInt("numberofpreys") : NUMBER_OF_PREYS;
        }

        


        
//        for(int i = 0; i < numberOfPreys; i++ ){
//                addPrey(new Prey(simulator, "Prey "+i, newRandomPosition(), 0, preyMass, preyRadius));
//        }
        
        
        nest = new Nest(simulator, "Nest", 0, 0, nestLimit);
        addObject(nest);
        
        
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
        double radius = random.nextDouble()*(forageLimit-nestLimit)+nestLimit*1.1;
        //double radius = forageLimit;
        double angle = random.nextDouble()*2*Math.PI;
        return new Vector2d(radius*Math.cos(angle),radius*Math.sin(angle));
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
            
            
            //Make sure the robot sees the prey to capture it            
            PreySensor preySensor;
            closeRobots = simulator.getEnvironment().getClosestRobots( currentPrey.getPosition(), closestRadius );
            for ( Robot closeRobot : closeRobots ) {       //add enabled robots
                
                
                if ( closeRobot.isEnabled() ){
                    preySensor = (PreySensor)closeRobot.getSensorByType( PreySensor.class );
                    if ( preySensor != null ) {
                        if ( preySensor.detects(currentPrey) ) {
                            enabledRobots.add(closeRobot);
                        }
                    }else{
                        enabledRobots.add(closeRobot);
                    }
                    
                }
                
//                if ( closeRobot.isEnabled() ) {            //to the enabled
//                    enabledRobots.add( closeRobot );       //robots list
//                }
                
            }

            
            if( enabledRobots.size() >= teamSize ) {            //prey captured                                         
                                                                //move prey to
                //currentPrey.teleportTo( newRandomPosition() );//new position

                currentPrey.teleportTo( new Vector2d(100, 100) );
                numberOfFoodSuccessfullyForaged++;              //account for
                                                                //foraged prey
                
                lastPreyCaptureTime = time;
                
                
                if ( numberOfFoodSuccessfullyForaged == numberOfPreys ) {
                    simulator.stopSimulation();         //no more preys
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
