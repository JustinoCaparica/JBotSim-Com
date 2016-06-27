package evolutionaryrobotics.evaluationfunctions;

import mathutils.Vector2d;
import simulation.Simulator;
import simulation.environment.CooperativeForagingEnvironment;
import simulation.robot.Robot;
import simulation.robot.sensors.RecruitSensor;
import simulation.robot.sensors.RecruiterSensor;
import simulation.util.Arguments;


/**
 * Evaluation function that rewards a team of robots where
 * one is the recruit and the other is the recruiter
 * @author gus
 */
public class RecruitmentEvaluationFunction extends EvaluationFunction{
	
    
    protected Vector2d   nestPosition = new Vector2d(0, 0);
        
        

    private int fit;
    
    private Simulator simulator;

    
    
    public RecruitmentEvaluationFunction(Arguments args) {
            super(args);	
            
            fit = 0;
    }

    
    
    @Override
    public double getFitness() {

            return  fit;
    }

    
    
    @Override
    public void update( Simulator simulator ) {

        if( simulator == null )
            this.simulator = simulator;

        
        
        
        
        Robot robot0 = simulator.getRobots().get(0);
        Robot robot1 = simulator.getRobots().get(1);
        
        
        RecruiterSensor recruiterSensor0;
        RecruitSensor recruitSensor0;
        recruiterSensor0 = (RecruiterSensor) robot0.getSensorByType( RecruiterSensor.class );
        recruitSensor0 = (RecruitSensor) robot0.getSensorByType( RecruitSensor.class );

        
        RecruiterSensor recruiterSensor1;
        RecruitSensor recruitSensor1;
        recruiterSensor1 = (RecruiterSensor) robot1.getSensorByType(RecruiterSensor.class);
        recruitSensor1 = (RecruitSensor) robot1.getSensorByType(RecruitSensor.class);
        
        
        
        if( (recruiterSensor0.getRecruiter() != null && recruitSensor0.getRecruit() == null) &&
            (recruiterSensor1.getRecruiter() == null && recruitSensor1.getRecruit() != null) ){
            fit++;
        }
            
        if( (recruiterSensor1.getRecruiter() != null && recruitSensor1.getRecruit() == null) &&
            (recruiterSensor0.getRecruiter() == null && recruitSensor0.getRecruit() != null) ){
            fit++;
        }  
            
        

        

    }
}