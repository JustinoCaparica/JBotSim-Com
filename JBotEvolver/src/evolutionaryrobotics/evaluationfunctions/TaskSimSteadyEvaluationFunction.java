package evolutionaryrobotics.evaluationfunctions;

import simulation.Simulator;
import simulation.environment.ForagingEnvironment;
import simulation.physicalobjects.TaskSim;
import simulation.util.Arguments;


/**
 * An evaluation function that measures
 * how high is the level of completeness
 * of a TaskSimSteady task
 * @author guest
 */
public class TaskSimSteadyEvaluationFunction extends EvaluationFunction{
	
    
    private TaskSim task;                   //the task
    
    private Double taskLevel;               //level of completeness
                                            //of the task

    private Double taskMaxLevel;            //max level of the task
    
    
    
    public TaskSimSteadyEvaluationFunction( Arguments args ) {
            super(args);	
            
            taskLevel = 0.0;
    }

    
    
    @Override
    public double getFitness() {
        return taskLevel / taskMaxLevel;
    }

    
    
    @Override
    public void update(Simulator simulator) {

        task            = ((ForagingEnvironment)(simulator.getEnvironment())).getTaskSim();
        taskLevel       = task.getCurrentLevel();
        taskMaxLevel    = task.getMaxLevel();

    }
}