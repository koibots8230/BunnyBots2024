// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.RPM;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.ShooterConstants;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.ShooterPivot;
import frc.robot.subsystems.TankDrive;
import monologue.Logged;
import monologue.Monologue;

public class RobotContainer implements Logged {
    private final XboxController controller;
    private final Shooter shooter;
    private final ShooterPivot pivot;
    private final TankDrive drive;
  public RobotContainer() {
    controller = new XboxController(0);
    shooter = new Shooter();
    pivot = new ShooterPivot();
    drive = new TankDrive();

    configureBindings();

    Monologue.setupMonologue(this, "Robot", false, false);
  }

  private void configureBindings() {
    Trigger intakeTrigger = new Trigger(() -> controller.getLeftTriggerAxis() > 0.1);
    intakeTrigger.onTrue(shooter.shootCommand(-1000,- 1000));
    intakeTrigger.onFalse(shooter.shootCommand(0, 0));

    Trigger shootTrigger = new Trigger(() -> controller.getRightTriggerAxis() > 0.1);
    shootTrigger.onTrue(shooter.shootCommand(1000, 1000));
    shootTrigger.onFalse(shooter.shootCommand(0, 0));

    Trigger shootPosition = new Trigger(controller::getRightBumper);
    shootPosition.onTrue(pivot.movePivotCommand(Math.PI-0.5));

    Trigger intakePosition = new Trigger(controller::getLeftBumper);
    intakePosition.onTrue(pivot.movePivotCommand(0.05));

    drive.setDefaultCommand(drive.driveCommand(controller::getLeftY, controller::getRightY));
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
