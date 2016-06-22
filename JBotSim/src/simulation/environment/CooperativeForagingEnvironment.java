package simulation.environment;

import java.util.List;
import java.util.Random;
import mathutils.Vector2d;
import simulation.Simulator;
import simulation.physicalobjects.Nest;
import simulation.physicalobjects.Prey;
import simulation.robot.Robot;
import simulation.util.Arguments;
import simulation.util.ArgumentsAnnotation;


/**
 * An Environment where preys must be captured by 
 * more than one robot. Thus, robots must cooperate
 * to capture preys.
 * @author gus
 */
public class CooperativeForagingEnvironment extends Environment {

    private static final double PREY_RADIUS = 0.025;
    private static final double PREY_MASS = 1;
    
    private static final Double CLOSEST_RADIUS = 0.07;
    @ArgumentsAnnotation(name="closestRadius", help="radius around each prey that robots must occupy simultaneously to capture the prey", defaultValue="0.07")
    private Double closestRadius;
    
    private static final int TEAM_SIZE = 2;
    @ArgumentsAnnotation(name="teamSize", help="number of robots required to capture a prey", defaultValue="2")
    private int teamSize;
    
    
    
    
    

    @ArgumentsAnnotation(name="nestlimit", defaultValue="0.5")
    private double nestLimit;

    @ArgumentsAnnotation(name="foragelimit", defaultValue="2.0")
    private double forageLimit;

    @ArgumentsAnnotation(name="forbiddenarea", defaultValue="5.0")
    private double forbiddenArea;

    @ArgumentsAnnotation(name="numberofpreys", defaultValue="10")
    private int numberOfPreys;

    @ArgumentsAnnotation(name="densityofpreys", defaultValue="")
    private Nest nest;
    
    @ArgumentsAnnotation(name="densityofpreysValues", help="list of possible values for the preys density. One value is randomly chosen from the list when the environment is created", defaultValue="")
    
    
    private int numberOfFoodSuccessfullyForaged = 0;        
    private Random random;

    
    
    
    private Simulator simulator;
    private Arguments args;

    
    public CooperativeForagingEnvironment(Simulator simulator, Arguments arguments) {
        
            super(simulator, arguments);
            
            this.simulator      = simulator;
            this.args           = arguments;
            
            nestLimit           = arguments.getArgumentIsDefined("nestlimit") ? arguments.getArgumentAsDouble("nestlimit")       : .5;
            forageLimit         = arguments.getArgumentIsDefined("foragelimit") ? arguments.getArgumentAsDouble("foragelimit")       : 2.0;
            forbiddenArea       = arguments.getArgumentIsDefined("forbiddenarea") ? arguments.getArgumentAsDouble("forbiddenarea")       : 5.0;
            
            closestRadius       = arguments.getArgumentAsDoubleOrSetDefault("closestRadius", CLOSEST_RADIUS);
            teamSize            = arguments.getArgumentAsIntOrSetDefault("teamSize", TEAM_SIZE);
            
            
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
                    numberOfPreys = args.getArgumentIsDefined("numberofpreys") ? args.getArgumentAsInt("numberofpreys") : 20;
            }

            
            
            for(int i = 0; i < numberOfPreys; i++ ){
                    addPrey(new Prey(simulator, "Prey "+i, newRandomPosition(), 0, PREY_MASS, PREY_RADIUS));
            }
            nest = new Nest(simulator, "Nest", 0, 0, nestLimit);
            addObject(nest);
    }

    
    private Vector2d newRandomPosition() {
            double radius = random.nextDouble()*(forageLimit-nestLimit)+nestLimit*1.1;
            double angle = random.nextDouble()*2*Math.PI;
            return new Vector2d(radius*Math.cos(angle),radius*Math.sin(angle));
    }
	
    
    
    @Override
    public void update(double time) {
//		nest.shape.getClosePrey().update(time, teleported);
//		CloseObjectIterator i = nest.shape.getClosePrey().iterator();

        List<Robot> closeRobots;    //robots within radius of prey
       
        for(Prey currentPrey : simulator.getEnvironment().getPrey() ) {

            closeRobots = simulator.getEnvironment().getClosestRobots( currentPrey.getPosition(), closestRadius );
              
//            
//            boolean focusedBy = false, focusingOn = false;      //this piece of code is for
//            for (Robot robot : closeRobots) {                   // debug purposes ONLY! remove it when debug is done
//                FocusedBySensor s1;
//                s1 = (FocusedBySensor)robot.getSensorByType( FocusedBySensor.class );
//                if ( s1.isFocused() ) {
//                    focusedBy = true;
//                }
//                FocusingOnSensor s2;
//                s2 = (FocusingOnSensor)robot.getSensorByType( FocusingOnSensor.class );
//                if ( s2.isFocused() ) {
//                    focusingOn = true;
//                }
//            }
//            if ( !focusedBy || !focusingOn ) {
//                continue;
//            }                                                   //end of debug code
            
            
            if( closeRobots.size() >= teamSize ) {              //prey captured                                         
                                                                //move prey to
                currentPrey.teleportTo( newRandomPosition() );  //new position

                numberOfFoodSuccessfullyForaged++;              //account for
                                                                //foraged prey
            }
        }



        
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
