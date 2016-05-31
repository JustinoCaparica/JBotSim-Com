package evolutionaryrobotics.evaluationfunctions;

import mathutils.Vector2d;
import simulation.Simulator;
import simulation.environment.CooperativeForagingEnvironment;
import simulation.environment.RoundForageEnvironment;
import simulation.robot.Robot;
import simulation.robot.sensors.PreyCarriedSensor;
import simulation.util.Arguments;

public class CooperativeForagingEvaluationFunction extends EvaluationFunction{
	protected Vector2d   nestPosition = new Vector2d(0, 0);
	protected int numberOfFoodForaged = 0;

	public CooperativeForagingEvaluationFunction(Arguments args) {
		super(args);	
	}

	//@Override
	public double getFitness() {
		return fitness + numberOfFoodForaged;
	}

	//@Override
	public void update(Simulator simulator) {
            
            int numberOfRobotsWithPrey              = 0;
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
            
            fitness += (double) numberOfRobotsBeyondForbidenLimit * -0.1 + numberOfRobotsBeyondForagingLimit * -0.0001;
            numberOfFoodForaged = ((CooperativeForagingEnvironment)(simulator.getEnvironment())).getNumberOfFoodSuccessfullyForaged();
            
	}
}