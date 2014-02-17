/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

/**
 *
 * @author MEOW MEOW
 */
public interface RobotComponent {

    public void autonomousInit();
    public void autonomousPeriodic();
    public void disabledInit();
    public void disabledPeriodic();
    public void teleopInit();
    public void teleopPeriodic();

}
    
