package taskexecutor.results;

import java.util.Map;
import result.Result;

public class SimpleFitnessResult extends Result {
	private int chromosomeId;
	private double fitness = 0;
        
        private Map<String, Double> fitnessInfo;
	

	public SimpleFitnessResult(int taskId, int chromosomeId, double fitness) {
		super(taskId);
		this.chromosomeId = chromosomeId;
		this.fitness = fitness;
	}

        public SimpleFitnessResult(int taskId, int chromosomeId, double fitness, Map<String, Double> fitnessInfo) {
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
         * @return a map where keys are the
         * info parameters and map values are the 
         * values associated with each parameter
         */
        public Map<String, Double> getFitnessInfo() {
            return fitnessInfo;
        }
	
        
        
        
        
	
}
