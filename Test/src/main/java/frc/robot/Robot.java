// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.TimedRobot;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  Timer timer = new Timer();
  CANSparkMax leftMotorFront = new CANSparkMax(, MotorType.kBrushless); 
  CANSparkMax leftMotorBack = new CANSparkMax(, MotorType.kBrushless);
  CANSparkMax rightMotorFront = new CANSparkMax(, MotorType.kBrushless);
  CANSparkMax rightMotorBack = new CANSparkMax(, MotorType.kBrushless);
  XboxController xboxCont = new XboxController(0);
  MotorControllerGroup leftSide = new MotorControllerGroup(leftMotorFront, leftMotorBack);
  MotorControllerGroup rightSide = new MotorControllerGroup(rightMotorFront, rightMotorBack);
  DifferentialDrive robotDrive = new DifferentialDrive(leftSide, rightSide); 
  

  @Override
  public void robotInit() {
    // rightMotor.setInverted(true); 
  }


  @Override
  public void robotPeriodic() {}


  @Override
  public void autonomousInit() {
    timer.start();
  }


  @Override
  public void autonomousPeriodic() {
    if(timer.get() <= 2) {
      robotDrive.arcadeDrive(.0, 0); // drive straight
    }
  }


  @Override
  public void teleopInit() {}


  @Override
  public void teleopPeriodic() {
    // up/down drives forward/back, left/right rotates left/right
    robotDrive.arcadeDrive(xboxCont.getRawAxis(1), xboxCont.getRawAxis(0)); 
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
