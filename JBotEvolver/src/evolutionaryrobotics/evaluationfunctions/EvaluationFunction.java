package evolutionaryrobotics.evaluationfunctions;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import simulation.Updatable;
import simulation.util.Arguments;

public abstract class EvaluationFunction implements Serializable, Updatable {
	protected double fitness;

        private final Map<String, Double> fitnessInfo;    //structure to store
                                                    //aditional fitness info
                                                    //map keys are the parameters
                                                    //map values are the values
                                                    //associated with each parameter
        
        
	public EvaluationFunction(Arguments args) {
            fitnessInfo = new HashMap<>();
           
        }

	public double getFitness() {
		return fitness;
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
	
        public void setFitnessInfoValue( String parameter, Double value ){
            fitnessInfo.put(parameter, value);
        }
        
        public Double getFitnessInfoValue( String parameter ){
            return fitnessInfo.get(parameter);
        }
        
        
	public static EvaluationFunction getEvaluationFunction(Arguments arguments) {
		if (!arguments.getArgumentIsDefined("classname"))
			throw new RuntimeException("Evaluation 'classname' not defined: "+arguments.toString());

		String evaluationName = arguments.getArgumentAsString("classname");
		
		try {
			Constructor<?>[] constructors = Class.forName(evaluationName).getDeclaredConstructors();
			for (Constructor<?> constructor : constructors) {
				Class<?>[] params = constructor.getParameterTypes();
				if (params.length == 1 && params[0] == Arguments.class) {
					return (EvaluationFunction) constructor.newInstance(arguments);
				}
			}
		} catch (Exception e) {
			
			// NEAT needs to instantiate EvalFunctions locally, so it is necessary to check this
			// if we are using Conillon...
			if(evaluationName.startsWith("__")) {
				System.out.println(evaluationName);
				evaluationName = evaluationName.substring(evaluationName.indexOf('.')+1,evaluationName.length());
				arguments.setArgument("classname", evaluationName);
				return getEvaluationFunction(arguments);
			}
						
			e.printStackTrace();
			System.exit(-1);
		}
		throw new RuntimeException("Unknown evaluation: " + evaluationName);
	}
}