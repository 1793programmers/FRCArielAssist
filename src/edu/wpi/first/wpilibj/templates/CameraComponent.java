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
    ColorImage image;

    public CameraComponent(AxisCamera c) {
        camera = c;
        camera.writeBrightness(60);
        System.out.println("Camera Activated");
    }

    public void autonomousPeriodic() {

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
