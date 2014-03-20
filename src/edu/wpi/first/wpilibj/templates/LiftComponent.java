/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;

/**
 *
 * @author MEOW MEOW
 */
public class LiftComponent implements RobotComponent {

    /**
     * @return the currentState
     */
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private Joystick armStick;
    //private JoystickButton resetButton;
    private Victor armVictor;
    static DigitalInput fLimitSwitch;
    static DigitalInput bLimitSwitch;
    private AnalogChannel ultrasonic;
    public static final int NEUTRAL = 1;
    public static final int DEPLOYING = 2;
    public static final int RETRACTING = 3;
    public static final int RESETTING = 4;
    //AUTONOMOUS STATES
    public static final int READY = 5;
    public static final int LAUNCH = 6;
    private static int currentState = NEUTRAL;
    static boolean isBLimitOpen;

    public LiftComponent(Joystick j, Victor v, DigitalInput s1, DigitalInput s2) {
        armStick = j;
        //resetButton = b;
        armVictor = v;
        fLimitSwitch = s1;
        bLimitSwitch = s2;
        currentState = NEUTRAL;
        isBLimitOpen = bLimitSwitch.get();
        ultrasonic = RobotRunner.getUltrasonicSensor();
    }

    public void autonomousInit() {
        armVictor.set(-1.0); //move to shooting position
        //  boolean isBLimitOpen = bLimitSwitch.get();
        if (!isBLimitOpen) { //when limitswitch is closed the arm will stop
            armVictor.set(0);
        }
        System.out.println("Lift Component initialized for autonomous");
        currentState = DEPLOYING;
    }

    public void autonomousPeriodic() {
        boolean isForwardLimitSwitchShut = !fLimitSwitch.get();
        boolean isBackwardLimitSwitchShut = !bLimitSwitch.get();
        printState(isForwardLimitSwitchShut, isBackwardLimitSwitchShut);
        armVictor.set(0.0);
        double armSignal = 1.0;
        switch (getCurrentState()) {
            case DEPLOYING:
                if (isBackwardLimitSwitchShut) {
                    armVictor.set(armSignal);
                }
                if (!isBackwardLimitSwitchShut) {
                    currentState = RETRACTING;
                }
                break;
            case RETRACTING:
                if (!isBackwardLimitSwitchShut) {
                    armVictor.set(-armSignal);
                }
                if (isBackwardLimitSwitchShut) {
                    armVictor.set(0);
                    currentState = READY;
                }
                break;
            case READY:
                if (DriveComponent.getCurrentState() == DriveComponent.LAUNCH) {
                    currentState = LAUNCH;
                }
                break;

            case LAUNCH:
                //Nothing, placeholder to check at this time
                break;
        }
    }

    public void teleopInit() {
        //System.out.println("Lift Component initialized for teleop");
        currentState = NEUTRAL;
    }

    public void teleopPeriodic() {
        boolean isForwardLimitSwitchShut = !fLimitSwitch.get();
        boolean isBackwardLimitSwitchShut = !bLimitSwitch.get();
       // printState(isForwardLimitSwitchShut, isBackwardLimitSwitchShut);
        armVictor.set(0.0);
        double armSignal = armStick.getThrottle();

        if (armSignal < -0.1) { // Retracting Backward
            //System.out.println("Moving Arm Backwards");
            if (!isBackwardLimitSwitchShut) {
                armVictor.set(-armSignal);
            }

        } else if (armSignal > 0.1) { // Retracting backward
            //System.out.println("Moving Arm Forward");
            if (!isForwardLimitSwitchShut) {
                armVictor.set(-armSignal);
            }
        }
    }

    public void disabledInit() {
    }

    public void disabledPeriodic() {
    }

    private void printState(boolean a, boolean b) {
       // System.out.println("Forward Limit =" + a + "Back Limit =" + b);
    }

    public static int getCurrentState() {
        return currentState;
    }
}
