package simulation.physicalobjects;

import java.io.Serializable;

public enum PhysicalObjectType implements Serializable {
    NA, 
    ROBOT, 
    PREY, 
    NEST, 
    WALL, 
    LIGHTPOLE, 
    PHEROMONE, 
    FOOD, 
    WATER, 
    RECT_HOLE, 
    WALLBUTTON, 
    LINE, 
    MARKER,
    TASK                //as taks that can be performed by robots or other stuff
}
