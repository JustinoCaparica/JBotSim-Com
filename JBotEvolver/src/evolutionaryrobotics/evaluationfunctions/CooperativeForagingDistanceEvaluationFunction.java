package evolutionaryrobotics.evaluationfunctions;

import java.util.ArrayList;
import mathutils.Vector2d;
import simulation.Simulator;
import simulation.environment.CooperativeForagingEnvironment;
import simulation.robot.Robot;
import simulation.util.Arguments;

public class CooperativeForagingDistanceEvaluationFunction extends EvaluationFunction{
	
    
    protected Vector2d   nestPosition = new Vector2d(0, 0);
        
    
    private int preys;                      //initial number of preys
                                            //in the environment
    
    private int teamSize;                   //number of robots
        
        
    private int preysCaptured;              //number of preys captured

    
    private Double distance;                //total distance run by 
                                            // all robots
    
    private Simulator simulator;

    
    
    public CooperativeForagingDistanceEvaluationFunction(Arguments args) {
            super(args);	
            
            preysCaptured = 0;
            distance = 0.0;
    }

    
    
    @Override
    public double getFitness() {

            return  ( 1.0 * preysCaptured / preys ) / distance;
    }

    
    
    @Override
    public void update(Simulator simulator) {

        if( simulator == null )
            this.simulator = simulator;

        //TODO next two lines are ugly, fix it when time is a surplus
        preys = ((CooperativeForagingEnvironment)(simulator.getEnvironment())).getNumberOfPreys(); 
        teamSize = simulator.getRobots().size();
        
        
        preysCaptured = ((CooperativeForagingEnvironment)(simulator.getEnvironment())).getNumberOfFoodSuccessfullyForaged();

        distance = getDistance( simulator.getRobots() );
    }
    
    
    /**
     * Determines the distance run
     * by all robots of a collection
     * @param robots the robots 
     * collection
     * @return the total distance
     * run by all robots
     */
    private double getDistance( ArrayList<Robot> robots ){
        
        double dist;                                //total distance
        dist = 0.0;                                 //run by all robots
        
        
        for ( Robot robot : robots ) {
            
            dist += robot.getDistance();            //add distance run by
                                                    //robot to total distance
        }
        
        return dist;
    }
    
    
}