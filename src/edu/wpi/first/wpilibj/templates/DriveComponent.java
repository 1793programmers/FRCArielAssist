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

    /**
     * @return the currentState
     */
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
    //AUTONOMOUS STATES
    public static final int WAITING = 1;
    public static final int APPROACH = 2;
    public static final int LAUNCH = 3;
    public static final int COMPLETE = 4;
    private static int currentState = WAITING;
    Timer timer = new Timer();

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

    public void autonomousInit() {
        currentState = WAITING;
    }

    public void autonomousPeriodic() {
        switch (getCurrentState()) {
            case WAITING:
                try {
                    fljag.setX(0);
                    rljag.setX(0);
                    frjag.setX(0);
                    rrjag.setX(0);
                } catch (CANTimeoutException ex) {
                    ex.printStackTrace();
                }
                if (LaunchComponent.getCurrentState() == 7
                        && LiftComponent.getCurrentState() == 5) {
                    currentState = APPROACH;
                }
                break;
            case APPROACH:
                if (RobotRunner.getUltrasonicComp().getRangeInches() < 84) {
                    timer.reset();
                    timer.start();
                    currentState = LAUNCH;
                }
                try {
                    fljag.setX(1.0);
                    rljag.setX(1.0);
                    frjag.setX(-1.0);
                    rrjag.setX(-1.0);
                } catch (CANTimeoutException ex) {
                    ex.printStackTrace();
                }
                break;

            case LAUNCH:
                if (timer.get() < 1) {
                    try {
                        fljag.setX(1.0);
                        rljag.setX(1.0);
                        frjag.setX(-1.0);
                        rrjag.setX(-1.0);
                    } catch (CANTimeoutException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    try {
                        fljag.setX(0);
                        rljag.setX(0);
                        frjag.setX(0);
                        rrjag.setX(0);
                    } catch (CANTimeoutException ex) {
                        ex.printStackTrace();
                    }
                    currentState = COMPLETE;
                }
                break;
            case COMPLETE:
                try {
                    fljag.setX(0);
                    rljag.setX(0);
                    frjag.setX(0);
                    rrjag.setX(0);
                } catch (CANTimeoutException ex) {
                    ex.printStackTrace();
                }
                break;
        }

        /*if (ultrasonic.getAverageVoltage() != 12) {
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
            
         }*/
    }

    public void teleopPeriodic() {
        drive.mecanumDrive_Cartesian(-dStick.getX() * .5, -dStick.getY() * .5, -dStick.getTwist() * .5, RobotRunner.getGyro().getAngle());//Fixing Forward, Backward, and Twisting
        //testDriveServo.set(dStick.getThrottle());
        if (resetGyroButton.get()) {
            RobotRunner.getGyro().reset();
        }
        //System.out.println(testDriveServo.get());
        //System.out.println("Gyro Angle =" + RobotRunner.getGyro().getAngle());
    }

    public void disabledPeriodic() {
    }

//    @Override
    public void disabledInit() {
    }

//    @Override
    public void teleopInit() {
        //System.out.println("Drive Component initialized for teleop");
    }

    public static int getCurrentState() {
        return currentState;
    }
}
