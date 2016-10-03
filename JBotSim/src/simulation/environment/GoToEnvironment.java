package simulation.environment;

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
 * An environment where there is a robot
 * and a prey. The goal is for the robot
 * to capture the prey.
 * 
 * @author gus
 */
public class GoToEnvironment extends Environment {

    
    private static final double PREY_RADIUS = 0.025;
    @ArgumentsAnnotation(name="preyRadius", help="prey radius", defaultValue="0.025")
    private Double preyRadius;
    
    
    private static final double PREY_MASS = 1;
    @ArgumentsAnnotation(name="preyMass", help="prey mass", defaultValue="1")
    private Double preyMass;
    
    
    
    private static final Double PREY_CAPTURE_DISTANCE = 0.07;
    @ArgumentsAnnotation(name="preyCaptureDistance", help="the robot must be at this distance, at most, of the prey to capture it", defaultValue="0.07")
    private Double preyCaptureDistance;
    
    
    private static final Double FORAGE_LIMIT = 0.80;
    @ArgumentsAnnotation(name="foragelimit", help="maximum distance that the prey will be placed from the robot position , i.e., the environment the center", defaultValue="0.80")
    private double forageLimit;

    
    private Double preyCaptureTime;             //moment when the last
                                                //prey was captured
        
    private Random random;

    private Simulator simulator;
    private Arguments args;

    
    public GoToEnvironment(Simulator simulator, Arguments arguments) {
        
            super(simulator, arguments);
            
            this.simulator      = simulator;
            this.args           = arguments;
            this.random         = simulator.getRandom();
            
            
            forageLimit         = arguments.getArgumentIsDefined("foragelimit") ? arguments.getArgumentAsDouble("foragelimit")       : FORAGE_LIMIT;
           
            
            preyCaptureDistance = arguments.getArgumentAsDoubleOrSetDefault("closestRadius", PREY_CAPTURE_DISTANCE);
            
            
            preyMass            = arguments.getArgumentAsDoubleOrSetDefault("preyMass", PREY_MASS);
            preyRadius          = arguments.getArgumentAsDoubleOrSetDefault("preyRadius", PREY_RADIUS);
            
    }
	
    
    
    
    @Override
    public void setup(Simulator simulator) {
            
        super.setup(simulator);

        if(simulator.getRobots().size() == 1) {
                Robot r = simulator.getRobots().get(0);
                r.setPosition(0, 0);                        //place robot at center
                r.setOrientation( random.nextDouble() );    //facing random direction
        }

        
        addPrey(new Prey(simulator, "Prey "+0, newRandomPosition(), 0, preyMass, preyRadius));
        
            
    }

    
    private Vector2d newRandomPosition() {

        double radius = forageLimit;
        double angle = random.nextDouble()*2*Math.PI;
        return new Vector2d( radius * Math.cos(angle), 
                             radius * Math.sin(angle) );
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
            closeRobots = simulator.getEnvironment().getClosestRobots( currentPrey.getPosition(), preyCaptureDistance );
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
                
            }

            
            if( enabledRobots.size() >= 1 ) {                   //prey captured                                         
                                                                //move prey to
                //currentPrey.teleportTo( newRandomPosition() );//new position

                currentPrey.teleportTo( new Vector2d(100, 100) );
                
                preyCaptureTime = time;
                simulator.stopSimulation();
            }
        }



        
    }

    /**
     * Gets the time stamp of the moment
     * when the last prey was captured
     * @return the time stamp
     */
    public Double getPreyCaptureTime() {
        return preyCaptureTime;
    }
	
    
    
    
    
    public double getForageRadius() {
            return forageLimit;
    }

    
    
    
    
}
