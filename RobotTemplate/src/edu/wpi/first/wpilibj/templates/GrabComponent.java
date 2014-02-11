/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 *
 * @author 1SRJ
 */
public class GrabComponent implements RobotComponent {

//private CANJaguar grabMotor;
//private Joystick armStick;
    private Joystick jStick;
    private int releaseButton = 2;
    private int grabButton = 3;
    private Victor vMotor;
    
    public GrabComponent(Joystick j, Victor v){
        jStick = j;
        vMotor = v;
    }

    public void Initialize() {
        vMotor.set(jStick.getY());
    }

    public void teleopInit() {

    }

    public void teleopPeriodic() {
        boolean grab = jStick.getRawButton(grabButton);
        boolean release = jStick.getRawButton(releaseButton);
        if(grab){
            vMotor.set(1.0);
        }else if(release){
            vMotor.set(-1.0);
        }else{
            vMotor.set(0.0);
        } 
    }

    public void autonomousInit() {
        
    }

    public void autonomousPeriodic() {
        vMotor.set(jStick.getY());
    }

    public void disabledInit() {
    }

    public void disabledPeriodic() {
    }
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initialize() {
        throw new java.lang.UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
