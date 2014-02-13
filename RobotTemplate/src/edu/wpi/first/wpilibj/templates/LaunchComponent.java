/*
 * The launch component is composed of a motor and two buttons
 
 
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 *
 * @author milo
 */
public class LaunchComponent implements RobotComponent {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private Victor launcher;
    private Joystick launchStick;
    private JoystickButton launchButt;
    private JoystickButton retractButt;
    DigitalInput limitSwitch;
    
    public void robotInit() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
        launcher = new Victor(2);
        launchStick = new Joystick(2);
        launchButt = new JoystickButton(launchStick, 1);
        limitSwitch = new DigitalInput(1);
    }

    public void autonomousPeriodic() {
    if (limitSwitch.get()== true){
    launcher.set(1);
    Timer.delay(1);
    launcher.set(-1);
    }
    else {
        launcher.set(0);
            }
    }

    public void teleopPeriodic() {
        
        if (launchButt.get() == true){
           launcher.set(1);
           Timer.delay(1);
           launcher.set(-1);
        }
        else {
           launcher.set(0);
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
        
    }

}
