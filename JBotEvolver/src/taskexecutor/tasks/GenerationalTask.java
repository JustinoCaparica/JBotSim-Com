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
        
        
	public GenerationalTask(JBotEvolver jBotEvolver, int samples, Chromosome chromosome, long seed) {
		super(jBotEvolver);
		this.samples = samples;
		this.chromosome = chromosome;
		this.random = new Random(seed);
                
                fitInfo = new HashMap<>();
                
                
        }
	
	@Override
	public void run() {
		
		for(int i = 0 ; i < samples ; i++) {
			
			jBotEvolver.getArguments().get("--environment").setArgument("fitnesssample", i);
			
			Simulator simulator = jBotEvolver.createSimulator(random.nextLong());
			
			simulator.setFileProvider(getFileProvider());
			
//			ArrayList<Robot> robots = jBotEvolver.createRobots(simulator);
//			jBotEvolver.setChromosome(robots, chromosome);
			ArrayList<Robot> robots = jBotEvolver.createRobots(simulator, chromosome);
			simulator.addRobots(robots);
			
			EvaluationFunction eval = EvaluationFunction.getEvaluationFunction(jBotEvolver.getArguments().get("--evaluation"));
			simulator.addCallback(eval);
			
			simulator.simulate();
			
			fitness+= eval.getFitness();
                        
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