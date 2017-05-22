package evolutionaryrobotics.evaluationfunctions;

import mathutils.Vector2d;
import simulation.Simulator;
import simulation.environment.CooperativeForagingEnvironment;
import simulation.environment.CooperativeNestForagingEnvironment;
import simulation.environment.ForagingEnvironment;
import simulation.robot.Robot;
import simulation.util.Arguments;

public class CooperativeForagingEvaluationFunction extends EvaluationFunction{
	
    
        
    
    private int preys;                      //initial number of preys
                                            //in the environment
    
    private int preysCaptured;              //number of preys captured

    
    private Double fitCutValue;             //fitness cut value: if
                                            //fitness below cut value
                                            //then fitness becomes 0
    
    //temp variables: declared here for performance
    private Double fit;
    
    
    public CooperativeForagingEvaluationFunction( Arguments args ) {
            super(args);	
            
            preysCaptured = 0;
            fitCutValue = 0.13;
    }

    
    
    @Override
    public double getFitness() {
        
        fit = 1.0 * preysCaptured / preys;          //fitness before cut
        
        
        if ( fit < fitCutValue ) {      //cut
            return 0.0;
        }
        
        
                                        //scale
        return  ( fit - fitCutValue ) / ( 1.0 - fitCutValue );
    }

    
    
    @Override
    public void update(Simulator simulator) {

        //TODO next line is ugly, fix it when time is a surplus
        preys = ((ForagingEnvironment)(simulator.getEnvironment())).getInitialPreyCount(); 
        
        preysCaptured = ((ForagingEnvironment)(simulator.getEnvironment())).getCapturedPreyCount();

    }
}