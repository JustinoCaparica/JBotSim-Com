/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation.robot.messenger.message.parser;

import simulation.robot.messenger.message.Message;
import simulation.robot.Robot;

/**
 * Parses messages and decides how
 * to deal with the messages
 * @author gus
 */
public interface MessageParser {
    
    
    /**
     * Parses a message and
     * changes stuff in the
     * receiver robot //TODO document this method
     * @param receiver the robot
     * that received the message
     * @param emitter the robot
     * that emitted the message
     * @param msg the message
     */
    public void parse( Robot receiver, Robot emitter, Message msg );
    
    
    
}
