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
 * more than one robot. Robots are placed in the nodes of
 * a matrix with a given width and height. The number of
 * robots and preys may be defined
 * @author gus
 */
public class CooperativeMatrixForagingEnvironment extends Environment {

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


    private final int NUMBER_OF_PREYS = 10;
    @ArgumentsAnnotation(name="numberofpreys", defaultValue="10")
    private int numberOfPreys;

    
    
    private final int MATRIX_COLUMNS = 4;
    @ArgumentsAnnotation(name="matrixColumns", defaultValue="4", help="the number of matrix columns to each side of the (0,0) location")
    private int matrixColumns;
    
    
    private final int MATRIX_LINES = 4;
    @ArgumentsAnnotation(name="matrixLines", defaultValue="4", help="the number of matrix lines to each side of the (0,0) location")
    private int matrixLines;
    
    
    private final double MATRIX_X_INTERVAL = 0.5;
    @ArgumentsAnnotation(name="matrixXinterval", defaultValue="0.5", help="interval between columns")
    private double matrixXinterval;
    
    
    private final double MATRIX_Y_INTERVAL = 0.5;
    @ArgumentsAnnotation(name="matrixYinterval", defaultValue="0.5", help="interval between lines")
    private double matrixYinterval;
    
    
    private final double MATRIX_ROBOTS_MAX_SPEED = 0.6;
    @ArgumentsAnnotation(name="matrixRobotsMaxSpeed", defaultValue="0.6", help="max speed for the robots that are placed on the matrix; 0.5 is stoped")
    private double matrixRobotsMaxSpeed;
    
    
//    
//    private final int ROBOTS_IN_MATRIX = 1;
//    @ArgumentsAnnotation(name="robotsInMatrix", defaultValue="1", help="number of robots in the matrix. Number of robots in the center equals the total number of robots minus the robots in the matrix")
//    private int robotsInMatrix;
//    
    
    
    
    
    
    
    private Nest nest;
    
    
    private Double lastPreyCaptureTime;             //moment when the last
                                                    //prey was captured
    
    private int numberOfFoodSuccessfullyForaged = 0;//number of preys captured        
    
    private Random random;                          //random generator, get it
                                                    //from the simulator

    
    
    private Wall wall;                              //environment wall, if exists
    
    
    private Simulator simulator;
    private Arguments args;

    
    public CooperativeMatrixForagingEnvironment(Simulator simulator, Arguments arguments) {
        
            super(simulator, arguments);
            
            this.simulator      = simulator;
            this.args           = arguments;
            
            nestLimit           = arguments.getArgumentIsDefined("nestlimit") ? arguments.getArgumentAsDouble("nestlimit")       : .5;
            
            closestRadius       = arguments.getArgumentAsDoubleOrSetDefault("closestRadius", CLOSEST_RADIUS);
            teamSize            = arguments.getArgumentAsIntOrSetDefault("teamSize", TEAM_SIZE);
            
            
            matrixColumns       = arguments.getArgumentAsIntOrSetDefault("matrixColumns", MATRIX_COLUMNS);
            matrixLines         = arguments.getArgumentAsIntOrSetDefault("matrixLines", MATRIX_LINES);
            matrixXinterval     = arguments.getArgumentAsDoubleOrSetDefault("matrixXinterval", MATRIX_X_INTERVAL);
            matrixYinterval     = arguments.getArgumentAsDoubleOrSetDefault("matrixYinterval", MATRIX_Y_INTERVAL);
            
            //robotsInMatrix      = arguments.getArgumentAsIntOrSetDefault("robotsInMatrix", ROBOTS_IN_MATRIX);
            numberOfPreys       = arguments.getArgumentAsIntOrSetDefault("numberOfPreys", NUMBER_OF_PREYS);
            
            matrixRobotsMaxSpeed = arguments.getArgumentAsDoubleOrSetDefault("matrixRobotsMaxSpeed", MATRIX_ROBOTS_MAX_SPEED) ;
            
            
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
        
        
        int matrixRobotsCount = matrixColumns * 2 * matrixLines * 2;
        int centerRobotsCount = numberOfRobots - matrixRobotsCount;
        
        
                                                                    //randomize central
        for (int i = 0; i <  centerRobotsCount; i++) {              //robots orientations
            simulator.getRobots().get(i).setOrientation( random.nextDouble()*2*Math.PI );
        }
        
        
        
        
        Set<Integer> robotsWithPreyIndices = new HashSet<>();
        int robotWithPreyIndex;
        while ( robotsWithPreyIndices.size() < numberOfPreys ) {                            //randomize 
            robotWithPreyIndex = random.nextInt( matrixRobotsCount ) + centerRobotsCount;   //prey
            if ( !robotsWithPreyIndices.contains(robotWithPreyIndex) ) {                    //positions
                robotsWithPreyIndices.add( robotWithPreyIndex );
            }
        }
        
        
        
        
        
        int robotIndex = centerRobotsCount;
        double xCoord, yCoord;
        
        for (int x = 1; x < matrixColumns + 1; x++) {
            xCoord = ( x * matrixXinterval );                   //determine robot position xCoord
            
            for (int y = 1; y < matrixLines + 1; y++) {
                yCoord = ( y * matrixYinterval );               //determine robot position yCoord
                
                addRobotToMatrix( simulator, robotsWithPreyIndices, robotIndex++,  xCoord,  yCoord );
                addRobotToMatrix( simulator, robotsWithPreyIndices, robotIndex++, -xCoord,  yCoord );
                addRobotToMatrix( simulator, robotsWithPreyIndices, robotIndex++,  xCoord, -yCoord );
                addRobotToMatrix( simulator, robotsWithPreyIndices, robotIndex++, -xCoord, -yCoord );
                
            }
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

    
    /**
     * Gets the initial number of
     * preys
     * @return the initial number 
     * of preys 
     */
    public int getNumberOfPreys(){
        return numberOfPreys;
    }

    
    
    /**
     * Adds a robot to a point in the matrix
     * @param simulator the simulator
     * @param robotsWithPreyIndices indices of
     * the robots who should have a prey in sight
     * @param robotIndex the index of the robot
     * to be added
     * @param xCoord the x coordinate of the point
     * where the robot is to be added
     * @param yCoord the y coordinate of the point
     * where the robot is to be added
     */
    private void addRobotToMatrix(  Simulator simulator, Set<Integer> robotsWithPreyIndices,
                                    int robotIndex, 
                                    double xCoord, double yCoord) {
        
        TwoWheelActuator wheelAct;
        RecruitmentImmediateActuator recruitmentAct;
        
        
        simulator.getRobots().get( robotIndex ).setPosition( xCoord, yCoord );
        



        wheelAct = (TwoWheelActuator) simulator.getRobots().get( robotIndex ).getActuatorByType( TwoWheelActuator.class );
        if ( wheelAct != null ) {                               //robots in matrix 
            wheelAct.setMaxSpeed( matrixRobotsMaxSpeed );       //max speed
        }

        
        
        if ( robotsWithPreyIndices.contains(robotIndex) ) {                         
            addPrey( new Prey( simulator, "Prey "+ this.getPrey().size(),
                                new Vector2d( xCoord + 0.05, yCoord + 0.0), 
                                0, preyMass, preyRadius));
            simulator.getRobots().get( robotIndex ).setOrientation( 0 );
        }
        else{
            simulator.getRobots().get( robotIndex ).setOrientation( simulator.getRandom().nextDouble() * 2 * Math.PI );
        }
        
        recruitmentAct = (RecruitmentImmediateActuator) simulator.getRobots().get( robotIndex ).getActuatorByType( RecruitmentImmediateActuator.class );
        if ( recruitmentAct != null ) {                         
            recruitmentAct.setEnableBeingRecruited( false );    //robot can not use recruitment actuator to be recruited
        }
        
    }
    
    
    
}
