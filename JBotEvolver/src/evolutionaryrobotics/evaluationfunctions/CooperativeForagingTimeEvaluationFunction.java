package evolutionaryrobotics.evaluationfunctions;

import mathutils.Vector2d;
import simulation.Simulator;
import simulation.environment.CooperativeForagingEnvironment;
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

    
    private Simulator simulator;

    
    
    public CooperativeForagingTimeEvaluationFunction(Arguments args) {
            super(args);	
            
            preysCaptured = 0;
            timeStep = 0.0;
    }

    
    
    @Override
    public double getFitness() {
//
//        System.out.println("timeStep:" + timeStep);
//        System.out.println("totalSteps:" + totalSteps);
//        System.out.println("fitness:" + (1.0 - (timeStep / totalSteps)) );
//        
//        System.out.println("");
        return  1.0 - (timeStep / totalSteps);
    }

    
    
    @Override
    public void update(Simulator simulator) {

        //TODO next two lines are ugly, fix it when time is a surplus
        CooperativeForagingEnvironment env;
        env = (CooperativeForagingEnvironment)(simulator.getEnvironment());
        
        
        preys = env.getNumberOfPreys(); 
        teamSize = simulator.getRobots().size();
        
        
        preysCaptured = env.getNumberOfFoodSuccessfullyForaged();
        
        timeStep = env.getLastPreyCaptureTime();
        totalSteps = env.getSteps();
        
    }
}