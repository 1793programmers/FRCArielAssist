/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.ADXL345_I2C;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotRunner extends IterativeRobot {

    /**
     * @return the ultrasonicComp
     */
    private final boolean testBoard = true; // which JAG IDs to load
    //DRIVE FIELDS BELOW!
    private Joystick driveJoystick;
    private JoystickButton resetGyroButton;
    private CANJaguar fljag;  //Front Left Wheel Jag
    private CANJaguar rljag; //Rear Left Wheel Jag
    private CANJaguar frjag; //Front Right Wheel Jag
    private CANJaguar rrjag; //Rear Right Wheel Jag
    private ADXL345_I2C accel;
    private static Gyro gyro;
    //ARM FIELDS BELOW!
    private Joystick armJoystick;
    private JoystickButton automaticButton;
    private JoystickButton latchButton;
    private JoystickButton cockButton;
    private JoystickButton freezeButton;
    private JoystickButton thawButton;
    private JoystickButton shootButton;
    private JoystickButton grabButton; //4 on left side of joy, 5 on right side. in <-- out -->
    private JoystickButton passButton;
    private Victor liftVictor;
    private Victor grabVictor;
    private Victor launchVictor;
    private Servo triggerServo;
    //SWITCHES BELOW!
    private static DigitalInput forwardLiftLimitSwitch;
    private static DigitalInput backwardLiftLimitSwitch;
    private static DigitalInput forwardLaunchLimitSwitch;
    private static DigitalInput backwardLaunchLimitSwitch;
    private static DigitalInput grabberLimitSwitch;
    private static AnalogChannel ultrasonicDetector;
    //CAMERA BELOW
    private AxisCamera camera;
    //TIME FOR BUSINESS! COMPONENTS INSTANTIATED BELOW!!
    private static DriveComponent driveComp;
    private static GrabComponent grabComp;
    private static LaunchComponent launchComp;
    private static LiftComponent liftComp;
    private static UltrasonicComponent ultrasonicComp;
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
        //gyro.setSensitivity(.0125);
        accel = new ADXL345_I2C(1, ADXL345_I2C.DataFormat_Range.k2G);
        resetGyroButton = new JoystickButton(driveJoystick, 6);

        // arm stick buttons
        armJoystick = new Joystick(2);
        automaticButton = new JoystickButton(armJoystick, 11);
        latchButton = new JoystickButton(armJoystick, 5);
        cockButton = new JoystickButton(armJoystick, 7);
        freezeButton = new JoystickButton(armJoystick, 9);
        thawButton = new JoystickButton(armJoystick, 10);
        shootButton = new JoystickButton(armJoystick, 2);
        grabButton = new JoystickButton(armJoystick, 4);
        passButton = new JoystickButton(armJoystick, 3);
        grabVictor = new Victor(1);
        launchVictor = new Victor(2);
        liftVictor = new Victor(3);
        forwardLiftLimitSwitch = new DigitalInput(6);
        backwardLiftLimitSwitch = new DigitalInput(2);
        forwardLaunchLimitSwitch = new DigitalInput(3);
        backwardLaunchLimitSwitch = new DigitalInput(4);
        grabberLimitSwitch = new DigitalInput(5);
        triggerServo = new Servo(9);
        ultrasonicDetector = new AnalogChannel(7);
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
       //         System.out.println("Jags Initialized!");
            }
            camera = AxisCamera.getInstance();
    //        System.out.println(camera.toString());
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        driveComp = new DriveComponent(driveJoystick, resetGyroButton, fljag, rljag, frjag, rrjag, accel, triggerServo);
        grabComp = new GrabComponent(grabButton, shootButton, passButton, grabVictor, getGrabberLimitSwitch());
        launchComp = new LaunchComponent(automaticButton, latchButton, cockButton, freezeButton, thawButton, shootButton, launchVictor, getLauncherForwardLimitSwitch(), getLauncherBackLimitSwitch(), triggerServo);
        liftComp = new LiftComponent(armJoystick, liftVictor, forwardLiftLimitSwitch, backwardLiftLimitSwitch);
        ultrasonicComp = new UltrasonicComponent();
        cameraComp = new CameraComponent(camera);
        // Collect 
        components[0] = driveComp;
        components[1] = grabComp;
        components[2] = launchComp;
        components[3] = liftComp;
        components[4] = cameraComp;
        // updateSmartDashboard();

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
        updateSmartDashboard();
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
        updateSmartDashboard();
    }

    public void teleopInit() {
        for (int i = 0; i < components.length; i++) {
            components[i].teleopInit();
        }
        //cameraComp.teleopInit();
    }

    public void teleopPeriodic() {
        for (int i = 0; i < components.length; i++) {
            components[i].teleopPeriodic();
        }
        //cameraComp.teleopPeriodic();
        updateSmartDashboard();
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

    public static UltrasonicComponent getUltrasonicComp() {
        return ultrasonicComp;
    }

//    public static CameraComponent getCameraComponent() {
//        return cameraComp;
//    }
    public static Gyro getGyro() {
        return gyro;
    }

    /**
     * @return the forwardLiftLimitSwitch
     */
    public static DigitalInput getLiftForwardLimitSwitch() {
        return forwardLiftLimitSwitch;
    }

    /**
     * @return the backwardLiftLimitSwitch
     */
    public static DigitalInput getLiftBackLimitSwitch() {
        return backwardLiftLimitSwitch;
    }

    /**
     * @return the forwardLaunchLimitSwitch
     */
    public static DigitalInput getLauncherForwardLimitSwitch() {
        return forwardLaunchLimitSwitch;
    }

    /**
     * @return the backwardLaunchLimitSwitch
     */
    public static DigitalInput getLauncherBackLimitSwitch() {
        return backwardLaunchLimitSwitch;
    }

    /**
     * @return the grabberLimitSwitch
     */
    public static DigitalInput getGrabberLimitSwitch() {
        return grabberLimitSwitch;
    }

    public static AnalogChannel getUltrasonicSensor() {
        return ultrasonicDetector;
    }

    protected void updateSmartDashboard() {
        SmartDashboard.putNumber("Lift Signal", liftVictor.get());
        SmartDashboard.putNumber("Gyro Angle", gyro.getAngle());
        int liftState = liftComp.getCurrentState();
        SmartDashboard.putBoolean("Lift at Forward Limit", forwardLiftLimitSwitch.get());
        SmartDashboard.putBoolean("Lift at Rear Limit", backwardLiftLimitSwitch.get());
        SmartDashboard.putBoolean("NEUTRAL_LIFT", liftState == LiftComponent.NEUTRAL);
        SmartDashboard.putBoolean("DEPLOYING", liftState == LiftComponent.DEPLOYING);
        SmartDashboard.putBoolean("RETRACTING", liftState == LiftComponent.RETRACTING);
        SmartDashboard.putBoolean("LIFT READY", liftState == LiftComponent.READY);
        SmartDashboard.putNumber("Launcher Signal", launchVictor.get());
        SmartDashboard.putBoolean("Launcher Forward Limit", forwardLaunchLimitSwitch.get());
        SmartDashboard.putBoolean("Launcher Rear Limit", backwardLaunchLimitSwitch.get());
        int driveState = driveComp.getCurrentState();
        SmartDashboard.putBoolean("WAITING", driveState == DriveComponent.WAITING);
        SmartDashboard.putBoolean("APPROACH", driveState == DriveComponent.APPROACH);
        SmartDashboard.putBoolean("LAUNCH", driveState == DriveComponent.LAUNCH);
        int launcherState = launchComp.getCurrentState();
        SmartDashboard.putBoolean("NEUTRAL_LAUNCHER", launcherState == LaunchComponent.NEUTRAL);
        SmartDashboard.putBoolean("MANUAL_LATCH", launcherState == LaunchComponent.MANUAL_LATCHING);
        SmartDashboard.putBoolean("MANUAL_COCK", launcherState == LaunchComponent.MANUAL_COCKING);
        SmartDashboard.putBoolean("AUTO_LATCH", launcherState == LaunchComponent.AUTO_LATCHING);
        SmartDashboard.putBoolean("AUTO_COCK", launcherState == LaunchComponent.AUTO_COCKING);
        SmartDashboard.putBoolean("LAUNCHING", launcherState == LaunchComponent.LAUNCHING);
        SmartDashboard.putBoolean("LAUNCH READY", launcherState == LaunchComponent.READY);
        SmartDashboard.putBoolean("Grabbing Ball Detector", grabberLimitSwitch.get());
        int grabberState = grabComp.getCurrentState();
        SmartDashboard.putBoolean("NEUTRAL_GRABBER", grabberState == GrabComponent.NEUTRAL);
        SmartDashboard.putBoolean("GRABBING", grabberState == GrabComponent.GRABBING);
        SmartDashboard.putBoolean("PASSING", grabberState == GrabComponent.PASSING);
        SmartDashboard.putNumber("Grabber Signal", grabVictor.get());
        SmartDashboard.putNumber("Range Finder Inches ", getUltrasonicComp().getRangeInches());
        SmartDashboard.putNumber("Range Finder Feet ", getUltrasonicComp().getRangeInches() / 12);
    }
}
