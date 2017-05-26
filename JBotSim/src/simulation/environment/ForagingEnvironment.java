/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.environment;

import simulation.physicalobjects.TaskSim;

/**
 * An interface to declare methods that
 * foraging environments should have
 * @author guest
 */
public interface ForagingEnvironment {
    
    /**
     * Gets the initial number of preys
     * @return an integer representing
     * the initial number of preys
     */
    public int getInitialPreyCount();
    
    
    /**
     * Gets the number of preys 
     * captured
     * @return an integer representing
     * the number of preys captured
     */
    public int getCapturedPreyCount();
    
    
    
    /**
     * Gets the TaskSim 
     * @return the TaskSim 
     * or null if there is none
     */
    public TaskSim getTaskSim();
    
}
