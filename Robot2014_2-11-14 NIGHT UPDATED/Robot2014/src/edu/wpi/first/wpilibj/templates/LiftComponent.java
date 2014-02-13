/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;

/**
 *
 * @author MEOW MEOW
 */

public class LiftComponent implements RobotComponent {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private Joystick armStick; 
    private Victor armVictor;
    DigitalInput fLimitSwitch; 
    DigitalInput bLimitSwitch;
    
    public LiftComponent(Joystick j, Victor v, DigitalInput s1, DigitalInput s2){
        armStick = j;
        armVictor = v;
        fLimitSwitch = s1;
        bLimitSwitch = s2;
    }
    
   
    
    public void autonomousInit() {
        armVictor.set(-1.0); //move to shooting position
        boolean isBLimitOpen = bLimitSwitch.get(); 
        if (!isBLimitOpen) { //when limitswitch is closed the arm will stop
            armVictor.set(0); 
        }
    }

    public void autonomousPeriodic() {
       
        
    }

    public void teleopInit() {
         

    }

    public void teleopPeriodic() {
        System.out.println("Reached teleop"); 
        
        boolean isFLimitOpen = fLimitSwitch.get();  
        boolean isBLimitOpen = bLimitSwitch.get(); 
        double armSignal = armStick.getY(); //vertical axis of joystick 
        if(armSignal < 0) { //move forward
            if(isFLimitOpen) { 
                armVictor.set(1.0); //move motor forward
            System.out.println(armVictor.get()); //shows pwm value 
            } else { 
                armVictor.set(0); 
            }
        } else if (armSignal > 0){
            if(isBLimitOpen) {
                armVictor.set(-1.0);
            } else {
                armVictor.set(0); 
            }
        } else {
            armVictor.set(0);
        }
        
    }
              
    
    public void disabledInit() {

    }

    public void disabledPeriodic() {
    }

  
}
