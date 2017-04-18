package taskexecutor.tasks;

import java.util.ArrayList;
import java.util.Random;

import evolutionaryrobotics.JBotEvolver;
import evolutionaryrobotics.evaluationfunctions.EvaluationFunction;
import evolutionaryrobotics.evaluationfunctions.EvaluationFunctionInfo;
import evolutionaryrobotics.neuralnetworks.MultipleChromosome;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import result.Result;
import simulation.Simulator;
import simulation.robot.Robot;
import taskexecutor.results.SimpleFitnessResult;

public class MultipleChromosomeTask extends JBotEvolverTask{

	private int samples;
	private double fitness = 0;
	private MultipleChromosome chromosome;
	private Random random;
	private long seed;

        private EvaluationFunctionInfo fitInfo;    //fitness info
        
        
	public MultipleChromosomeTask(JBotEvolver jBotEvolver, int samples, MultipleChromosome chromosome, long seed) {
		super(jBotEvolver);
		this.samples = samples;
		this.chromosome = chromosome;
		this.random = new Random(seed);
		this.seed = seed;
                
                fitInfo = new EvaluationFunctionInfo();
	}

        
	@Override
	public void run() {

		for(int i = 0 ; i < samples ; i++) {

			jBotEvolver.getArguments().get("--environment").setArgument("fitnesssample", i);
			Simulator simulator = jBotEvolver.createSimulator(random.nextLong());
			simulator.setFileProvider(getFileProvider());

			//for(int j = 0; j < JoinedGenerationalEvolution.CHROM_NUM; j++){
			//				ArrayList<Robot> robots = jBotEvolver.createRobots(simulator);
			//				jBotEvolver.setChromosome(robots, chromosome.getChromosome(j));
			ArrayList<Robot> robots = jBotEvolver.createRobots(simulator, chromosome);
			//System.out.println("Number of Robots: " + robots.size() + " RandomSeed: " + seed);
			simulator.addRobots(robots);
			//}

			EvaluationFunction eval = EvaluationFunction.getEvaluationFunction(jBotEvolver.getArguments().get("--evaluation"));
			simulator.addCallback(eval);
			simulator.simulate();

			fitness+= eval.getFitness();
                        
                        //average the values for the fitness information
                        Iterator<String> it = eval.getFitnessInfo().keySet().iterator();
                        while( it.hasNext() ){
                            String key = it.next();
                            if ( fitInfo.getFitnessInfoValue(key) == null ) {
                                fitInfo.setFitnessInfoValue(key, eval.getFitnessInfo().getFitnessInfoValue(key)/samples );
                            }
                            else{
                                fitInfo.setFitnessInfoValue(key, fitInfo.getFitnessInfoValue(key) + (eval.getFitnessInfo().getFitnessInfoValue(key)/samples) );
                            }
                        }
		}
	}
        
        
	@Override
	public Result getResult() {
            //SimpleFitnessResult fr = new SimpleFitnessResult(getId(),chromosome.getID(),fitness/samples);
            SimpleFitnessResult fr = new SimpleFitnessResult(getId(),chromosome.getID(),fitness/samples, fitInfo);
            
            
            
            
            return fr;
	}

}
