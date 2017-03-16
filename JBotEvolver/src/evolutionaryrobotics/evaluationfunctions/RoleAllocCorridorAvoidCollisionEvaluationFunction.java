package evolutionaryrobotics.evaluationfunctions;

import java.util.ArrayList;
import mathutils.Vector2d;
import simulation.Simulator;
import simulation.environment.RoleAllocCorridorEnvironment;
import simulation.physicalobjects.Nest;
import simulation.robot.Robot;
import simulation.robot.actuators.RoleActuator;
import simulation.util.Arguments;

public class RoleAllocCorridorAvoidCollisionEvaluationFunction extends EvaluationFunction{
	
    
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
    
    private Double bfc1Accum, bfc2Accum;    //acummulated
                                            //fitness components
    
    
    public RoleAllocCorridorAvoidCollisionEvaluationFunction(Arguments args) {
            super(args);	
            
            currentFitness  = 0.0;
            
            
            
            //fitness info
            setFitnessInfoValue("bfc1", 0.0);
            setFitnessInfoValue("bfc2", 0.0);
            setFitnessInfoValue("cfc", 0.0);
            setFitnessInfoValue("bfc", 0.0);
            setFitnessInfoValue("fit", 0.0);
            
    }

    
    
    @Override
    public double getFitness() {
        
        //double testFit = currentFitness * 2 - 1;
        
        
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
        Vector2d nestPos = env.getNest().getPosition();
                
        
        //* First behavioral fitness component: leader close to nest
        Robot leader;
        leader = findClosestRobotToNest( simulator, nestPos );
        
        
        double distanceLeaderToNest;
        distanceLeaderToNest = leader.getDistanceBetween( nestPos );

        double bfc1 = (Math.max(0.0, maxDist-distanceLeaderToNest) / maxDist);         
                 
        
        //* Second behavioral fitness component: keep all others away from nest
        double  distanceAllOthers = 0.0;
        for (Robot robot : simulator.getRobots()) {
            //System.out.println("robot" + robot.getId() + " x,y:" + robot.getPosition().getX() + ", " + robot.getPosition().getY());
            if ( !robot.equals( leader ) ) {
                //System.out.println("robot " + robot.getId() + " distance to nest:" + robot.getDistanceBetween( nestPos ) );
                distanceAllOthers += Math.min( maxDist, robot.getDistanceBetween( nestPos ) ) / maxDist;
            }
        }
        
        double bfc2 = distanceAllOthers / ((simulator.getRobots().size() - 1));
        
//        
//        
//        System.out.println("");
//        System.out.println("bfc1=" + (bfc1/totalSteps) );
//        System.out.println("bfc2=" + (bfc2/totalSteps) );
//        
        
        //TODO measure the shortest distance between any robot and any obstacle
        // in a normalized interval [0,1] according to 
        double safetyDist = 0.03;
        double minDist;
        minDist = simulator.getRobots().get(0).getDiameter() + safetyDist;  //safety distance
                                                                            //from the center of a robot
                                                                            //to the center of another robot
        
        double shortestDistance = minDist;                              //current smallest distance
                                                                        //between two robots
        for (Robot robot : simulator.getRobots()) {
            
            for( Robot closestRobot : simulator.getEnvironment().getClosestRobots(robot.getPosition(), minDist) ) {                
                if ( !robot.equals(closestRobot) && robot.getDistanceBetween( closestRobot.getPosition() ) < shortestDistance ) {
                    //System.out.println("#1 robot " + robot.getId() + " at " + robot.getDistanceBetween( closestRobot.getPosition()) + " from " + closestRobot.getId());
                    shortestDistance = robot.getDistanceBetween( closestRobot.getPosition() );
                }
            }
        }
        
        shortestDistance -= simulator.getRobots().get(0).getDiameter();
        if ( shortestDistance < 0 ) {
            shortestDistance = 0;
        }
        currentFitness += (0.75 * (bfc1/totalSteps) + 0.25 * (bfc2/totalSteps)) * (shortestDistance/safetyDist);
        
        
        getFitnessInfo().put("bfc1", getFitnessInfo().get("bfc1") + bfc1/totalSteps);
        getFitnessInfo().put("bfc2", getFitnessInfo().get("bfc2") + bfc2/totalSteps);
        getFitnessInfo().put("cfc", 0.0);
        getFitnessInfo().put("bfc", getFitnessInfo().get("bfc") + ((0.75*bfc1/totalSteps) + (0.25*bfc2/totalSteps)) );
        getFitnessInfo().put("fit", currentFitness);
        
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
     * Gets the robot with
     * the highest output in the
     * role actuator 
     * @param robots
     * @return 
     */
    private Robot findHighestOutputRobot( ArrayList<Robot> robots ) {
        
        
        RoleActuator roleAct;
        
        Double highest = 0.0;
        Robot r = null;
        
        for ( Robot robot : robots ) {                  //run throw all robots
            roleAct = (RoleActuator) robot.getActuatorByType( RoleActuator.class );
            if ( roleAct.getValue() > highest ) {       //and find the robot 
                highest = roleAct.getValue();           //with highest output
                r = robot;
            }
        }
        
        return r;
    }
    
    
}