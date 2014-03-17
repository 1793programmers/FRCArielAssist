package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.ADXL345_I2C;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 *
 * @author milo
 */
//
public class DriveComponent implements RobotComponent {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    private RobotDrive drive;
    private Joystick dStick;
    private JoystickButton resetGyroButton;
    private CANJaguar fljag;  //Front Left Wheel Jag
    private CANJaguar rljag; //Rear Left Wheel Jag
    private CANJaguar frjag; //Front Right Wheel Jag
    private CANJaguar rrjag; //Rear Right Wheel Jag
    private ADXL345_I2C accel;
    private Servo testDriveServo;
    private double accelerationX;
    private double accelerationY;
    private double accelerationZ;
    ADXL345_I2C.AllAxes accelerations;
    AnalogChannel ultrasonic = RobotRunner.getUltrasonicSensor();
        //for now, if ultrasonic sensor returns 7 feet in autonomous, launch with a timer delay to stop (can be anywhere btwn 0 & 2 seconds)

    public DriveComponent(Joystick j, JoystickButton jb1, CANJaguar jag2, CANJaguar jag3, CANJaguar jag4, CANJaguar jag5, ADXL345_I2C a, Servo s) {
        dStick = j;
        resetGyroButton = jb1;
        fljag = jag2;
        rljag = jag3;
        frjag = jag4;
        rrjag = jag5;
        accel = a;
        drive = new RobotDrive(fljag, rljag, frjag, rrjag);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
        testDriveServo = s;
    }

    public void autonomousPeriodic() {
        if (ultrasonic.getAverageVoltage() != 12) {
            try {
                fljag.setX(0);
                rljag.setX(0);
                frjag.setX(0);
                rrjag.setX(0);
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
        }
        else {
        try {
            fljag.setX(1);
            rljag.setX(1);
            frjag.setX(1);
            rrjag.setX(1);
        } catch (CANTimeoutException ex) {
            
        }
            
        }
    }

    public void teleopPeriodic() {
        drive.mecanumDrive_Cartesian(-dStick.getX() * .5, -dStick.getY() * .5, -dStick.getTwist() * .5, RobotRunner.getGyro().getAngle());//Fixing Forward, Backward, and Twisting
        //testDriveServo.set(dStick.getThrottle());
        if (resetGyroButton.get()) {
            RobotRunner.getGyro().reset();
        }
        //System.out.println(testDriveServo.get());
        System.out.println("Gyro Angle =" + RobotRunner.getGyro().getAngle());
    }

    public void disabledPeriodic() {
    }

//    @Override
    public void autonomousInit() {
        System.out.println("Drive Component initialized for autonomous");
    }

//    @Override
    public void disabledInit() {
    }

//    @Override
    public void teleopInit() {
        System.out.println("Drive Component initialized for teleop");
    }
}
