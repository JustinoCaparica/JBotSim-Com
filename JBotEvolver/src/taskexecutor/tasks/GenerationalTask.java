package taskexecutor.tasks;

import java.util.ArrayList;
import java.util.Random;

import evolutionaryrobotics.JBotEvolver;
import evolutionaryrobotics.evaluationfunctions.EvaluationFunction;
import evolutionaryrobotics.neuralnetworks.Chromosome;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import result.Result;
import simulation.Simulator;
import simulation.robot.Robot;
import taskexecutor.results.SimpleFitnessResult;
import tests.Cronometer;

public class GenerationalTask extends JBotEvolverTask {
	
	private int samples;
	private double fitness = 0;
	private Chromosome chromosome;
	private Random random;
        

	private Map<String, Double> fitInfo;    //fitness info
        
        /**
         * 
         * @param jBotEvolver
         * @param samples number of trials to test
         * the controller
         * @param chromosome the controller description,
         * used to generate the controller
         * @param seed the generation random seed, used to
         * generate random numbers for each trial
         */
	public GenerationalTask(JBotEvolver jBotEvolver, int samples, Chromosome chromosome, long seed) {
		super(jBotEvolver);
		this.samples = samples;
		this.chromosome = chromosome;
		this.random = new Random(seed);    
                
                fitInfo = new HashMap<>();
                
                
        }
	
	@Override
	public void run() {
		
            Long seed;
            
		for(int i = 0 ; i < samples ; i++) {
			
			jBotEvolver.getArguments().get("--environment").setArgument("fitnesssample", i);
			
                        seed = random.nextLong();                               //generate trial's seed
			Simulator simulator = jBotEvolver.createSimulator(seed);  
                        
			
			simulator.setFileProvider(getFileProvider());
			
//			ArrayList<Robot> robots = jBotEvolver.createRobots(simulator);
//			jBotEvolver.setChromosome(robots, chromosome);
			ArrayList<Robot> robots = jBotEvolver.createRobots(simulator, chromosome);
			simulator.addRobots(robots);
			
			EvaluationFunction eval = EvaluationFunction.getEvaluationFunction(jBotEvolver.getArguments().get("--evaluation"));
			simulator.addCallback(eval);
			
			simulator.simulate();
			
			fitness+= eval.getFitness();
                        
                        //System.out.println("seed:" + seed);
                        fitInfo.put("trialSeed" + i, seed.doubleValue() );      //store trial's seed
                        fitInfo.put("trialFitness" + i, eval.getFitness() );        //store trial's fitness
                        
                        //average the values for the fitness information
                        Iterator<String> it = eval.getFitnessInfo().keySet().iterator();
                        while( it.hasNext() ){
                            String key = it.next();
                            if ( fitInfo.get(key) == null ) {
                                fitInfo.put( key, eval.getFitnessInfo().get(key)/samples );
                            }
                            else{
                                fitInfo.put( key, fitInfo.get(key) + (eval.getFitnessInfo().get(key)/samples) );
                            }
                        }
                        
		}
                
                
	}
        
        
	@Override
	public Result getResult() {
		//SimpleFitnessResult fr = new SimpleFitnessResult(getId(),chromosome.getID(),fitness/samples);
		
                SimpleFitnessResult fr = new SimpleFitnessResult(getId(),chromosome.getID(),fitness/samples, fitInfo);
                
                //System.out.println("evalFunc.getFitnessInfo().get(\"bfc1Accum\"):" + fitInfo.get("bfc1Accum"));
                
                return fr;
	}
}