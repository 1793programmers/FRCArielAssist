/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.AnalogChannel;

/**
 *
 * @author milo
 */
public class UltrasonicComponent implements RobotComponent{
    
    //private AnalogChannel ultrasonic;
    private final double SCALE_FACTOR = 5.108/512; // Vcc/512, multimeter value
    private final double OFFSET = 0.0;
    private final double MAX_RELIABLE = 240; // 20 feet
    private final double MIN_RELIABLE = 10; // 10 inches

    public double getRangeInches(){
        AnalogChannel ultrasonicDetector = RobotRunner.getUltrasonicSensor();
        double range = ultrasonicDetector.getAverageVoltage()/SCALE_FACTOR-OFFSET;
        if (range > MIN_RELIABLE && range < MAX_RELIABLE) {
            return range;
        }
        return 0.0;  // don't rely on values outside our reliable range
    }

    public void autonomousInit() {
 //       throw new java.lang.UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void autonomousPeriodic() {
 //       throw new java.lang.UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void disabledInit() {
//        throw new java.lang.UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void disabledPeriodic() {
 //       throw new java.lang.UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void teleopInit() {
 //       throw new java.lang.UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void teleopPeriodic() {
//       throw new java.lang.UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    
    }
    
}
