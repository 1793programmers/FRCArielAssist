/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 *
 * @author 1SRJ
 */
public class GrabComponent implements RobotComponent {

    private Joystick jStick;
    private JoystickButton grabButton;
    private JoystickButton releaseButton;
    private Victor vMotor;
    
    public GrabComponent(Joystick j, JoystickButton jb1, JoystickButton jb2, Victor v){
        jStick = j;
        grabButton = jb1;
        releaseButton = jb2;
        vMotor = v;
    }

    public void initialize() {
        vMotor.set(jStick.getY());
    }
    
    public void autonomousInit() {
        
    }
    
    public void autonomousPeriodic() {
        vMotor.set(-1);
    }

    public void teleopInit() {

    }

    public void teleopPeriodic() {
        boolean isGrabPressed = grabButton.get();
        boolean isReleasePressed = releaseButton.get();
        if(isGrabPressed){
            vMotor.set(1.0);
        }else if(isReleasePressed){
            vMotor.set(-1.0);
        }else{
            vMotor.set(0.0);
        } 
    }
    
    public void disabledInit() {
    }

    public void disabledPeriodic() {
    }
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

}
