package evolutionaryrobotics.evaluationfunctions;

import mathutils.Vector2d;
import simulation.Simulator;
import simulation.environment.CooperativeForagingEnvironment;
import simulation.robot.Robot;
import simulation.util.Arguments;

public class CooperativeForagingEvaluationFunction extends EvaluationFunction{
	
    
    protected Vector2d   nestPosition = new Vector2d(0, 0);
        
    
    private int preys;                      //initial number of preys
                                            //in the environment
    
    private int teamSize;                   //number of robots
        
        
    private int preysCaptured;              //number of preys captured

    
    private Simulator simulator;

    
    
    public CooperativeForagingEvaluationFunction(Arguments args) {
            super(args);	
            
            preysCaptured = 0;
            
    }

    
    
    @Override
    public double getFitness() {

            return preysCaptured / preys / teamSize;
    }

    
    
    @Override
    public void update(Simulator simulator) {

        if( simulator == null )
            this.simulator = simulator;

        //TODO next two lines are ugly, fix it when time is a surplus
        preys = ((CooperativeForagingEnvironment)(simulator.getEnvironment())).getNumberOfPreys(); 
        teamSize = simulator.getRobots().size();
        
        
        preysCaptured = ((CooperativeForagingEnvironment)(simulator.getEnvironment())).getNumberOfFoodSuccessfullyForaged();

    }
}