package evolutionaryrobotics.evaluationfunctions;

import java.util.ArrayList;
import mathutils.Vector2d;
import simulation.Simulator;
import simulation.environment.CooperativeForagingEnvironment;
import simulation.robot.Robot;
import simulation.robot.actuators.TwoWheelActuator;
import simulation.util.Arguments;

public class CooperativeForagingEnergyEvaluationFunction extends EvaluationFunction{
	
    
    protected Vector2d   nestPosition = new Vector2d(0, 0);
        
    
    private int preys;                      //initial number of preys
                                            //in the environment
    
    private int teamSize;                   //number of robots
        
        
    private int preysCaptured;              //number of preys captured

    
    private Double spentEnergy;             //total distance run by 
                                            // all robots
    
    private TwoWheelActuator act;           //temporary variable to hold
                                            //a twoWheelActuator
    
    
    private Simulator simulator;

    
    
    public CooperativeForagingEnergyEvaluationFunction(Arguments args) {
            super(args);	
            
            preysCaptured = 0;
            spentEnergy = 0.0;
    }

    
    
    @Override
    public double getFitness() {
        return  ( 1.0 * preysCaptured * preysCaptured / preys ) / spentEnergy;
        //return 1.0 / ( 1 + spentEnergy );
    }

    
    
    @Override
    public void update(Simulator simulator) {

        if( simulator == null )
            this.simulator = simulator;

        //TODO next two lines are ugly, fix it when time is a surplus
        preys = ((CooperativeForagingEnvironment)(simulator.getEnvironment())).getNumberOfPreys(); 
        teamSize = simulator.getRobots().size();
        
        
        preysCaptured = ((CooperativeForagingEnvironment)(simulator.getEnvironment())).getNumberOfFoodSuccessfullyForaged();
        
        spentEnergy = getSpentEnergy( simulator.getRobots() );
    }
    
    
    /**
     * Determines the energy spent
     * by all robots of a collection
     * @param robots the robots 
     * collection
     * @return the total energy
     * spent by all robots
     */
    private double getSpentEnergy( ArrayList<Robot> robots ){
        
        double energy;                                //total distance
        energy = 0.0;                                 //run by all robots
        
        
        
        for ( Robot robot : robots ) {
            
            act = (TwoWheelActuator) robot.getActuatorByType( TwoWheelActuator.class );
            
            energy += act.getSpentEnergy();         //add distance run by
                                                    //robot to total distance
        }
        
        return energy;
    }
    
    
}