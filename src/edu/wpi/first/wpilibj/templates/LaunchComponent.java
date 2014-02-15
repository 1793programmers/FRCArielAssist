/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Servo;

/**
 *
 * @author milo
 */
public class LaunchComponent implements RobotComponent {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private Victor lVictor;
    private Joystick launchStick;
    private JoystickButton launchButton;
    private JoystickButton retractButton;
    private Timer timer;
    boolean isLaunching = false;
    private Servo launchServo;

    public LaunchComponent(Joystick j, JoystickButton jb1, JoystickButton jb2, Victor v) {
        launchStick = j;
        launchButton = jb1;
        retractButton = jb2;
        lVictor = v;
    }

    public void autonomousPeriodic() {

    }

    public void teleopPeriodic() {

        /* if (launchButton.get() == true){
         lVictor.set(1);
         // lVictor.set(-1);
         }
         else if (retractButton.get() == true){
         lVictor.set(-1);
         Timer.delay(1);
         }
         else {
         lVictor.set(0);
         }
         */
        if(launchSwitch.get()) {
            if (launchButton.get() == true) { // would like to launch
                if (!launching) { // and not currently in launch mode
                    timer.reset();
                    timer.start();
                    launching = true;
                    launchServo.set(1.0);
                 } // if already in launch mode, no need to change anything
             } else {  // don't want to launch
            if (timer.get() > (2000)) { // if beyond time limit, stop launching
                launchServo.set(0);
                launching = false;
                timer.reset();
                timer.stop();
            } // otherwise, we are not to the time limit, so don't change
            
        }
        
    }

    public void testPeriodic() {
    }

    public void disabledPeriodic() {
    }

    public void autonomousInit() {

    }

    public void disabledInit() {

    }

    public void teleopInit() {
        timer = new Timer();

    }

    public boolean getLaunching() {
        return launching;
    }
}
