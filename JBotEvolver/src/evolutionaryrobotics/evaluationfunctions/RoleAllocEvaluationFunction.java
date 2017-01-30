package evolutionaryrobotics.evaluationfunctions;

import mathutils.Vector2d;
import simulation.Simulator;
import simulation.robot.Robot;
import simulation.robot.actuators.RoleActuator;
import simulation.util.Arguments;

public class RoleAllocEvaluationFunction extends EvaluationFunction{
	
    
    protected Vector2d   nestPosition = new Vector2d(0, 0);
        
    
    private int numOfRobots;                //number of robots in the simulation
    
    private int totalSteps;                 //number of time steps
                                            //for the duration of the simulation
    

    private Double currentStep;             //currentStep
    
    
    private Double maxOutput;               //maximum output amongst all robots
    private Double[] outputs;               //outputs of all robots
    
    
    private Double currentFitness;          //fitness value up to a moment
    
    
    public RoleAllocEvaluationFunction(Arguments args) {
            super(args);	
            
            currentFitness = 0.0;
            
            
    }

    
    
    @Override
    public double getFitness() {
        
        return currentFitness;        
        
    }

    
    
    @Override
    public void update(Simulator simulator) {

        if ( outputs == null ) {
            
            outputs = new Double[ simulator.getRobots().size() ];
            numOfRobots = simulator.getRobots().size();
            totalSteps = simulator.getEnvironment().getSteps();
            
        }
        
        currentStep = simulator.getTime();
        
        maxOutput = 0.0;                    //reinitialize maximum output
        
        RoleActuator act;                   //actuator that stores the output
        
        int robotIndex = 0;
        for ( Robot r : simulator.getRobots() ) {
            act = (RoleActuator) r.getActuatorByType( RoleActuator.class );
                                                    //get output value
            outputs[robotIndex] = act.getValue();   //of robot with index i

            if ( act.getValue() > maxOutput ) {     //store the max output
               maxOutput = act.getValue();          //in a variable
            }
            robotIndex++;
        }
        
        
        Double diffsSum = 0.0;                  //sum of all differences between
                                                //maxoutput and all outputs
        
        
        for (Double output : outputs) {         //determine sum of
            diffsSum += (maxOutput - output);   //all differences
        }
        
        
        currentFitness += ( diffsSum / (totalSteps * (numOfRobots - 1) ) );
        
        
    }

    
    /**
     * Gets the average of all values
     * within an array of Double
     * @param array the array of Double
     * @return the average
     */
    private Double getAvg(Double[] array) {
        
        Double totalVal = 0.0;
        
        for (Double val : array) {      //sum all
            totalVal += val;            //values
        }
        
        return totalVal / array.length; //return average
    }
}