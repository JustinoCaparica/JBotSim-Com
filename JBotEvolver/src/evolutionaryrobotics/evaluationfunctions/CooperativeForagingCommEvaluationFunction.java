package evolutionaryrobotics.evaluationfunctions;

import mathutils.Vector2d;
import simulation.Simulator;
import simulation.environment.CooperativeForagingEnvironment;
import simulation.robot.Robot;
import simulation.util.Arguments;


/**
 * Evaluation function to force robots to exchange messages. 
 * This class is for test purposes only.
 * @author gus
 */
public class CooperativeForagingCommEvaluationFunction extends EvaluationFunction{
	
    
    protected Vector2d   nestPosition = new Vector2d(0, 0);
        
    
    private int preys;                      //initial number of preys
                                            //in the environment
    
    private int teamSize;                   //number of robots
        
        
    private int preysCaptured;              //number of preys captured

    
    private int numberOfMessages;           //total number of messages 
                                            //received by all robots
    
    private Simulator simulator;

    
    
    public CooperativeForagingCommEvaluationFunction(Arguments args) {
            super(args);	
            
            preysCaptured = 0;
            
            numberOfMessages = 0;
            
    }

    
    
    @Override
    public double getFitness() {

            return  (( 1.0 * preysCaptured / preys ) / teamSize) + (numberOfMessages / teamSize) ;
    }

    
    
    @Override
    public void update(Simulator simulator) {

        if( simulator == null )
            this.simulator = simulator;

        //TODO next two lines are ugly, fix it when time is a surplus
        preys = ((CooperativeForagingEnvironment)(simulator.getEnvironment())).getNumberOfPreys(); 
        teamSize = simulator.getRobots().size();
        
        
        numberOfMessages = 0;
        for ( Robot robot : simulator.getRobots() ) {                   //count the
            numberOfMessages += robot.getMsgBox().receivedMsgsCount();  //number of total
        }                                                               //messages received
                                                                        //by all robots
        
        
        preysCaptured = ((CooperativeForagingEnvironment)(simulator.getEnvironment())).getNumberOfFoodSuccessfullyForaged();

    }
}