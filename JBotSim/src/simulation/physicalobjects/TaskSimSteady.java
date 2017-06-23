/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.physicalobjects;

import simulation.Simulator;
import simulation.physicalobjects.collisionhandling.knotsandbolts.Shape;

/**
 * A TaskSim where the level
 * only changes when it is executed by calling
 * the execute() method
 * @author guest
 */
public class TaskSimSteady extends TaskSim {

    
    private Double change;              //the change caused on the task
                                        //level when the task is executed
                                        //by the execute() method
    
    
    /**
     * Initializes a new object
     * @param simulator the simulator
     * @param name name of the task
     * @param x x coordinate of the task
     * @param y y coordinate
     * @param shape the shape of the task
     * @param currentLevel the current level
     * of the task
     * @param minLevel the minimum level the
     * task can achieve
     * @param maxLevel the maximum level the
     * task can achieve
     */
    public TaskSimSteady(Simulator simulator, String name, 
                            Double x, Double y, 
                            Shape shape, 
                            Double currentLevel, Double minLevel, Double maxLevel,
                            Double change) {
        
        super(simulator, name, x, y, shape, currentLevel, minLevel, maxLevel);
        this.change = change;
    
    }

    @Override
    public void execute() {
        
        setCurrentLevel( getCurrentLevel() + change );  // current level increases/decreases
                                                        // by 'change' units
        
        if ( getCurrentLevel() > getMaxLevel() ) {      // assure current level in
            setCurrentLevel( getMaxLevel() );           // [minLevel, MaxLevel] range
        }
        else if( getCurrentLevel() < getMinLevel() ) {
            setCurrentLevel( getMinLevel() );
        }
        
    }
    
}
