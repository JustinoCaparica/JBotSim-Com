package evolutionaryrobotics.evaluationfunctions;

import simulation.Simulator;
import simulation.environment.ForagingEnvironment;
import simulation.util.Arguments;


/**
 * An evaluation function that counts
 * the number of preys captured and normalizes
 * the value according to the initial number
 * of preys.
 * @author guest
 */
public class CapturedPreysCountNormEvaluationFunction extends EvaluationFunction{
	
    
        
    
    private int preys;                      //initial number of preys
                                            //in the environment
    
    private int preysCaptured;              //number of preys captured

    
    
    
    public CapturedPreysCountNormEvaluationFunction( Arguments args ) {
            super(args);	
            
            preysCaptured = 0;
    }

    
    
    @Override
    public double getFitness() {
        return 1.0 * preysCaptured / preys;
    }

    
    
    @Override
    public void update(Simulator simulator) {

        //TODO next line is ugly, fix it when time is a surplus
        preys = ((ForagingEnvironment)(simulator.getEnvironment())).getInitialPreyCount(); 
        
        preysCaptured = ((ForagingEnvironment)(simulator.getEnvironment())).getCapturedPreyCount();

    }
}