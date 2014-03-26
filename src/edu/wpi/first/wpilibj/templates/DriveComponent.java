package edu.wpi.first.wpilibj.templates;

import com.sun.squawk.debugger.Log;
import edu.wpi.first.wpilibj.ADXL345_I2C;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Jaguar;
//import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
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
    private Victor frontLeftMotor;  //Front Left Wheel Jag
    private Victor rearLeftMotor; //Rear Left Wheel Jag
    private Victor frontRightMotor; //Front Right Wheel Jag
    private Victor rearRightMotor; //Rear Right Wheel Jag
    private JoystickButton resetGyroButton;
//    private CANJaguar frontLeftMotor;  //Front Left Wheel Jag
//    private CANJaguar rearLeftMotor; //Rear Left Wheel Jag
//    private CANJaguar frontRightMotor; //Front Right Wheel Jag
//    private CANJaguar rearRightMotor; //Rear Right Wheel Jag
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
    private static double joyXValue;
    private static double joyYValue;
    private static double magnitude;
    private static double direction;
    Timer timer = new Timer();

    public DriveComponent(Joystick j, JoystickButton jb1, Victor frontLeft, Victor rearLeft, Victor frontRight, Victor rearRight) {
        dStick = j;
        resetGyroButton = jb1;
        frontLeftMotor = frontLeft;
        rearLeftMotor = rearLeft;
        frontRightMotor = frontRight;
        rearRightMotor = rearRight;
        drive = new RobotDrive(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor);
    }

    public double atan2(double y, double x) {
        double coeff_1 = Math.PI / 4d;
        double coeff_2 = 3d * coeff_1;
        double abs_y = Math.abs(y) + 1e-10f;
        double r, angle;
        if (x >= 0d) {
            r = (x - abs_y) / (x + abs_y);
            angle = coeff_1;
        } else {
            r = (x + abs_y) / (abs_y - x);
            angle = coeff_2;
        }

        angle += (0.1963f * r * r - 0.9817f) * r;

        return y < 0.0f ? Math.toDegrees(-angle)-90 : Math.toDegrees(angle)-90.0;
    }
    /* public DriveComponent(Joystick j, CANJaguar jag2, CANJaguar jag3, CANJaguar jag4, CANJaguar jag5, ADXL345_I2C a, Servo s) {
     dStick = j;
     //resetGyroButton = jb1;
     frontLeftMotor = jag2;
     rearLeftMotor = jag3;
     frontRightMotor = jag4;
     rearRightMotor = jag5;
     accel = a;
     drive = new RobotDrive(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor);
     drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
     drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
     testDriveServo = s;
     }*/

    public void autonomousInit() {
        currentState = WAITING;
    }

    public void autonomousPeriodic() {
        switch (getCurrentState()) {
            case WAITING:

                drive.mecanumDrive_Polar(0, 0, 0);

                if (LaunchComponent.getCurrentState() == 7
                        && LiftComponent.getCurrentState() == 5) {
                    currentState = APPROACH;
                }
                break;
            case APPROACH:
                magnitude = 1;
                direction = atan2(1,0);
                //System.out.println("Arc Tan 2 "+);
                if (RobotRunner.getUltrasonicComp().getRangeInches() < 72) {
                    timer.reset();
                    timer.start();
                    currentState = LAUNCH;
                }
                drive.mecanumDrive_Polar(magnitude, direction, 0.0);
//                frontLeftMotor.set(1.0);
//                rearLeftMotor.set(1.0);
//                frontRightMotor.set(-1.0);
//                rearRightMotor.set(-1.0);
                break;



            case LAUNCH:
                if (timer.get() < 1) {
                      drive.mecanumDrive_Polar(1, 1, 0.0);
                } else {
                      drive.mecanumDrive_Polar(0, 0, 0);
                    currentState = COMPLETE;
                }
                break;
            case COMPLETE:

                drive.mecanumDrive_Polar(0, 0, 0);


                break;
        }
    }
    /*if (ultrasonic.getAverageVoltage() != 12) {
     try {
     frontLeftMotor.setX(0);
     rearLeftMotor.setX(0);
     frontRightMotor.setX(0);
     rearRightMotor.setX(0);
     } catch (CANTimeoutException ex) {
     ex.printStackTrace();
     }
     }
     else {
     try {
     frontLeftMotor.setX(1);
     rearLeftMotor.setX(1);
     frontRightMotor.setX(1);
     rearRightMotor.setX(1);
     } catch (CANTimeoutException ex) {
            
     }
            
     }*/

    public void disabledInit() {
        //  throw new java.lang.UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void disabledPeriodic() {
        //   throw new java.lang.UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void teleopInit() {
//        
//     try {
//            rearLeftMotor.setX(0);
//            frontLeftMotor.setX(0);
//            frontRightMotor.setX(0);
//            rearRightMotor.setX(0);
//        } catch (CANTimeoutException ex) {
//            ex.printStackTrace();
//        }
        //System.out.println("Drive Component initialized for teleop");
    }

    public void teleopPeriodic() {
        joyXValue = dStick.getX();
        joyYValue = -dStick.getY();
        magnitude = Math.sqrt(joyXValue * joyXValue + joyYValue * joyYValue);
        direction = atan2(joyYValue, -joyXValue);
        drive.mecanumDrive_Polar(magnitude, direction, dStick.getTwist());
//      drive.mecanumDrive_Cartesian(joyXValue, joyYValue, dStick.getTwist(), RobotRunner.getGyro().getAngle());
        // for calibration
        //frontLeftMotor.set(dStick.getX());
        //frontRightMotor.set(dStick.getX());
        //rearLeftMotor.set(dStick.getX());
        //rearRightMotor.set(dStick.getX());
//        try {
//            rearRightMotor.setX(dStick.getThrottle());
//        } catch (CANTimeoutException ex) {
//            ex.printStackTrace();
//        }
        //Fixing Forward, Backward, and Twisting
        //testDriveServo.set(dStick.getThrottle());
        if (resetGyroButton.get()) {
            RobotRunner.getGyro().reset();
        }
        //System.out.println(testDriveServo.get());
        //System.out.println("Gyro Angle =" + RobotRunner.getGyro().getAngle());
    }

    public static int getCurrentState() {
        return currentState;
    }
}
