/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 *
 * @author MEOW MEOW
 */
public class LiftComponent implements RobotComponent {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private Joystick armStick;
    private JoystickButton resetButton;
    private Victor armVictor;
    static DigitalInput fLimitSwitch;
    static DigitalInput bLimitSwitch;
    private AnalogChannel ultrasonic = RobotRunner.getUltrasonicSensor();
    public static final int NEUTRAL = 1;
    public static final int DEPLOYING = 2;
    public static final int RETRACTING = 3;
    public static final int RESETTING = 4;
    private static int currentState = NEUTRAL;
    static boolean isBLimitOpen;
        //for now, if ultrasonic sensor returns 7 feet in autonomous, launch with a timer delay to stop (can be anywhere btwn 0 & 2 seconds)
    
    public LiftComponent(Joystick j, Victor v, DigitalInput s1, DigitalInput s2, JoystickButton b) {
        armStick = j;
        resetButton = b;
        armVictor = v;
        fLimitSwitch = s1;
        bLimitSwitch = s2;
        currentState = NEUTRAL;
    }

    public void autonomousInit() {
        armVictor.set(-1.0); //move to shooting position
        isBLimitOpen = bLimitSwitch.get();
        if (!isBLimitOpen) { //when limitswitch is closed the arm will stop
            armVictor.set(0);
        }
        System.out.println("Lift Component initialized for autonomous");
    }

    public void autonomousPeriodic() {
    }

    public void teleopInit() {
        System.out.println("Lift Component initialized for teleop");
        currentState = NEUTRAL;
    }

    public void teleopPeriodic() {
        boolean isForwardLimitSwitchShut = !fLimitSwitch.get();
        boolean isBackwardLimitSwitchShut = !bLimitSwitch.get();
//        printState(isForwardLimitSwitchShut, isBackwardLimitSwitchShut);
        armVictor.set(0.0);
        double armSignal = armStick.getThrottle();
        currentState=NEUTRAL;
        
        if (armSignal < -0.1) { 
//            System.out.println("Moving Arm Backwards");
            if (!isBackwardLimitSwitchShut) {
                armVictor.set(-armSignal);
                currentState=RETRACTING;
            }

        } else if (armSignal >0.1) { 
//            System.out.println("Moving Arm Forward");
            if (!isForwardLimitSwitchShut) {
                armVictor.set(-armSignal);
                currentState=DEPLOYING;
            }
        }
    }

    public void disabledInit() {
    }

    public void disabledPeriodic() {
    }
    
    public int getCurrentState(){
        return currentState;
    }
}
