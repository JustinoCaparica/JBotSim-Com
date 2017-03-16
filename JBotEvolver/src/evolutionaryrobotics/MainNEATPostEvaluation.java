/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolutionaryrobotics;

/**
 * A class to execute post evaluation for
 * NEAT networks
 * @author guest
 */
public class MainNEATPostEvaluation {
    
    private static NEATPostEvaluation postEvaluator;
    private static String[] arguments;

    public MainNEATPostEvaluation() {
        
        
    }
    
    
    
    
    public static void main( String[] args ){
        
        String experimentDir = "roleAlloc-corridor-shortRange-stopWhenCollide";
        
        arguments = new String[10];
        
        arguments[0] = "dir=../EvolutionAutomator/role-alloc-corridor/wallAndRobotSensor/" + experimentDir + "/" + experimentDir + "/1";
        arguments[1] = "samples=10";
        arguments[2] = "fitnesssamples=1";
        arguments[3] = "targetfitness=0.0";
        arguments[4] = "singleevaluation=1";
        arguments[5] = "localevaluation=1";
        arguments[6] = "steps=2000";
        arguments[7] = "showoutput=1";
        arguments[8] = "sampleincrement=1";
        arguments[9] = "saveoutput=1";
        
        
        postEvaluator = new NEATPostEvaluation(arguments);
        
        
        
        
        postEvaluator.runPostEval();
        
        
    }
    
    
    
    
    
    
}
