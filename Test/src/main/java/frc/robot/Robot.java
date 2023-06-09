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
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
// import com.revrobotics.SparkMaxRelativeEncoder;
// import com.revrobotics.SparkMaxAlternateEncoder;
import com.revrobotics.SparkMaxAlternateEncoder.Type;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
// import com.revrobotics.SparkMaxRelativeEncoder.Type;
// import com.revrobotics.RelativeEncoder;

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
  CANSparkMax leftMotorFront = new CANSparkMax(10, MotorType.kBrushless); 
  CANSparkMax leftMotorBack = new CANSparkMax(2, MotorType.kBrushless);
  CANSparkMax rightMotorFront = new CANSparkMax(3, MotorType.kBrushless);
  CANSparkMax rightMotorBack = new CANSparkMax(4, MotorType.kBrushless);

  // CANSparkMax Encoders
  RelativeEncoder leftEncoderFront = leftMotorFront.getAlternateEncoder(Type.kQuadrature, neoTicksPerRev);
  RelativeEncoder leftEncoderBack = leftMotorBack.getAlternateEncoder(Type.kQuadrature, 42);
  RelativeEncoder rightEncoderFront = rightMotorFront.getAlternateEncoder(Type.kQuadrature, 42);
  RelativeEncoder rightEncoderBack = rightMotorBack.getAlternateEncoder(Type.kQuadrature, 42);

  // Controllers
  XboxController xboxCont = new XboxController(0);

  // Motor groups
  MotorControllerGroup leftSide = new MotorControllerGroup(leftMotorFront, leftMotorBack);
  MotorControllerGroup rightSide = new MotorControllerGroup(rightMotorFront, rightMotorBack);

  // Differential drive
  DifferentialDrive robotDrive = new DifferentialDrive(leftSide, rightSide); 

  // PIDControllers + values
  double kpDrive = 0;
  double kiDrive = 0;
  double kdDrive = 0;
  PIDController pidController = new PIDController(kpDrive, kiDrive, kdDrive);
  
  // Circumference of Wheel
  double wheelSizeMetres = 8 * Math.PI / 39.37; // 8 inches

  double distancePerRev = wheelSizeMetres; // one rev equals this distance
  double revsForOneMetre = 1 / distancePerRev; // 1 metre divided by rev distance equals 1 metre per required amount of revs
  double ticksForOneMetre = revsForOneMetre / neoTicksPerRev; 

  // Circumference to rotate one 360
  double one360 = 24 * Math.PI / 39.37; 

  // Setpoint for PID 
  double setpoint = 0; // in metres
  double errorSum = 0; // for integral
  double lastTimestamp = 0; 

  public void resetPID(){
    pidController.reset(); 
  }

  @Override
  public void robotInit() {
    // rightMotor.setInverted(true); 
    resetPID();
    errorSum = 0;
    timer.start();
    lastTimestamp = timer.get();
  }


  @Override
  public void robotPeriodic() {
    if(xboxCont.getAButton()){
        setpoint = 5;
    }

    // Get position of robot using encoder
    double robotPosition = leftEncoderFront.getCountsPerRevolution() * ticksForOneMetre;

    // calculations
    double errorDistance = setpoint - robotPosition; // distance left to travel to setpoint
    double dTime = timer.get() - lastTimestamp;
    double outputSpeed = kpDrive * errorDistance; // change speed of robot depending on how fast to drive and left over distance

    errorSum += errorDistance * dTime;

    // drive motors
    robotDrive.arcadeDrive(outputSpeed, -outputSpeed);

    // display values
    SmartDashboard.putNumber("encoder value", leftEncoderFront.getCountsPerRevolution() * ticksForOneMetre);
  }


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
    robotDrive.arcadeDrive(xboxCont.getRawAxis(1), -xboxCont.getRawAxis(0)); 
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
