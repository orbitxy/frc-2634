// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.lang.Math;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxRelativeEncoder.Type;
// import com.revrobotics.SparkMaxRelativeEncoder;
import com.revrobotics.RelativeEncoder;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  // NEO Ticks per Rev
  int neoTicksPerRev = 42;
  
  // Timer
  Timer timer = new Timer();

  // CANSparkMax Motor Controllers
  CANSparkMax leftMotorFront = new CANSparkMax(1, MotorType.kBrushless); 
  CANSparkMax leftMotorBack = new CANSparkMax(2, MotorType.kBrushless);
  CANSparkMax rightMotorFront = new CANSparkMax(3, MotorType.kBrushless);
  CANSparkMax rightMotorBack = new CANSparkMax(4, MotorType.kBrushless);

  // CANSparkMax Encoders
  RelativeEncoder leftEncoderFront = leftMotorFront.getEncoder(Type.kQuadrature, 42);
  RelativeEncoder leftEncoderBack = leftMotorBack.getEncoder(Type.kQuadrature, 42);
  RelativeEncoder rightEncoderFront = rightMotorFront.getEncoder(Type.kQuadrature, 42);
  RelativeEncoder rightEncoderBack = rightMotorBack.getEncoder(Type.kQuadrature, 42);

  // Controllers
  XboxController xboxCont = new XboxController(0);

  // Motor groups
  MotorControllerGroup leftSide = new MotorControllerGroup(leftMotorFront, leftMotorBack);
  MotorControllerGroup rightSide = new MotorControllerGroup(rightMotorFront, rightMotorBack);

  // Differential drive
  DifferentialDrive robotDrive = new DifferentialDrive(leftSide, rightSide); 
  
  // Circumference of Wheel
  double wheelDiamInches = 8; 
  double wheelSizeInches = wheelDiamInches * Math.PI; 
  double wheelSizeMetres = wheelSizeInches / 39.37;

  double distancePerRev = wheelSizeMetres; // one rev equals this distance
  double revsForOneMetre = 1 / distancePerRev; // 1 metre divided by rev distance equals 1 metre per required amount of revs
  double ticksForOneMetre = revsForOneMetre / neoTicksPerRev; 

  @Override
  public void robotInit() {
    // rightMotor.setInverted(true); 
  }


  @Override
  public void robotPeriodic() {}


  @Override
  public void autonomousInit() {

  }


  @Override
  public void autonomousPeriodic() {
      // drive one meter forward using amount of ticks passed
      if(leftEncoderFront.getCountsPerRevolution() < ticksForOneMetre){
        robotDrive.arcadeDrive(0.5, 0.5);
      }

      // drive one meter forward using amount of rotations passed 
      if(leftEncoderFront.getPosition() < revsForOneMetre){
        robotDrive.arcadeDrive(0.5, 0.5);
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
