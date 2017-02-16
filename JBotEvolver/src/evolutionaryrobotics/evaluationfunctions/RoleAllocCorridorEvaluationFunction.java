package evolutionaryrobotics.evaluationfunctions;

import java.util.ArrayList;
import mathutils.Vector2d;
import simulation.Simulator;
import simulation.environment.RoleAllocCorridorEnvironment;
import simulation.physicalobjects.Nest;
import simulation.robot.Robot;
import simulation.robot.actuators.RoleActuator;
import simulation.util.Arguments;

public class RoleAllocCorridorEvaluationFunction extends EvaluationFunction{
	
    
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
    
    
    public RoleAllocCorridorEvaluationFunction(Arguments args) {
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
        
//        Nest nest = null;
//        Vector2d nestPos = null;
//        
//        for (int i = 0; i < simulator.getEnvironment().getAllObjects().size() && nest == null; i++) {
//            if ( simulator.getEnvironment().getAllObjects().get(i) instanceof Nest ) {
//                nest = (Nest) simulator.getEnvironment().getAllObjects().get(i);
//                nestPos = nest.getPosition();
//            }
//        }
                
        
                 
                 
        RoleActuator act;                   //actuator that stores the output
        
        
        RoleAllocCorridorEnvironment env;                   //get the environment
        env = (RoleAllocCorridorEnvironment) simulator.getEnvironment();
        Vector2d nestPos = env.getNest().getPosition();
        
        
        //* First behavioral fitness component: leader close to nest
        Robot leader;
        leader = findClosestRobotToNest( simulator, nestPos );
        
        //leader = findHighestOutputRobot( simulator.getRobots() );
        
        double distanceLeaderToNest;
        distanceLeaderToNest = leader.getDistanceBetween( nestPos );

        double bfc1 = (Math.max(0.0, maxDist-distanceLeaderToNest) / maxDist);
        
        
        //* Second behavioral fitness component: keep all others away from nest
        double  distanceAllOthers = 0.0;
        for (Robot robot : simulator.getRobots()) {
            if ( !robot.equals( leader ) ) {
                distanceAllOthers += Math.min( 1, robot.getDistanceBetween( nestPos ) ) / maxDist;
                System.out.println("dist robot " + robot.getId() + " " + robot.getDistanceBetween( nestPos ));
                System.out.println("fit robot " + robot.getId() + " " + Math.min( 1, robot.getDistanceBetween( nestPosition ) ) / maxDist);
            }
        }
        
        double bfc2;
        if ( simulator.getRobots().size() > 1 ) {
            bfc2 = distanceAllOthers / (simulator.getRobots().size() - 1);
        }else{
            bfc2 = 0.0;
        }
        
        System.out.println("");
        System.out.println("bfc1=" + (bfc1/totalSteps) );
        System.out.println("bfc2=" + (bfc2/totalSteps) );
        
        
        currentFitness += 0.75 * (bfc1/totalSteps) + 0.25 * (bfc2/totalSteps);
        
        
        
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