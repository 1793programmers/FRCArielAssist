package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.ADXL345_I2C;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;
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

    public DriveComponent(Joystick j, CANJaguar jag2, CANJaguar jag3, CANJaguar jag4, CANJaguar jag5, ADXL345_I2C a, Servo s) {
        dStick = j;
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
        try {
            fljag.setX(1);
            rljag.setX(1);
            frjag.setX(1);
            rrjag.setX(1);
            Timer.delay(2);
            fljag.setX(0);
            rljag.setX(0);
            frjag.setX(0);
            rrjag.setX(0);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    public void teleopPeriodic() {
        drive.mecanumDrive_Cartesian(-dStick.getY(), dStick.getX(),-dStick.getTwist(), RobotRunner.getGyro().getAngle() - 90);//Fixing Forward, Backward, and Twisting
        testDriveServo.set(dStick.getThrottle());
        System.out.println(testDriveServo.get());
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
