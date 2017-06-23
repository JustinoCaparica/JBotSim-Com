package evolutionaryrobotics.evaluationfunctions;

import simulation.Simulator;
import simulation.environment.ForagingEnvironment;
import simulation.util.Arguments;


/**
 * An evaluation function that counts
 * the number of preys captured.
 * @author guest
 */
public class CapturedPreysCountEvaluationFunction extends EvaluationFunction{
	
    
        
    
    private int preysCaptured;              //number of preys captured

    
    
    
    public CapturedPreysCountEvaluationFunction( Arguments args ) {
            super(args);	
            
            preysCaptured = 0;
    }

    
    
    @Override
    public double getFitness() {
        return preysCaptured;
    }

    
    
    @Override
    public void update(Simulator simulator) {

        preysCaptured = ((ForagingEnvironment)(simulator.getEnvironment())).getCapturedPreyCount();

    }
}