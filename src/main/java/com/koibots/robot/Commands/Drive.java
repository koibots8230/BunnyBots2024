package com.koibots.robot.Commands;

import com.koibots.robot.Subsystems.TankDrive;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class Drive extends CommandBase{

    public Drive(){
        addRequirements(TankDrive.Tank_Drive);
         //tank_drive is public to make this work. will the bot explode??
    }

    public void init(double leftSpeed, double rightSpeed) {
        TankDrive.Tank_Drive.setLeftSpeed(leftSpeed);
        TankDrive.Tank_Drive.setRightSpeed(rightSpeed);
    }
    
}
