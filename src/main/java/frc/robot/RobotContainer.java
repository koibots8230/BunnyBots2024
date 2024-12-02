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
import frc.robot.Subsystems.Shooter;
import monologue.Logged;
import monologue.Monologue;

public class RobotContainer implements Logged {
    private final XboxController controller;
    private final Shooter shooter;
  public RobotContainer() {
    controller = new XboxController(0);
    shooter = new Shooter();

    configureBindings();

    Monologue.setupMonologue(this, "Robot", false, false);
  }

  private void configureBindings() {
    Trigger shootTest = new Trigger(controller::getXButton);
    shootTest.onTrue(shooter.shootCommand(ShooterConstants.INTAKE_SPEED.in(RPM), ShooterConstants.INTAKE_SPEED.in(RPM)));
    shootTest.onFalse(shooter.shootCommand(0, 0));
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
