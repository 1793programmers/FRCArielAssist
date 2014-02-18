/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 *
 * @author MEOW MEOW
 */
public class LiftComponent implements RobotComponent {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private Joystick armStick;
    private JoystickButton resetButton;
    private Victor armVictor;
    DigitalInput fLimitSwitch;
    DigitalInput bLimitSwitch;
    public static final int NEUTRAL = 1;
    public static final int DEPLOYING = 2;
    public static final int RETRACTING = 3;
    public static final int RESETTING = 4;
    private static int currentState = NEUTRAL;

    public LiftComponent(Joystick j, Victor v, DigitalInput s1, DigitalInput s2, JoystickButton b) {
        armStick = j;
        resetButton = b;
        armVictor = v;
        fLimitSwitch = s1;
        bLimitSwitch = s2;
        currentState = NEUTRAL;
    }

    public void autonomousInit() {
        armVictor.set(-1.0); //move to shooting position
        boolean isBLimitOpen = bLimitSwitch.get();
        if (!isBLimitOpen) { //when limitswitch is closed the arm will stop
            armVictor.set(0);
        }
        System.out.println("Lift Component initialized for autonomous");
    }

    public void autonomousPeriodic() {
    }

    public void teleopInit() {
        System.out.println("Lift Component initialized for teleop");
        currentState = NEUTRAL;
    }

    public void teleopPeriodic() {
        boolean isForwardLimitSwitchShut = !fLimitSwitch.get();
        boolean isBackwardLimitSwitchShut = !bLimitSwitch.get();
        printState(isForwardLimitSwitchShut, isBackwardLimitSwitchShut);
        armVictor.set(0.0);
        double armSignal = armStick.getThrottle();
        
        if (armSignal < -0.1) { // Retracting Backward
            System.out.println("Moving Arm Backwards");
            if (!isBackwardLimitSwitchShut) {
                armVictor.set(-armSignal);
            }

        } else if (armSignal >0.1) { // Retracting backward
            System.out.println("Moving Arm Forward");
            if (!isForwardLimitSwitchShut) {
                armVictor.set(-armSignal);
            }
        }
    }

    public void disabledInit() {
    }

    public void disabledPeriodic() {
    }
    private void printState(boolean a, boolean b){
        System.out.println("Forward Limit ="+a+"Back Limit ="+b);
    }
}
