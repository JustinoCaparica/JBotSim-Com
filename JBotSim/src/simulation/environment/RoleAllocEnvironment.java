package simulation.environment;

import java.util.Random;
import mathutils.Vector2d;
import simulation.Simulator;
import simulation.physicalobjects.Wall;
import simulation.robot.Robot;
import simulation.util.Arguments;
import simulation.util.ArgumentsAnnotation;


/**
 * An Environment where robots must designate
 * one of them to output a high value
 * while all other robots output a low value
 * @author gus
 */
public class RoleAllocEnvironment extends Environment {

  
    
    
    
    
    
    
    private static final int WALLED = 1;
    @ArgumentsAnnotation(name="wall", defaultValue="1")     //does the environment
    private boolean walled;                                 //have a wall?
    
    
    
    
    
    

    
    
    
    private Random random;

    
    
    private Wall wall;                          //environment wall, if exists
    
    
    private Simulator simulator;
    

    
    public RoleAllocEnvironment(Simulator simulator, Arguments arguments) {
        
            super(simulator, arguments);
            
            this.simulator      = simulator;
            
            
            
            walled              = arguments.getArgumentAsIntOrSetDefault("wall", WALLED) == 1;
            
            
    }
	
    
    
    
    @Override
    public void setup(Simulator simulator) {
            
        super.setup(simulator);

       
        this.random = simulator.getRandom();

        for (Robot robot : simulator.getRobots()) {
            robot.setPosition( (random.nextDouble() * 2 - 1) * width/2.5,
                               (random.nextDouble() * 2 - 1) * height/2.5);
            robot.setOrientation( random.nextDouble() * Math.PI);
        }
        
       
        
        
       
        
        
        double wallSize = width/2;
        if ( walled ) {
            wall = new Wall( simulator, -wallSize, 0, 0.10, 2*wallSize);
            super.addObject( wall );
            
            wall = new Wall( simulator, wallSize, 0, 0.10, 2*wallSize);
            super.addObject( wall );
            
            wall = new Wall( simulator, 0, -wallSize, 2*wallSize, 0.10);
            super.addObject( wall );
            
            wall = new Wall( simulator, 0, wallSize, 2*wallSize, 0.10);
            super.addObject( wall );
            
            
            
        }
        

            
            
    }

    
    private Vector2d newRandomPosition() {
        
        
        double radius = random.nextDouble() * width;
        double angle = random.nextDouble()*2*Math.PI;
        
        return new Vector2d( radius * Math.cos(angle), radius * Math.sin(angle) );
    
    }
	
    
    
    
    
    
    
    
    @Override
    public void update(double time) {

    }

    
    
    
    
    
    
    
    
    
    
    
    
    
}
