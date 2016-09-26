package simulation.robot.actuators;

import java.util.Random;

import simulation.Simulator;
import simulation.robot.DifferentialDriveRobot;
import simulation.robot.Robot;
import simulation.util.Arguments;
import simulation.util.ArgumentsAnnotation;

public class TwoWheelActuator extends Actuator {

    public static final float NOISESTDEV = 0.05f;

    private double totalWheelSpeed;                 //total wheel speed spent
                                                    //by the actuator to move

    private Simulator simulator;                    //the simulator
                                                    //running the experiment

    private double tickDuration;                    //duration of a simulation
                                                    //tick, in seconds
    
    protected double leftSpeed = 0;
    protected double rightSpeed = 0;
    protected Random random;
    @ArgumentsAnnotation(name = "maxspeed", defaultValue = "0.1")
    protected double maxSpeed;

    public TwoWheelActuator( Simulator simulator, int id, Arguments arguments ) {
        super(simulator, id, arguments);
        this.random = simulator.getRandom();
        this.maxSpeed = arguments.getArgumentAsDoubleOrSetDefault("maxspeed", 0.1);

        totalWheelSpeed = 0.0;
        this.simulator = simulator;
        
        this.tickDuration = simulator.getTimeDelta();
    }

    public void setLeftWheelSpeed(double value) {
        leftSpeed = (value - 0.5) * maxSpeed * 2.0;
    }

    public void setRightWheelSpeed(double value) {
        rightSpeed = (value - 0.5) * maxSpeed * 2.0;
    }

    @Override
    public void apply(Robot robot, double timeDelta) {
        
        //sum the wheel speed spent since last call to this method
        //to the total wheel speed
        totalWheelSpeed += tickDuration * ( Math.abs(leftSpeed) + Math.abs(rightSpeed) );
        
        
        leftSpeed *= (1 + random.nextGaussian() * NOISESTDEV);
        rightSpeed *= (1 + random.nextGaussian() * NOISESTDEV);

        if ( leftSpeed < -maxSpeed ) {
            leftSpeed = -maxSpeed;
        } else if ( leftSpeed > maxSpeed ) {
            leftSpeed = maxSpeed;
        }

        if ( rightSpeed < -maxSpeed ) {
            rightSpeed = -maxSpeed;
        } else if ( rightSpeed > maxSpeed ) {
            rightSpeed = maxSpeed;
        }
        ((DifferentialDriveRobot) robot).setWheelSpeed( leftSpeed, rightSpeed );
        

    }

    /**
     * Gets the total amount of
     * wheel speed spent so far
     * @return 
     */
    public double getSpentEnergy() {
        return totalWheelSpeed;
    }

    @Override
    public String toString() {
        return "TwoWheelActuator [leftSpeed=" + leftSpeed + ", rightSpeed="
                + rightSpeed + "]";
    }

    /**
     * Gets the maximum speed.
     * @return 
     */
    public double getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * Sets the maximum speed. 
     * @param maxSpeed the maximum
     * speed for the wheels
     */
    public void setMaxSpeed( double maxSpeed ) {
        this.maxSpeed = maxSpeed;
    }

    
    
    
    
    
    public double[] getSpeed() {
        double[] velocities = {leftSpeed, rightSpeed};
        return velocities;
    }

    public double[] getSpeedPrecentage() {
        double leftPercentage = leftSpeed / maxSpeed;
        double rightPercentage = rightSpeed / maxSpeed;

        return new double[]{leftPercentage, rightPercentage};
    }

}
