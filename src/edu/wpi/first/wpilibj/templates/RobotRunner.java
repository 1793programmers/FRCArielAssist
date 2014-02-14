/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.ADXL345_I2C;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotRunner extends IterativeRobot {

    //Gunner is Awesome!
    //DRIVE FIELDS BELOW!
    private Joystick driveJoystick;
    private CANJaguar fljag;  //Front Left Wheel Jag
    private CANJaguar rljag; //Rear Left Wheel Jag
    private CANJaguar frjag; //Front Right Wheel Jag
    private CANJaguar rrjag; //Rear Right Wheel Jag
    private ADXL345_I2C accel;
    //ARM FIELDS BELOW!
    private Joystick armJoystick;
    private JoystickButton launchButton;
    private JoystickButton retractButton;
    private JoystickButton grabButton; //4 on left side of joy, 5 on right side. in <-- out -->
    private JoystickButton releaseButton;
    private Victor liftVictor;
    private Victor grabVictor;
    private Victor launchVictor;
    //SWITCHES BELOW!
    private DigitalInput gFLimitSwitch;
    private DigitalInput gBLimitSwitch;
    private DigitalInput launchLimitSwitch;
    //TIME FOR BUSINESS! COMPONENTS INSTANTIATED BELOW!!
    private DriveComponent driveComp;
    private GrabComponent grabComp;
    private static LaunchComponent launchComp;
    private LiftComponent liftComp;
    private CameraComponent cameraComp;
    private RobotComponent[] components = new RobotComponent[5];
    
    private TestComponent testComp;
    //Put all components above in an array to traverse later

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        //Jag instantiations below
        driveJoystick = new Joystick(1);
        accel = new ADXL345_I2C(1, ADXL345_I2C.DataFormat_Range.k2G);
        armJoystick = new Joystick(2);
        launchButton = new JoystickButton(armJoystick, 3);
        retractButton = new JoystickButton(armJoystick, 2);
        grabButton = new JoystickButton(armJoystick, 4);
        releaseButton = new JoystickButton(armJoystick, 5);
        grabVictor = new Victor(1);
        launchVictor = new Victor(2);
        liftVictor = new Victor(3);
        gFLimitSwitch = new DigitalInput(1);
        gBLimitSwitch = new DigitalInput(2);
        launchLimitSwitch = new DigitalInput(3);
        
        try {
            fljag = new CANJaguar(2); //Front Left Wheel Jag
            rljag = new CANJaguar(3); //Rear Left Wheel Jag
            frjag = new CANJaguar(4); //Front Right Wheel Jag
            rrjag = new CANJaguar(5); //Rear Right Wheel Jag
            

        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
          driveComp = new DriveComponent(driveJoystick, fljag, rljag, frjag, rrjag, accel);
          grabComp = new GrabComponent(armJoystick, grabButton, releaseButton, grabVictor);
          launchComp = new LaunchComponent(armJoystick, launchButton, retractButton, launchVictor);
          liftComp = new LiftComponent(armJoystick, liftVictor, gFLimitSwitch, gBLimitSwitch);
          cameraComp = new CameraComponent();
          //testComp = new TestComponent(armJoystick, launchButton, retractButton, launchVictor);
 //         components[0] = testComp;

//Run each component's initialize method
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousInit() {
        /*for (int i = 0; i < components.length; i++) {
            components[i].autonomousInit();
        }*/
    }

    public void autonomousPeriodic() {
        /*for (int i = 0; i < components.length; i++) {
            components[i].autonomousPeriodic();
        }*/
    }

    /**
     * This function is called periodically during operator control
     */
    public void disabledInit() {
        /*for (int i = 0; i < components.length; i++) {
            components[i].disabledInit();
        }*/
    }

    public void disabledPeriodic() {
       /*for (int i = 0; i < components.length; i++) {
            components[i].disabledPeriodic();
        }*/
    }

    public void teleopInit() {
        //testComp.teleopInit();        
        /*for (int i = 0; i < components.length; i++) {
            components[i].teleopInit();
        }*/
    }

    public void teleopPeriodic() {
        /*for (int i = 0; i < components.length; i++) {
            components[i].teleopPeriodic();
        }*/
        launchComp.teleopPeriodic();
        grabComp.teleopPeriodic();
        liftComp.teleopPeriodic();
        driveComp.teleopPeriodic();
        cameraComp.teleopPeriodic();
        //components[0].teleopPeriodic();
//        components[2].teleopPeriodic();
//        components[3].teleopPeriodic();
//        components[4].teleopPeriodic();
        
    }
    
    public static LaunchComponent getLaunchComponent(){
        return launchComp;
    }
    /**
     * This function is called periodically during test mode
     */
}
