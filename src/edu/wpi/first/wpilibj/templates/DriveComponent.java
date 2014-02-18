/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 *
 * @author 1SRJ
 */
public class GrabComponent implements RobotComponent {

    private JoystickButton grabButton; // button
    private JoystickButton shootButton;
    private JoystickButton passButton; //button 
    private Victor grabberVictor;
    private DigitalInput grabberLimitSwitch;
    public static final int NEUTRAL = 1;
    public static final int GRABBING = 2;
    public static final int PASSING = 3;
    private static int currentState = NEUTRAL;
    private Timer timer;
    private static final int TIMEOUT_DELAY = 1;

    public GrabComponent(JoystickButton jb1, JoystickButton jb2, JoystickButton jb3, Victor v, DigitalInput g) {
        grabButton = jb1;
        shootButton = jb2;
        passButton = jb3;
        grabberVictor = v;
        grabberLimitSwitch = g;
        currentState = NEUTRAL;
        timer = new Timer();

    }

    public void autonomousInit() {
        System.out.println("Grab Component initialized for autonomous");

    }

    public void autonomousPeriodic() {
        grabberVictor.set(-1);
    }

    public void teleopInit() {
        System.out.println("Grab Component initialized for teleop");
        currentState = NEUTRAL;

    }

    public void teleopPeriodic() {

//        boolean isGrabPressed = grabButton.get();
//        boolean isReleasePressed = passButton.get();
//        if(isGrabPressed){
//            vMotor.set(-1.0);
//        }else if(isReleasePressed){
//            vMotor.set(1.0);
//        }else{
//            vMotor.set(0.0);
//        } 
        boolean isGrabPressed = grabButton.get();
        boolean isReleasePressed = passButton.get();
        boolean isLaunching = (RobotRunner.getLaunchComponent().getState() == LaunchComponent.LAUNCHING);
        boolean ballHeld = !grabberLimitSwitch.get();

        //System.out.println("State: "+ currentState + " Ball Held: "+ballHeld);
        switch (currentState) {
            case NEUTRAL:
                grabberVictor.set(0.0);
                if (isGrabPressed) {
                    currentState = GRABBING;
                }
                if (isReleasePressed || isLaunching) {
                    timer.reset();
                    timer.start();
                    currentState = PASSING;
                }
                break;
            case GRABBING:
                if (!ballHeld) {
                    grabberVictor.set(-1.0);
                } else {
                    grabberVictor.set(0.0);
                }
                if (!isGrabPressed) {
                    currentState = NEUTRAL;
                }
                if (isReleasePressed || isLaunching) {
                    timer.reset();
                    timer.start();
                    currentState = PASSING;
                }
                break;
            case PASSING:
                //timer.start();
                if (timer.get() > TIMEOUT_DELAY) {
                    grabberVictor.set(0.0);
                    timer.stop();
                    currentState = NEUTRAL;
                    break;
                }
                grabberVictor.set(1.0);
                break;
        }
    }

    public void disabledInit() {
    }

    public void disabledPeriodic() {
    }
}
