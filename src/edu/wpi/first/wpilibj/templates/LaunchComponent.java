/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 * @author milo
 */
public class LaunchComponent implements RobotComponent {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    private Victor launchVictor;
    private Joystick launchStick;
    private JoystickButton autoLaunchButton;
    private JoystickButton manualLaunchButton;
    private DigitalInput forwardLaunchSwitch; // channel 3 
    private DigitalInput backwardLaunchSwitch; //channel 4
    private Servo launchServo;
    private boolean isLaunching = false;
    private Timer timer;
    private static final double timeout = 1.0;
    private static final double UNLATCH_POSITION = 0.0;
    private static final double LATCH_POSITION = 0.6425531914893617;
    public static final int NEUTRAL = 1;
    public static final int MANUAL_LAUNCHING = 2;
    public static final int AUTO_LAUNCHING = 3;
    public static final int LAUNCH_FORWARD = 4;
    public static final int LAUNCH_REVERSE = 5;
    private static int currentState = NEUTRAL;

    public LaunchComponent(Joystick j, JoystickButton jb1, JoystickButton jb2, Victor v, DigitalInput dI1, DigitalInput dI2, Servo s1) {
        launchStick = j;
        launchButton = jb1;
        manualLaunchButton = jb2;
        launchVictor = v;
        forwardLaunchSwitch = dI1;
        backwardLaunchSwitch = dI2;
        launchServo = s1;
        timer = new Timer();
        currentState = NEUTRAL;
    }

    public void autonomousPeriodic() {
    }

    public void teleopInit() {
        launchServo.set(UNLATCH_POSITION);
        System.out.println("Launch Component initialized for teleop");
        currentState = NEUTRAL;
    }

    public void teleopPeriodic() {

        /*if (launchButton.get() == true){
         lVictor.set(1);
         Timer.delay(1);
         // lVictor.set(-1);
         }
         else if (retractButton.get() == true){
         lVictor.set(-1);
         Timer.delay(1);
         }
         else {
         lVictor.set(0);
         }*/
        /*
         System.out.println(launchStick.getThrottle() + "     " + launchServo.get());
         
         System.out.println("Time =" + timer.get());
         if (launchSwitch.get()) {
         if (launchButton.get() == true) { //would like to launch
         if (isLaunching == false) { // not currently in launch mode
         timer.reset();
         timer.start();
         isLaunching = true;
         launchServo.set(UNLATCH_POSITION);
         lVictor.set(1.0);
         } // if already in launch mode, no need to change anything
         } else if (retractButton.get() == false) {
         isLaunching = false;
         launchServo.set(LATCH_POSITION);
         } else { //don't want in launch
         if (timer.get() > 1) { //if beyond time limit, stop launching
         lVictor.set(-1.0);
         launchServo.set(LATCH_POSITION);
         isLaunching = false;
         timer.reset();
         timer.stop();
         }// otherwise, we are not to the time limit, so don't change
         }
         } else {
         lVictor.set(0);
         }
         */
        //launchServo.set(launchStick.getThrottle());
        if (RobotRunner.getArmFrozen()) {
            launchVictor.set(0.0);
        } else {
            System.out.println(launchStick.getThrottle() + "     " + launchServo.get());
            //launchServo.set(launchStick.getThrottle());
            System.out.println("Time =" + timer.get());
            double launchSignal = launchStick.getY();
            
            switch (currentState) {
                
                case NEUTRAL:
                    launchVictor.set(0.0);
                    launchServo.set(0.0);
                    if (launchButton.get() == true) {//if in auto mode and button 2 pressed
                        timer.reset();
                        timer.start();
                        currentState = LAUNCHING;
                    }
                    if (manualLaunchButton.get() {
                        currentState = MANUAL_LATCHING;
                    }

                    break;
                    
                case MANUAL_LAUNCHING:
                    launchVictor.set(1.0);
                    //launchServo.set(0.0);
                    if (autoLaunchButton.get() == true && RobotRunner.getManualMode() == false) {
                        currentState = AUTO_LAUNCHING;
                    }
//                    if (manualLaunchButton.get() && RobotRunner.getManualMode() == true) {
//                        MANUAL_LAUNCHING = MANUAL_LAUNCHING;
//                    }
                    if (launchSignal < 0 && RobotRunner.getManualMode() == true) {
                        currentState = LAUNCH_REVERSE;
                    }
                    if (launchSignal > 0 && RobotRunner.getManualMode() == true) {
                        currentState = LAUNCH_FORWARD;
                    }

                    break;
                    
                case AUTO_LAUNCHING:
                    launchVictor.set(1.0);
                    //launchServo.set(0.0);
//                    if (autoLaunchButton.get() == true && RobotRunner.getManualMode() == false) {//if in auto mode and button 2 pressed
//                        currentState = AUTO_LAUNCHING;
//                    }
                    if (manualLaunchButton.get() && RobotRunner.getManualMode() == true) {
                        currentState = MANUAL_LAUNCHING;
                    }
                    if (launchSignal < 0 && RobotRunner.getManualMode() == true) {
                        currentState = LAUNCH_REVERSE;
                    }
                    if (launchSignal > 0 && RobotRunner.getManualMode() == true) {
                        currentState = LAUNCH_FORWARD;
                    }

                    break;
                    
                case LAUNCH_REVERSE:
                    launchVictor.set(launchStick.getY());
                    //launchServo.set(0.0);
                    if (autoLaunchButton.get() == true && RobotRunner.getManualMode() == false) {//if in auto mode and button 2 pressed
                        currentState = AUTO_LAUNCHING;
                    }
                    if (manualLaunchButton.get() && RobotRunner.getManualMode() == true) {
                        currentState = MANUAL_LAUNCHING;
                    }
//                    if (launchSignal < 0 && RobotRunner.getManualMode() == true) {
//                        currentState = LAUNCH_REVERSE;
//                    }
                    if (launchSignal > 0 && RobotRunner.getManualMode() == true) {
                        currentState = LAUNCH_FORWARD;
                    }

                    break;
                    
                case LAUNCH_FORWARD:
                    //launchServo.set(UNLATCH_POSITION);
                    launchVictor.set(launchStick.getY());
                    timer.reset();
                    timer.start();
                    //isLaunching = true;
                    if (timer.get() > 1) {
                        currentState = NEUTRAL;
                    }
                    launchVictor.set(launchStick.getY());
                    launchServo.set(0.0);
                    if (autoLaunchButton.get() == true && RobotRunner.getManualMode() == false) {//if in auto mode and button 2 pressed
                        currentState = AUTO_LAUNCHING;
                    }
                    if (manualLaunchButton.get() && RobotRunner.getManualMode() == true) {
                        currentState = MANUAL_LAUNCHING;
                    }
                    if (launchSignal < 0 && RobotRunner.getManualMode() == true) {
                        currentState = LAUNCH_REVERSE;
                    }
//                    if (launchSignal > 0 && RobotRunner.getManualMode() == true) {
//                        currentState = LAUNCH_FORWARD;
//                    }

                    break;
            }
        }
    }

    public void testPeriodic() {
    }

    public void disabledPeriodic() {
    }

    public void autonomousInit() {
        System.out.println("Launch Component initialized for autonomous");
    }

    public void disabledInit() {
    }

    public boolean isLaunching() {
        return isLaunching;
    }
}
