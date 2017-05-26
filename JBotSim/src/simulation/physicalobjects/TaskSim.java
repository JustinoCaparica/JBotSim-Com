/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.physicalobjects;

import java.awt.Color;
import simulation.Simulator;
import simulation.physicalobjects.collisionhandling.knotsandbolts.CircularShape;
import simulation.physicalobjects.collisionhandling.knotsandbolts.Shape;
import simulation.util.Arguments;

/**
 * A task for robots, or other entities, to do.
 * The task as a level that
 * represents how much of the task is 
 * currently completed
 * @author guest
 */
public abstract class TaskSim extends PhysicalObject{
    
    
    private Double currentLevel;                //the current level
    private Double minLevel;                    //minimum level the task can achieve
    private Double maxLevel;                    //maximum level the task can achieve
    private final Double initialLevel;          //the initial level
    
    
    /**
     * Initializes a new instance
     * @param simulator the simulator
     * where experiments are running
     * @param name the name of the task
     * @param x the x coordinate
     * @param y the y coordinate
     * @param shape the shape of the task
     * @param currentLevel the current, initial, level
     * @param minLevel the minimum level
     * @param maxLevel the maximum level
     */
    
    public TaskSim(Simulator simulator, String name,
                    Double x, Double y, Shape shape,
                    Double currentLevel, Double minLevel, Double maxLevel) {
        
        super(simulator, name, x, y, 0, 0, PhysicalObjectType.TASK);
        
        this.shape = shape;
        
        this.currentLevel       = currentLevel;
        this.initialLevel       = currentLevel;
        this.maxLevel           = maxLevel;
        this.minLevel           = minLevel;
        
        
    }

    /**
     * Gets the current level
     * of completeness
     * @return 
     */
    public Double getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Sets the current level
     * @param level the new value
     * for the current level
     */
    public void setCurrentLevel(Double level) {
        this.currentLevel = level;
    }

    
    
    
    /**
     * Gets the initial level
     * @return 
     */
    public double getInitialLevel() {
        return initialLevel;
    }


    /**
     * Gets the minimum level
     * @return 
     */
    public double getMinLevel() {
        return minLevel;
    }
    
    /**
     * Sets the minimum level
     * @param minLevel 
     */
    public void setMinLevel( double minLevel ) {
        this.minLevel = minLevel;
    }

    
    /**
     * Gets the maximum level
     * @return 
     */
    public double getMaxLevel() {
        return maxLevel;
    }

    /**
     * Sets the maximum level
     * @param maxLevel 
     */
    public void setMaxLevel( double maxLevel ) {
        this.maxLevel = maxLevel;
    }
    
    
    /**
     * Implementation of the consequences
     * of executing the task
     */
    public abstract void execute();
    
    
    
    
    
}
