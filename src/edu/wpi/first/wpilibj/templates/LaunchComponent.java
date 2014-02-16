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

    private Victor lVictor;
    private Joystick launchStick;
    private JoystickButton launchButton; // button 3
    private JoystickButton retractButton; // button 2
    private DigitalInput launchSwitch; //channel 4
    private Servo launchServo;
    private boolean isLaunching = false;
    private Timer timer;
    private static final double UNLATCH_POSITION = 0.0;
    private static final double LATCH_POSITION = 0.6425531914893617;

    public LaunchComponent(Joystick j, JoystickButton jb1, JoystickButton jb2, Victor v, DigitalInput dI1, Servo s1) {
        launchStick = j;
        launchButton = jb1;
        retractButton = jb2;
        lVictor = v;
        launchSwitch = dI1;
        launchServo = s1;
        timer = new Timer();
    }

    public void autonomousPeriodic() {
    }

    public void teleopInit() {
        launchServo.set(UNLATCH_POSITION);
        System.out.println("Launch Component initialized for teleop"); 
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
        System.out.println(launchStick.getThrottle() + "     " + launchServo.get());
        launchServo.set(launchStick.getThrottle());
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
