package evolutionaryrobotics.evaluationfunctions;

import mathutils.Vector2d;
import simulation.Simulator;
import simulation.environment.CooperativeCircleForagingEnvironment;
import simulation.environment.CooperativeForagingEnvironment;
import simulation.environment.CooperativeMatrixForagingEnvironment;
import simulation.environment.CooperativeNestForagingEnvironment;
import simulation.environment.ReturnToNestEnvironment;
import simulation.robot.Robot;
import simulation.util.Arguments;

public class ReturnToNestTimeEvaluationFunction extends EvaluationFunction{
	
    
    protected Vector2d   nestPosition = new Vector2d(0, 0);
        
    
    private int preys;                      //initial number of preys
                                            //in the environment
    
    private int teamSize;                   //number of robots
        
    
    private double timeStep;                //time stamp of the moment
                                            //when the prey was captured
    
    private int totalSteps;                 //number of time steps
                                            //for the duration of the simulation
    
        
    private int preysCaptured;              //number of preys captured

    

    private Double currentTime;             //current simulation time
    
    
    private Boolean teamInNest;             //are all team elements
                                            //inside the nest?
    
    private Double teamDistanceToNest;      //sum of all team elements
                                            //distance to nest
    
    
    public ReturnToNestTimeEvaluationFunction(Arguments args) {
            super(args);	
            
            preysCaptured   = 0;
            timeStep        = 0.0;
            
            
            teamInNest          = false;
            teamDistanceToNest  = 0.0;
    }

    
    
    @Override
    public double getFitness() {
        

        //count the number of robots inside the nest
        if ( teamInNest ) {
            return 100.0 - (currentTime / totalSteps * 100);
        }
        else{
            return 0.0;     //TODO add a way to reward teams that are closer to the nest
        }
        
    
        
    }

    
    
    @Override
    public void update(Simulator simulator) {

        //TODO next lines are ugly, fix it when time is a surplus
        if ( (simulator.getEnvironment()).getClass().equals( ReturnToNestEnvironment.class )) {
            ReturnToNestEnvironment env;
            env = (ReturnToNestEnvironment)(simulator.getEnvironment());
            teamSize = simulator.getRobots().size();
            currentTime = simulator.getTime();

            teamInNest = env.isTeamInNest();

            totalSteps = env.getSteps();
        }
        
        
    }
}