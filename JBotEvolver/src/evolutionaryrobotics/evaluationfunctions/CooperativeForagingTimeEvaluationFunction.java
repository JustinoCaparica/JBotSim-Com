package evolutionaryrobotics.evaluationfunctions;

import mathutils.Vector2d;
import simulation.Simulator;
import simulation.environment.CooperativeCircleForagingEnvironment;
import simulation.environment.CooperativeForagingEnvironment;
import simulation.environment.CooperativeMatrixForagingEnvironment;
import simulation.robot.Robot;
import simulation.util.Arguments;

public class CooperativeForagingTimeEvaluationFunction extends EvaluationFunction{
	
    
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
    
    
    
    
    
    public CooperativeForagingTimeEvaluationFunction(Arguments args) {
            super(args);	
            
            preysCaptured = 0;
            timeStep = 0.0;
    }

    
    
    @Override
    public double getFitness() {
        
        
        //reward for preys captured
//        Double fitPreys = ( 1.0 * (preysCaptured*preysCaptured) / (preys*preys) );
//        
//        
//        //time penalty: the longer it takes to capture all captured  preys, the higher the penalty
//        Double timePenalty = fitPreys - ( 1.0 * ( (preysCaptured-1) * (preysCaptured-1) ) / (preys*preys) );
//        timePenalty = ( 0.66 * timePenalty) * ( (1.0 * timeStep) / (totalSteps) );
//        
//        
//        return fitPreys - timePenalty;
        
        
        //version 0.3
//        return ( 1.0 * preysCaptured / preys ); 


        
        
        //version 0.2
//        Double fitPreys = ( 1.0 * preysCaptured / preys );
//        Double timePenalty = (0.66*1.0/preys) * ( (1.0 * timeStep) / (totalSteps) );
//        return fitPreys - timePenalty;
        
        
        //version 0.1
        if ( preysCaptured == 0 ) {
            return 0.0;
        }
        
        return 1.0 - ( (1.0 * timeStep) / (totalSteps) );
    
        
    }

    
    
    @Override
    public void update(Simulator simulator) {

        //TODO next lines are ugly, fix it when time is a surplus
        if ( (simulator.getEnvironment()).getClass().equals( CooperativeForagingEnvironment.class )) {
            CooperativeForagingEnvironment env;
            env = (CooperativeForagingEnvironment)(simulator.getEnvironment());
            preys = env.getNumberOfPreys(); 
            teamSize = simulator.getRobots().size();
            currentTime = simulator.getTime();

            preysCaptured = env.getNumberOfFoodSuccessfullyForaged();

            timeStep = env.getLastPreyCaptureTime();
            totalSteps = env.getSteps();
        }
        else if ((simulator.getEnvironment()).getClass().equals( CooperativeMatrixForagingEnvironment.class )) {
            CooperativeMatrixForagingEnvironment env;
            env = (CooperativeMatrixForagingEnvironment)(simulator.getEnvironment());
            preys = env.getNumberOfPreys(); 
            teamSize = simulator.getRobots().size();
            currentTime = simulator.getTime();

            preysCaptured = env.getNumberOfFoodSuccessfullyForaged();

            timeStep = env.getLastPreyCaptureTime();
            totalSteps = env.getSteps();
        }
        else {
            CooperativeCircleForagingEnvironment env;
            env = (CooperativeCircleForagingEnvironment)(simulator.getEnvironment());
            preys = env.getNumberOfPreys(); 
            teamSize = simulator.getRobots().size();
            currentTime = simulator.getTime();

            preysCaptured = env.getNumberOfFoodSuccessfullyForaged();

            timeStep = env.getLastPreyCaptureTime();
            totalSteps = env.getSteps();
        }
        
        
        
        
        
        
    }
}