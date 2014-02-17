package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ColorImage;

/**
 *
 * @author milo
 */
public class CameraComponent implements RobotComponent {

    AxisCamera camera; //Camera
    private int imageWidth;//width of image
    //DriverStation dS = DriverStation.getInstance();
    ColorImage image;

    public CameraComponent(AxisCamera c) {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());

        camera = c;
        camera.writeMaxFPS(5);
        camera.writeResolution(AxisCamera.ResolutionT.k160x120);
        camera.writeCompression(20);
        camera.writeBrightness(0);
        System.out.println("Camera Activated");
    }

    public void autonomousPeriodic() {
        if (camera.getBrightness() == 100) {
            //shoot!
        }
    }

    public void teleopPeriodic() {
        if (camera.freshImage()) {
            try {
                image = camera.getImage();
                image.free();
            } catch (AxisCameraException ex) {
                ex.printStackTrace();
            } catch (NIVisionException ex) {
                ex.printStackTrace();
            }
        }

        //camera.writeRotation(AxisCamera.RotationT.k180);
        System.out.println(camera.getBrightness());
        DriverStationLCD.getInstance().updateLCD();
    }

    public void testPeriodic() {
    }

    public void disabledPeriodic() {
    }

    public void autonomousInit() {
        System.out.println("Camera Component initialized for autonomous");

    }

    public void disabledInit() {
    }

    public void teleopInit() {
        System.out.println("Camera Component initialized for teleop");
    }
}
