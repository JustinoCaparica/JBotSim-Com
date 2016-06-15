package simulation.environment;

import java.util.List;
import java.util.Random;
import mathutils.Vector2d;
import simulation.Simulator;
import simulation.physicalobjects.ClosePhysicalObjects.CloseObjectIterator;
import simulation.physicalobjects.Nest;
import simulation.physicalobjects.PhysicalObjectDistance;
import simulation.physicalobjects.Prey;
import simulation.robot.Robot;
import simulation.robot.actuators.PreyPickerActuator;
import simulation.robot.sensors.PreyCarriedSensor;
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
    
    
    @ArgumentsAnnotation(name="closestRadius", help="radius around each prey that cooperating robots must occupy simultaneously to capture the prey", defaultValue="0.2")
    private Double closestRadius;
    
    
    @ArgumentsAnnotation(name="teamElementsCount", help="number of robots required to capture a prey", defaultValue="2")
    private int teamElementsCount;
    
    

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
            
            closestRadius       = 0.2; //TODO get this variable from the arguments
            teamElementsCount   = 2;   //TODO get this variable from the arguments
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
                    numberOfPreys = (int)(densityoffood*Math.PI*forageLimit*forageLimit+.5);
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
        
        for ( Prey currentPrey : simulator.getEnvironment().getPrey() ) {

            closeRobots = simulator.getEnvironment().getClosestRobots( currentPrey.getPosition(), closestRadius );

            if( closeRobots.size() >= teamElementsCount ) {     //prey captured
                                                                   
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
}
