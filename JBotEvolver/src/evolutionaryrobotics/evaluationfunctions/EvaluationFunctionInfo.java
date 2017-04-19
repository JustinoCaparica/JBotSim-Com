/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolutionaryrobotics.evaluationfunctions;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Holds information about the evaluation of
 * a controller. It may be used to store extra
 * information about fitness, for instance, by
 * associating Strings to Double 
 * @author guest
 */
public class EvaluationFunctionInfo implements Serializable {
    
    private final Map<String, Double> storage;      //structure to store
                                                    //fitness info
                                                    //map keys are the parameters
                                                    //map values are the values
                                                    //associated with each parameter

    
    /**
     * Initializes a new empty EvaluationFunctionInfo
     */
    public EvaluationFunctionInfo() {
        this.storage = new HashMap<>();
    }

    
    /**
     * Associates a string to a given value.
     * If the string is already associated 
     * with a value, the previous value is
     * replaced by the new value
     * @param key the string
     * @param value the value
     */
    public void setFitnessInfoValue( String key, Double value ) {
        storage.put( key, value );
    }

    /**
     * Gets the value associated with a
     * given string
     * @param key the string
     * @return the value associated with
     * a string or null is there is none
     */
    public Double getFitnessInfoValue( String key ) {
        return storage.get(key);
    }

    /**
     * Gets all stored keys
     * @return A Set with
     * the stored keys
     */
    public Set<String> keySet() {
        return storage.keySet();
    }
    
    
    
    
    
    
    
    
    
}
