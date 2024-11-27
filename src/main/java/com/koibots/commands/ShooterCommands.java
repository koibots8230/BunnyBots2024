package com.koibots.commands;

import com.koibots.subsystems.shooter;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class ShooterCommands extends CommandBase{
    public static Command ShootCommand(shooter shooter, double rVelocity, double lVelocity){
        return shooter.shootCommand();
    }
}
