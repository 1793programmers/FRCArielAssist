/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalInput;
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
    private DigitalInput grabberLimitSwitch; 
    public static final int NEUTRAL = 1; 
    public static final int GRABBING = 2; 
    public static final int RELEASING = 3; 
    private static int currentState = NEUTRAL;        
    
    public GrabComponent(Joystick j, JoystickButton jb1, JoystickButton jb2, Victor v, DigitalInput g){
        jStick = j;
        grabButton = jb1;
        releaseButton = jb2;
        vMotor = v;
        grabberLimitSwitch = g; 
        currentState = NEUTRAL;
        
    }

    public void autonomousInit() {
        System.out.println("Grab Component initialized for autonomous"); 
        
    }
    
    public void autonomousPeriodic() {
        vMotor.set(-1);
    }

    public void teleopInit() {
        System.out.println("Grab Component initialized for teleop");
        currentState = NEUTRAL;

    }

    public void teleopPeriodic() {
        
//        boolean isGrabPressed = grabButton.get();
//        boolean isReleasePressed = releaseButton.get();
//        if(isGrabPressed){
//            vMotor.set(-1.0);
//        }else if(isReleasePressed){
//            vMotor.set(1.0);
//        }else{
//            vMotor.set(0.0);
//        } 
        boolean isGrabPressed = grabButton.get();
        boolean isReleasePressed = releaseButton.get();
        boolean isLaunching = RobotRunner.getLaunchComponent().isLaunching();
        boolean ballHeld = !grabberLimitSwitch.get();
        
        System.out.println("State: "+ currentState + " Ball Held: "+ballHeld);
        switch(currentState) {
            case NEUTRAL: 
                vMotor.set(0.0);
                if(isGrabPressed){
                    currentState = GRABBING;
                }
                if(isReleasePressed || isLaunching) {
                    currentState = RELEASING; 
                }
                break;
            case GRABBING:
                if(!ballHeld) {
                    vMotor.set(-1.0); 
                } else {
                    vMotor.set(0.0);
                }
                if(!isGrabPressed) {
                    currentState = NEUTRAL; 
                }
                if(isReleasePressed || isLaunching) {
                    currentState = RELEASING; 
                }
                break;
            case RELEASING:
                vMotor.set(1.0);
                if(!isReleasePressed && !isLaunching) {
                    currentState = NEUTRAL; 
                }
                break;
        }
    }
    
    public void disabledInit() {
    }

    public void disabledPeriodic() {
    }

}
