package evolutionaryrobotics.evaluationfunctions;

import mathutils.Vector2d;
import simulation.Simulator;
import simulation.environment.CooperativeForagingEnvironment;
import simulation.robot.Robot;
import simulation.util.Arguments;

public class CooperativeForagingDisableRobotsEvaluationFunction extends EvaluationFunction{
	
    
    protected Vector2d   nestPosition = new Vector2d(0, 0);
        
    
    private int preys;                      //initial number of preys
                                            //in the environment
    
    private int teamSize;                   //number of robots
        
        
    private int preysCaptured;              //number of preys captured

    
    private Simulator simulator;

    
    
    public CooperativeForagingDisableRobotsEvaluationFunction( Arguments args ) {
            super(args);	
            
            preysCaptured = 0;
            
    }

    
    
    
    
    @Override
    public double getFitness() {

        if ( simulator == null ) {                      //if the simulator has
            return 0.0;                                 //not been initiated yet
        }
        
        
        int disabledRobots = 0;
        for ( Robot robot : simulator.getRobots() ) {   //count the number
            if ( !robot.isEnabled() ) {                 //of disabled robots
                disabledRobots++;
            }
        }
        
                
                
        return  ( 1.0 * preysCaptured / preys ) / ( (1 + disabledRobots) * (1 + disabledRobots) ) ;
    }

    
    
    
    
    
    @Override
    public void update(Simulator simulator) {

        if( this.simulator == null )
            this.simulator = simulator;

        //TODO next two lines are ugly, fix it when time is a surplus
        preys = ((CooperativeForagingEnvironment)(simulator.getEnvironment())).getNumberOfPreys(); 
        teamSize = simulator.getRobots().size();
        
        
        preysCaptured = ((CooperativeForagingEnvironment)(simulator.getEnvironment())).getNumberOfFoodSuccessfullyForaged();

    }
}