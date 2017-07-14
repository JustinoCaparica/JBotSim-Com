package evolutionaryrobotics.evaluationfunctions;

import java.util.ArrayList;
import mathutils.Vector2d;
import simulation.Simulator;
import simulation.environment.RoleAllocCorridorEnvironment;
import simulation.physicalobjects.Nest;
import simulation.robot.Robot;
import simulation.robot.actuators.RoleActuator;
import simulation.robot.sensors.NestSensor;
import simulation.util.Arguments;
import simulation.util.ArgumentsAnnotation;

/**
 * An evaluation function that measures how well 
 * the swarm is doing the task. It counts the number
 * of time steps a sole robot is at less than N cm
 * from the nest and no other robot is at less than 45cm
 * from the nest.
 * @author gus
 */
public class RoleAllocCorridorBehavioralPostEvaluationFunction extends EvaluationFunction{
	
    
    protected Vector2d   nestPosition = new Vector2d(0, 0);
        
    
    
    private int totalSteps;                 //number of time steps
                                            //for the duration of the simulation
    

    private Double maxDistLeaderToNest;     //maximum distance the leader
                                            //might be from the nest
                                            // for a time step to be counted
                                            // as successful
    
    private Double DIST_TO_NEST_LIMIT = 0.45;
    @ArgumentsAnnotation(name = "distToNestLimit", defaultValue = "0.45", help = "if more than one robot is closer to nest than this distance, fitness is not increased")
    private Double distToNestLimit;         //maximum distance a second robot
                                            //may be to the nest for fitness
                                            //to be considered
    
    
    
    private Double currentFitness;          //fitness value up to a moment
    
    
    
    public RoleAllocCorridorBehavioralPostEvaluationFunction(Arguments args) {
            super(args);	
            
            currentFitness  = 0.0;
            
            maxDistLeaderToNest = 0.075;        
            
            distToNestLimit = args.getArgumentAsDoubleOrSetDefault("distToNestLimit", DIST_TO_NEST_LIMIT);
            
            //fitness info
            setFitnessInfoValue("bfc1", 0.0);
            setFitnessInfoValue("bfc2", 0.0);
            setFitnessInfoValue("cfc", 0.0);
            setFitnessInfoValue("bfc", 0.0);
            setFitnessInfoValue("fit", 0.0);
            
    }

    
    
    @Override
    public double getFitness() {
        
        return currentFitness;        
        
    }

    
    
    @Override
    public void update(Simulator simulator) {

        
        totalSteps = simulator.getEnvironment().getSteps();

        
        RoleAllocCorridorEnvironment env;                   //get the environment
        env = (RoleAllocCorridorEnvironment) simulator.getEnvironment();
        Vector2d nestPos = env.getNest().getPosition();     //get nest position
                
        
        //* First behavioral fitness component: leader close to nest
        Robot leader;
        leader = findClosestRobotToNest( simulator, nestPos );
        
        
        double distanceLeaderToNest;
        distanceLeaderToNest = leader.getDistanceBetween( nestPos );

        double bfc1 = 0.0;
        
        if ( distanceLeaderToNest <= maxDistLeaderToNest ) {
            bfc1 = 1.0;
        }
                 
        //if there are NOT more than 1 robot closer than a distance to nest
        // count this time step
        if ( !moreThanOneRobotCloserToNest( simulator.getRobots(), nestPos, distToNestLimit ) ) {
            currentFitness += (bfc1/totalSteps);
        }
       
        setFitnessInfoValue("bfc1", getFitnessInfoValue("bfc1") + bfc1/totalSteps);
        setFitnessInfoValue("bfc2", getFitnessInfoValue("bfc2") + (-1.0) );
        setFitnessInfoValue("cfc", getFitnessInfoValue("cfc")   + (-1.0) );
        setFitnessInfoValue("bfc", getFitnessInfoValue("bfc") + (-1.0) );
        setFitnessInfoValue("fit", currentFitness);
        
    }

    
    /**
     * Finds the closest robot
     * to nest
     * @param simulator the simulator
     * @param nestPosition the nest
     * position
     * @return the robot that is
     * closest to nest
     */
    private Robot findClosestRobotToNest( Simulator simulator, Vector2d nestPosition ) {
        
        Robot closest   = simulator.getRobots().get(0);     //first robot
        
        
        
        Double dist;
        dist = closest.getDistanceBetween( nestPosition );   
        
        
        for ( Robot robot : simulator.getRobots() ) {
                                                            //a robot closest
                                                            //to nest is found!
            if ( robot.getDistanceBetween( nestPosition ) < dist ) {
                closest = robot;
                dist = robot.getDistanceBetween( nestPosition );
            }
        }
    
        return closest;
        
    }

    /**
     * Determines if more than one robot 
     * is closer to nest than a given
     * distance
     * @param distToNestLimit the distance
     * @param robots the robots in the
     * environment
     * @param nestPos nest position
     * @return TRUE if more than one
     * robot is closer to nest than
     * a given distance
     */
    private boolean moreThanOneRobotCloserToNest(   ArrayList<Robot> robots,
                                                    Vector2d nestPos,
                                                    Double distToNestLimit ) {
        
        int robotsCount = 0;
                                            //count robots closer to
        for (Robot robot : robots) {        //nest than the given distance
            if ( robot.getDistanceBetween(nestPos) < distToNestLimit ) {
                robotsCount++;
            }
        }
        
        return robotsCount > 1;     
    }

    
    
}