package taskexecutor.results;

import evolutionaryrobotics.evaluationfunctions.EvaluationFunctionInfo;
import java.util.Map;
import result.Result;

public class SimpleFitnessResult extends Result {
	private int chromosomeId;
	private double fitness = 0;
        
        private EvaluationFunctionInfo fitnessInfo;
	

	public SimpleFitnessResult(int taskId, int chromosomeId, double fitness) {
		super(taskId);
		this.chromosomeId = chromosomeId;
		this.fitness = fitness;
	}

        public SimpleFitnessResult(int taskId, int chromosomeId, double fitness, EvaluationFunctionInfo fitnessInfo) {
		super(taskId);
		this.chromosomeId = chromosomeId;
		this.fitness = fitness;
                this.fitnessInfo = fitnessInfo;
	}
        
	public double getFitness() {
		return fitness;
	}

	public int getChromosomeId() {
		return chromosomeId;
	}

        /**
         * Gets a structure that stores
         * additional fitness information
         * @return an EvaluationFunctionInfo
         * object
         */
        public EvaluationFunctionInfo getFitnessInfo() {
            return fitnessInfo;
        }
	
        
        
        
        
	
}
