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

/**
 *
 * @author milo
 */
public class LaunchComponent implements RobotComponent {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private Victor lVictor;
    private Joystick launchStick;
    private JoystickButton launchButt;
    private JoystickButton retractButt;
    
    public LaunchComponent(Joystick j, JoystickButton jb1, JoystickButton jb2, Victor v){
        launchStick = j;
        launchButt = jb1;
        retractButt = jb2;
        lVictor = v;
    }

    public void autonomousPeriodic() {
    }

    public void teleopPeriodic() {
        
        if (launchButt.get() == true){
           lVictor.set(1);
           Timer.delay(1);
           lVictor.set(-1);
        }
        else {
           lVictor.set(0);
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
