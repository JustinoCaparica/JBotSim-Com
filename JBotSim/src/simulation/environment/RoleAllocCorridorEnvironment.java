package simulation.environment;

import java.util.Random;
import mathutils.Vector2d;
import simulation.Simulator;
import simulation.physicalobjects.Nest;
import simulation.physicalobjects.Wall;
import simulation.robot.Robot;
import simulation.robot.actuators.TwoWheelActuator;
import simulation.util.Arguments;
import simulation.util.ArgumentsAnnotation;


/**
 * An Environment where robots must designate
 * one of them to get to the nest while
 * while all other keep way from the nest
 * @author gus
 */
public class RoleAllocCorridorEnvironment extends Environment {

  
    
    
    
    
    
    
    private static final int WALLED = 1;
    @ArgumentsAnnotation(name="wall", defaultValue="1")     //does the environment
    private boolean walled;                                 //have a wall?
    
    
    
    
    
    private Nest nest;                          //the nest 
    
    
    private Random random;

    
    
    private Wall wall;                          //environment wall, if exists
    
    
    private Simulator simulator;
    

    
    public RoleAllocCorridorEnvironment(Simulator simulator, Arguments arguments) {
        
            super(simulator, arguments);
            
            this.simulator      = simulator;
            
            
            
            walled              = arguments.getArgumentAsIntOrSetDefault("wall", WALLED) == 1;
            
            
    }
	
    
    
    
    @Override
    public void setup(Simulator simulator) {
            
        super.setup(simulator);

       
        this.random = simulator.getRandom();

        
        
       
        
       
        
        
        double arenaWidth       = width;
        double arenaHeight      = arenaWidth * (6.0/11.0);
        
        double corridorLength   = arenaWidth * (5.0/11.0);
        double corridorHeight   = arenaHeight / 3;
        
        double corridorStartX   = (arenaWidth/2.0) - corridorLength;
        
        double wallThick = 0.01;
        
        
        
        if ( walled ) {
            
            double leftWallX = -arenaWidth / 2;
            wall = new Wall( simulator, leftWallX, 0, wallThick, arenaHeight);
            super.addObject( wall );                        //left wall
            
            
            
            double topWallLength = arenaHeight;
            double topWallX = corridorStartX - ( (topWallLength) / 2.0 );
            wall = new Wall( simulator, topWallX, arenaHeight/2, topWallLength, wallThick );
            super.addObject( wall );                        //top wall
            

            wall = new Wall( simulator, topWallX, -arenaHeight/2, topWallLength, wallThick );
            super.addObject( wall );                        //bottom wall
            
            
            double sideWallsLength = (arenaHeight - corridorHeight) / 2.0;
            wall = new Wall( simulator, corridorStartX, corridorHeight/2 + sideWallsLength/2, wallThick, sideWallsLength );
            super.addObject( wall );                        //arena right top wall
            
            wall = new Wall( simulator, corridorStartX, -corridorHeight/2 - sideWallsLength/2, wallThick, sideWallsLength );
            super.addObject( wall );                        //arena right bottom wall
            
            
            
            
            //* corridor walls
            double rightWallX = arenaWidth/2.0;
            wall = new Wall( simulator, rightWallX, 0, wallThick, corridorHeight );
            super.addObject( wall );                        //corridor right wall
            
            
            wall = new Wall( simulator, corridorStartX + (corridorLength/2), corridorHeight/2, corridorLength, wallThick );
            super.addObject( wall );                        //corridor top wall
            
            
            wall = new Wall( simulator, corridorStartX + (corridorLength/2), -corridorHeight/2, corridorLength, wallThick );
            super.addObject( wall );                        //corridor bottom wall
            
            
        }
        
        
        nest = new Nest(simulator, null, corridorLength - 0.01, 0, 0.02);
        super.addObject(nest);

        
//        simulator.getRobots().get(0).setPosition(0.3, 0.00);
//        simulator.getRobots().get(1).setPosition(-0.4, 0.00);
        //simulator.getRobots().get(0).setOrientation( Math.PI );
       
//        simulator.getRobots().get(2).setPosition(-0.4, 0.05);
//        simulator.getRobots().get(3).setPosition(-0.4, -0.05);
        
        
//        for (Robot robot : simulator.getRobots()) {
//            ((TwoWheelActuator) robot.getActuatorByType(TwoWheelActuator.class)).setMaxSpeed(0.01);
//        }
        
        
        for (Robot robot : simulator.getRobots()) {
            robot.setPosition( (random.nextDouble() * 2 - 1) * width*0.2 - arenaHeight/2,
                               (random.nextDouble() * 2 - 1) * arenaHeight/2*0.8);
            robot.setOrientation( random.nextDouble() * Math.PI);
            //((TwoWheelActuator) robot.getActuatorByType(TwoWheelActuator.class)).setMaxSpeed(0.0);
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

    /**
     * Gets the nest on the
     * environment
     * @return the nest or
     * null if there is none
     */
    public Nest getNest() {
        return nest;
    }


    
    
    
    
    
    
    
    
    
    
    
    
    
}
