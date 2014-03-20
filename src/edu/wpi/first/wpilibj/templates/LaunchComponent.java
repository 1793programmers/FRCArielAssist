/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 *
 * @author milo
 */
public class LaunchComponent implements RobotComponent {

    // control inputs
    private Joystick armJoystick;
    private JoystickButton automaticButton;
    private JoystickButton latchButton;
    private JoystickButton cockButton;
    private JoystickButton freezeButton;
    private JoystickButton thawButton;
    private JoystickButton launchButton;
    // limit switches and sensor inputs
    private DigitalInput latchLimitSwitch; // channel 3 
    private DigitalInput cockLimitSwitch; //channel 4
    private AnalogChannel ultrasonic = RobotRunner.getUltrasonicSensor();
    // outputs and actuators
    private Servo latchServo;
    private Victor launcherVictor;
    private Timer timer;
    // additional variables
    private boolean isFrozen = false;
    private boolean isLatched = false;
    private boolean isCocked = false;
    private boolean isAutomatic = false;
    private static double moveToLatch = -1.0;
    private static double moveToCock = 1.0;
    private static int currentState;
    // class constants
    private static final double UNLATCH_POSITION = 0.05; //-0.225531914893617;
    private static final double LATCH_POSITION = 1.0;
    private static final double TIMEOUT_DELAY = 1.0;
    public static final int NEUTRAL = 1;
    public static final int MANUAL_LATCHING = 2;
    public static final int MANUAL_COCKING = 3;
    public static final int AUTO_LATCHING = 4;
    public static final int AUTO_COCKING = 5;
    public static final int LAUNCHING = 6;
    public static final int READY = 7;

    public LaunchComponent(JoystickButton auto,
            JoystickButton latch,
            JoystickButton cock,
            JoystickButton stop,
            JoystickButton thaw,
            JoystickButton launch,
            Victor victor,
            DigitalInput fwdLimit,
            DigitalInput backLimit,
            Servo servo) {
        automaticButton = auto;
        latchButton = latch;
        cockButton = cock;
        freezeButton = stop;
        thawButton = thaw;
        launchButton = launch;
        launcherVictor = victor;
        latchLimitSwitch = fwdLimit;
        cockLimitSwitch = backLimit;
        latchServo = servo;

        armJoystick = new Joystick(2);

        timer = new Timer();
        currentState = NEUTRAL;
    }

    public void autonomousInit() {
        //System.out.println("Launch Component initialized for autonomous");
        currentState = AUTO_COCKING;
    }

    public void autonomousPeriodic() {
        isCocked = !cockLimitSwitch.get();
        switch (currentState) {
            case AUTO_COCKING:
                launcherVictor.set(moveToCock);
                latchServo.set(LATCH_POSITION);
                if (isCocked) { // cocking complete
                    launcherVictor.set(0.0);
                    currentState = READY;
                    break;
                }
                break;
            case READY:
                if (DriveComponent.getCurrentState() == DriveComponent.LAUNCH) {
                    currentState = LAUNCHING;
                    timer.start();
                    break;
                }
                break;

            case LAUNCHING:
                latchServo.set(UNLATCH_POSITION);
                launcherVictor.set(0.0);
                isLatched = false;
                isCocked = false;
                if (timer.get() > TIMEOUT_DELAY) {
                    currentState = NEUTRAL;
                }
                break;
        }

    }

    public void teleopInit() {
     //   System.out.println("Launch Component initialized for teleop");
        currentState = NEUTRAL;
        isFrozen = false;
        latchServo.set(LATCH_POSITION);
    }

    public void teleopPeriodic() {
        //printState();
    //    System.out.println(latchServo.get());
        if (freezeButton.get()) {
     //       System.out.println("Freeze button pressed");
            isFrozen = true;
        } else if (thawButton.get()) {
    //        System.out.println("Thaw button pressed");
            isFrozen = false;
        }
        if (isFrozen) {
   //         System.out.println("Launcher Frozen");
            launcherVictor.set(0.0);
            latchServo.set(LATCH_POSITION);
        } else {
            // collect sensor information, update latched/cocked
            boolean isLatchLimitSwitchClosed = !latchLimitSwitch.get();
            if (isLatchLimitSwitchClosed) {
    //            System.out.println("Launcher now Latched");
                isLatched = true;
            }
            boolean isCockLimitSwitchClosed = !cockLimitSwitch.get();
            if (isCockLimitSwitchClosed) {
    //            System.out.println("Launcher now Cocked");
                isCocked = true;
            } else {
                isCocked = false;
            }
            //double latchSignal = launchStick.getY();
            switch (currentState) {
                case NEUTRAL:
                    launcherVictor.set(0.0);
                    latchServo.set(LATCH_POSITION);
                    // allow to set automatic mode
                    if (automaticButton.get()) {
                        isAutomatic = true;
                        currentState = AUTO_LATCHING;
                        break;
                    }
                    if (launchButton.get()) {
                        timer.reset();
                        timer.start();
                        currentState = LAUNCHING;
                        break; // no need to continue
                    }
                    if (latchButton.get()) {
                        isAutomatic = false;
       //                 System.out.println("LatchButtonPressed");
                        if (!isLatchLimitSwitchClosed) {
          //                  System.out.println("Moving to Manual Latched State");
                            currentState = MANUAL_LATCHING;
                            break;
                        }
                        break;
                    }
                    if (cockButton.get()) {
        //                System.out.println("CockButtonPressed");
                        isAutomatic = false;
                        if (!isCockLimitSwitchClosed) {
         //                   System.out.println("Moving to Manual Cock State");
                            currentState = MANUAL_COCKING;
                            break;
                        }
                        break;
                    }
                    if (isAutomatic) {
                        if (!isLatched) {
                            currentState = AUTO_LATCHING;
                        } else if (!isCocked) {
                            currentState = AUTO_COCKING;
                        }
                    }
                case MANUAL_LATCHING:
                    launcherVictor.set(moveToLatch);
                    latchServo.set(LATCH_POSITION);
                    isAutomatic = false;
                    if (isLatchLimitSwitchClosed || !latchButton.get()) {
                        launcherVictor.set(0.0);
                        currentState = NEUTRAL;
                    }
                    break;

                case MANUAL_COCKING:
                    launcherVictor.set(moveToCock);
                    latchServo.set(LATCH_POSITION);
                    isAutomatic = false;
                    if (isCockLimitSwitchClosed || !cockButton.get()) {
                        launcherVictor.set(0.0);
                        currentState = NEUTRAL;
                    }
                    break;

                case AUTO_LATCHING:
                    launcherVictor.set(moveToLatch);
                    latchServo.set(LATCH_POSITION);
                    if (latchButton.get() || cockButton.get()) {  // go manual
                        launcherVictor.set(0.0);
                        currentState = NEUTRAL;
                        break;
                    }
                    if (isLatched) { // latching complete
                        launcherVictor.set(0.0);
                        currentState = AUTO_COCKING;
                        break;
                    }
                    break;

                case AUTO_COCKING:
                    launcherVictor.set(moveToCock);
                    latchServo.set(LATCH_POSITION);
                    if (latchButton.get() || cockButton.get()) { // go manual
                        launcherVictor.set(0.0);
                        currentState = NEUTRAL;
                        break;
                    }
                    if (isCocked) { // cocking complete
                        launcherVictor.set(0.0);
                        currentState = NEUTRAL;
                        break;
                    }
                    break;

                case LAUNCHING:
                    latchServo.set(UNLATCH_POSITION);
                    launcherVictor.set(0.0);
                    isLatched = false;
                    isCocked = false;
                    if (timer.get() > TIMEOUT_DELAY) {
                        if (isAutomatic) {
                            currentState = AUTO_LATCHING;
                        } else {
                            currentState = NEUTRAL;
                        }
                    }
                    break;
            }
        }
    }

    public void disabledPeriodic() {
    }

    public void disabledInit() {
    }

    public int getState() {
        return currentState;
    }

    private void printState() {
   //     System.out.println("Launcher state: " + currentState + " Latched: " + isLatched + " Cocked: " + isCocked + " Frozen: " + isFrozen + " Automatic: " + isAutomatic + "Servo =" + latchServo.get());

    }

    public static int getCurrentState() {
        return currentState;
    }
}
