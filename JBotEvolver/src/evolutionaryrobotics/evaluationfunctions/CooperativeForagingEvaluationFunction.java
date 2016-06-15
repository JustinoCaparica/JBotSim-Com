package evolutionaryrobotics.evaluationfunctions;

import mathutils.Vector2d;
import simulation.Simulator;
import simulation.environment.CooperativeForagingEnvironment;
import simulation.robot.Robot;
import simulation.util.Arguments;

public class CooperativeForagingEvaluationFunction extends EvaluationFunction{
	protected Vector2d   nestPosition = new Vector2d(0, 0);
	protected int numberOfFoodForaged = 0;
        
        private Simulator simulator;

	public CooperativeForagingEvaluationFunction(Arguments args) {
		super(args);	
	}

	//@Override
	public double getFitness() {
            
		return fitness + numberOfFoodForaged;
	}

	//@Override
	public void update(Simulator simulator) {
            
            if( simulator == null )
                this.simulator = simulator;
            
            
            int numberOfRobotsBeyondForbidenLimit   = 0;
            int numberOfRobotsBeyondForagingLimit   = 0;

            double forbidenArea =  ((CooperativeForagingEnvironment)(simulator.getEnvironment())).getForbiddenArea();
            double foragingArea =  ((CooperativeForagingEnvironment)(simulator.getEnvironment())).getForageRadius();	

            
            double distanceToNest;
            for( Robot r : simulator.getEnvironment().getRobots() ){
                
                distanceToNest = r.getPosition().distanceTo( nestPosition );

                if( distanceToNest > forbidenArea ){            //count robots
                        numberOfRobotsBeyondForbidenLimit++;    //beyond limit
                } 
                else if( distanceToNest > foragingArea ){       //count robots
                        numberOfRobotsBeyondForagingLimit++;    //beyond foraging limit
                }

            }
            
            //TODO why is fitness incremental instead of instantaneous?
            fitness += (double) numberOfRobotsBeyondForbidenLimit * -0.1 + numberOfRobotsBeyondForagingLimit * -0.0001;
            numberOfFoodForaged = ((CooperativeForagingEnvironment)(simulator.getEnvironment())).getNumberOfFoodSuccessfullyForaged();
            
            //TODO account for distance run by robots; 
            //the higher the distance the lower the fitness, for the same level of foragedFood
            
            
	}
}