package com.team254.frc2014.auto;

import com.team254.frc2014.FieldPosition;
import com.team254.frc2014.ConfigurationAutoMode;
import com.team254.frc2014.paths.AutoPaths;
import com.team254.lib.trajectory.Path;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author Mani Gnanasivam
 * @author EJ Sebathia
 */
public class AerialAssistAuto extends ConfigurationAutoMode {

  static Path path = AutoPaths.get("CenterLanePath");
  
  public AerialAssistAuto() {
    super("Default auto mode");
  }
  
  protected void routine() {
    
    boolean goLeft = config.lane == MIDDLE_LANE;//(Math.floor(Timer.getFPGATimestamp()) % 2 == 0);
    Timer t = new Timer();
    t.start();
 
    
    // Turn on wheel
    shooterController.setVelocityGoal(4000);
    
    // Grab balls from ground
    clapper.wantFront = false;
    clapper.wantRear = false;
    frontIntake.wantBumperGather = true;
    rearIntake.wantBumperGather = true;
    waitTime(.5);
    

    // Drive to correct place
    if (goLeft)
      path.goLeft();
    else
      path.goRight();
    drivePath(path, 10);
   
    // Hold position
    drivebase.resetEncoders();
    System.out.println("Drive time: " + t.get());
    headingController.setDistance(0);
    headingController.setHeading(Math.toDegrees(Math.PI/6.0) * (goLeft ? 1.0 : -1.0));
    drivebase.useController(headingController);
    
    // Wait for 5 seconds in
    waitTime(.5);
    rearIntake.wantShoot = frontIntake.wantShoot = true;
    waitTime(.6);
    
    
    System.out.println("Shooting 1st ball at: " + t.get());
    // Shoot first ball
    clapper.wantShot = true;
    waitTime(.5);
    clapper.wantShot = false;
    rearIntake.wantShoot = frontIntake.wantShoot = false;
    
    // Speed up for 2nd and 3rd shots
   shooterController.setVelocityGoal(4300);
    
    // Queue 2nd ball
    rearIntake.wantBumperGather = false;
    rearIntake.wantDown = true;
    rearIntake.setManualRollerPower(1.0);
    waitTime(0.3);
    rearIntake.wantDown = false;
    waitTime(.4);
    rearIntake.setManualRollerPower(0);
    
    // Settle time
    waitTime(.75);
    
    // Shoot second ball
    frontIntake.wantShoot = true;
    waitTime(.25);
    rearIntake.setManualRollerPower(1);
    clapper.wantShot = true;
    waitTime(.5);
    clapper.wantShot = false;
    rearIntake.setManualRollerPower(0);
    waitTime(0.3);
    frontIntake.wantShoot = false;
    
    // Queue 3rd ball
    frontIntake.wantBumperGather = false;
    frontIntake.wantDown = true;
    frontIntake.setManualRollerPower(0.5);
    waitTime(0.3);
    frontIntake.wantDown = false;
    waitTime(0.5);
    frontIntake.setManualRollerPower(0.00);

    
    // Settle time
    waitTime(1.0);
    
    // Shoot thirdball
    rearIntake.setManualRollerPower(1);
    clapper.wantShot = true;
    waitTime(.5);
    clapper.wantShot = false;
    
    // Print out time
    System.out.println("Auto done at: " + t.get());
    
    // Clean up
    rearIntake.setManualRollerPower(0);
    shooterController.setVelocityGoal(0);
  }

  public FieldPosition getFieldPosition() {
    return FieldPosition.centeredOnLine;
  }
}