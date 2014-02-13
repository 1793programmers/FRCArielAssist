/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot {
   
    private RobotDrive drive;
    private Joystick driveStick;
    private Joystick armStick;
    private CANJaguar fljag;  //Front Left Wheel Jag
    private CANJaguar rljag; //Rear Left Wheel Jag
    private CANJaguar frjag; //Front Right Wheel Jag
    private CANJaguar rrjag; //Rear Right Wheel Jag
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        System.out.println("Robot Ran Init.");
        driveStick = new Joystick(1);
        armStick = new Joystick(2);
        try {
        fljag = new CANJaguar(2); //Front Left Wheel Jag
        rljag = new CANJaguar(3); //Rear Left Wheel Jag
        frjag = new CANJaguar(4); //Front Right Wheel Jag
        rrjag = new CANJaguar(5); //Rear Right Wheel Jag
         drive = new RobotDrive(rljag, rrjag);
         } catch (CANTimeoutException ex) {
           ex.printStackTrace();
    }
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        //System.out.println("TeleOp");
        System.out.println("Controller Activated!");
        try {
            fljag.setX(driveStick.getY());
            rljag.setX(driveStick.getY());
            frjag.setX(driveStick.getThrottle());
            rrjag.setX(driveStick.getThrottle());
            System.out.println("Leonard made me do it!!");
        } catch (CANTimeoutException ex) {
          ex.printStackTrace();
        }
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}
