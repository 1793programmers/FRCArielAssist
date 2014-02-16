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
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotRunner extends IterativeRobot {

    private final boolean testBoard = false; // which JAG IDs to load
    //DRIVE FIELDS BELOW!
    private Joystick driveJoystick;
    private CANJaguar fljag;  //Front Left Wheel Jag
    private CANJaguar rljag; //Rear Left Wheel Jag
    private CANJaguar frjag; //Front Right Wheel Jag
    private CANJaguar rrjag; //Rear Right Wheel Jag
    private ADXL345_I2C accel;
    private static Gyro gyro;
    //ARM FIELDS BELOW!
    private Joystick armJoystick;
    private JoystickButton launchButton;
    private JoystickButton retractButton;
    private JoystickButton grabButton; //4 on left side of joy, 5 on right side. in <-- out -->
    private JoystickButton releaseButton;
    private JoystickButton resetButton; 
    private Victor liftVictor;
    private Victor grabVictor;
    private Victor launchVictor;
    private Servo launchServo;
    //SWITCHES BELOW!
    private DigitalInput gFLimitSwitch;
    private DigitalInput gBLimitSwitch;
    private DigitalInput launchLimitSwitch;
    private DigitalInput grabberLimitSwitch;
    //CAMERA BELOW
    private AxisCamera camera;
    //TIME FOR BUSINESS! COMPONENTS INSTANTIATED BELOW!!
    private static DriveComponent driveComp;
    private static GrabComponent grabComp;
    private static LaunchComponent launchComp;
    private static LiftComponent liftComp;
    private static CameraComponent cameraComp;
    private RobotComponent[] components = new RobotComponent[5];

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        //Jag instantiations below
        driveJoystick = new Joystick(1);
        gyro = new Gyro(1);
        accel = new ADXL345_I2C(1, ADXL345_I2C.DataFormat_Range.k2G);
        armJoystick = new Joystick(2);
        resetButton = new JoystickButton(armJoystick, 11); 
        launchButton = new JoystickButton(armJoystick, 1);
        retractButton = new JoystickButton(armJoystick, 2);
        grabButton = new JoystickButton(armJoystick, 4);
        releaseButton = new JoystickButton(armJoystick, 5);
        grabVictor = new Victor(1);
        launchVictor = new Victor(2);
        liftVictor = new Victor(3);
        gFLimitSwitch = new DigitalInput(1);
        gBLimitSwitch = new DigitalInput(2);
        launchLimitSwitch = new DigitalInput(3);
        grabberLimitSwitch = new DigitalInput(4);
        launchServo = new Servo(10);
        camera = AxisCamera.getInstance("192.168.0.90");

        try {
            if (testBoard) {
                fljag = new CANJaguar(2); //Front Left Wheel Jag
                rljag = new CANJaguar(3); //Rear Left Wheel Jag
                frjag = new CANJaguar(4); //Front Right Wheel Jag
                rrjag = new CANJaguar(5); //Rear Right Wheel Jag
            } else {
                fljag = new CANJaguar(6); //Front Left Wheel Jag
                rljag = new CANJaguar(11); //Rear Left Wheel Jag
                frjag = new CANJaguar(12); //Front Right Wheel Jag
                rrjag = new CANJaguar(7); //Rear Right Wheel Jag
            }

        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        driveComp = new DriveComponent(driveJoystick, fljag, rljag, frjag, rrjag, accel);
        grabComp = new GrabComponent(armJoystick, grabButton, releaseButton, grabVictor, grabberLimitSwitch);
        launchComp = new LaunchComponent(armJoystick, launchButton, retractButton, launchVictor, launchLimitSwitch, launchServo);
        liftComp = new LiftComponent(armJoystick, liftVictor, gFLimitSwitch, gBLimitSwitch, resetButton);
        cameraComp = new CameraComponent(camera);
        // Collect components
        components[0] = driveComp;
        components[1] = grabComp;
        components[2] = launchComp;
        components[3] = liftComp;
        components[4] = cameraComp;

    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousInit() {
        for (int i = 0; i < components.length; i++) {
         components[i].autonomousInit();
         }
    }

    public void autonomousPeriodic() {
        for (int i = 0; i < components.length; i++) {
         components[i].autonomousPeriodic();
         }
    }


    public void disabledInit() {
        for (int i = 0; i < components.length; i++) {
         components[i].disabledInit();
         }
    }

    public void disabledPeriodic() {
        for (int i = 0; i < components.length; i++) {
         components[i].disabledPeriodic();
         }
    }

    public void teleopInit() {
        for (int i = 0; i < components.length; i++) {
         components[i].teleopInit();
         }
    }

    public void teleopPeriodic() {
        for (int i = 0; i < components.length; i++) {
         components[i].teleopPeriodic();
         }
    }

    public static LaunchComponent getLaunchComponent() {
        return launchComp;
    }
    
    public static DriveComponent getDriveComponent() {
        return driveComp;
    }
    
    public static LiftComponent getLiftComponent() {
        return liftComp;
    }
    
    public static GrabComponent getGrabComponent() {
        return grabComp;
    }
    
    public static CameraComponent getCameraComponent() {
        return cameraComp;
    }
    
    public static Gyro getGyro(){
        return gyro;
    }

}
