package evolutionaryrobotics.evaluationfunctions;

import mathutils.Vector2d;
import simulation.Simulator;
import simulation.environment.CooperativeCircleForagingEnvironment;
import simulation.environment.CooperativeForagingEnvironment;
import simulation.environment.GoToEnvironment;
import simulation.robot.Robot;
import simulation.util.Arguments;

/**
 * Evaluation function for the GoToEnvironment
 * @author gus
 */
public class GoToEvaluationFunction extends EvaluationFunction{
	
    
    protected Vector2d   nestPosition = new Vector2d(0, 0);
        
    
    
    private Double preyCaptureTime;         //time stamp of the moment
                                            //when the prey was captured
    
    private int totalSteps;                 //number of time steps
                                            //for the duration of the simulation
        
    
    
    
    
    
    
    public GoToEvaluationFunction(Arguments args) {
            super(args);	
            
    }

    
    
    @Override
    public double getFitness() {
        
        if ( preyCaptureTime == null ) {
            return 0.0;
        }
        
        //version 0.2
//        Double fitPreys = ( 1.0 * preysCaptured / preys );
//        Double timePenalty = (0.66*1.0/preys) * ( (1.0 * timeStep) / (totalSteps) );
//        return fitPreys - timePenalty;
        
        
        //version 0.1
        return 1.0 - ( (1.0 * preyCaptureTime) / (totalSteps) );
        
    }

    
    
    @Override
    public void update( Simulator simulator ) {

        GoToEnvironment env;
        env = (GoToEnvironment)( simulator.getEnvironment() );
        
        preyCaptureTime = env.getPreyCaptureTime();
        totalSteps      = env.getSteps();
        
    }
}