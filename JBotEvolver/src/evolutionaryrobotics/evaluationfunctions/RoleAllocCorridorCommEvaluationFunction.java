package evolutionaryrobotics.evaluationfunctions;

import java.util.ArrayList;
import mathutils.Vector2d;
import simulation.Simulator;
import simulation.environment.RoleAllocCorridorEnvironment;
import simulation.robot.Robot;
import simulation.robot.actuators.RoleActuator;
import simulation.util.Arguments;

public class RoleAllocCorridorCommEvaluationFunction extends EvaluationFunction{
	
    
    protected Vector2d   nestPosition = new Vector2d(0, 0);
        
    
    private int numOfRobots;                //number of robots in the simulation
    
    private int totalSteps;                 //number of time steps
                                            //for the duration of the simulation
    

    
    
    private Double maxOutput;               //maximum output amongst all robots
    private Double[] outputs;               //outputs of all robots
    
    
    
    
    
    //* First behavioral fitness component
    private Double maxDist = 0.9;           //max distance from leader to nest
                                            //this is an arbitrary value
    
    
    
    
    
    
    private Double currentFitness;          //fitness value up to a moment
    
    
    public RoleAllocCorridorCommEvaluationFunction(Arguments args) {
            super(args);	
            
            currentFitness = 0.0;
            
            
    }

    
    
    @Override
    public double getFitness() {
        
        return currentFitness;        
        
    }

    
    
    @Override
    public void update(Simulator simulator) {

        if ( outputs == null ) {
            
            outputs = new Double[ simulator.getRobots().size() ];
            numOfRobots = simulator.getRobots().size();
            totalSteps = simulator.getEnvironment().getSteps();
            
        }
        
        
        
        
        RoleAllocCorridorEnvironment env;                   //get the environment
        env = (RoleAllocCorridorEnvironment) simulator.getEnvironment();
        
        //* First behavioral fitness component: leader close to nest
        Robot leader;
        leader = findClosestRobotToNest( simulator, env.getNest().getPosition() );
        
        
        double distanceLeaderToNest;
        distanceLeaderToNest = leader.getDistanceBetween( env.getNest().getPosition() );

        double bfc1 = (Math.max(0.0, maxDist-distanceLeaderToNest) / maxDist);
        
        
        //* Second behavioral fitness component: keep all others away from nest
        double  distanceAllOthers = 0.0;
        for (Robot robot : simulator.getRobots()) {
            if ( !robot.equals( leader ) ) {
                distanceAllOthers += Math.min( 1, robot.getDistanceBetween( nestPosition ) ) / maxDist;
            }
        }
        double bfc2 = distanceAllOthers / (simulator.getRobots().size() - 1);
        
        
        //* communication fitness component
        maxOutput = 0.0;                    //reinitialize maximum output
        
        RoleActuator act;                   //actuator that stores the output
        
        int robotIndex = 0;
        for ( Robot r : simulator.getRobots() ) {
            act = (RoleActuator) r.getActuatorByType( RoleActuator.class );
                                                    //get output value
            outputs[robotIndex] = act.getValue();   //of robot with index i

            if ( act.getValue() > maxOutput ) {     //store the max output
               maxOutput = act.getValue();          //in a variable
            }
            robotIndex++;
        }
        
        
        Double diffsSum = 0.0;                  //sum of all differences between
                                                //maxoutput and all outputs
        
        
        for (Double output : outputs) {         //determine sum of
            diffsSum += (maxOutput - output);   //all differences
        }
        
        
        double cfc = ( diffsSum / (totalSteps * (numOfRobots - 1) ) );
        
        
//        System.out.println("");
//        System.out.println("bfc1=" + (bfc1/totalSteps) );
//        System.out.println("bfc2=" + (bfc2/totalSteps) );
//        System.out.println("cfc=" + cfc );
        
        
        
        currentFitness += 0.60 * (bfc1/totalSteps) + 0.20 * (bfc2/totalSteps) + 0.2 * cfc;
        
        
        
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

    
    
}