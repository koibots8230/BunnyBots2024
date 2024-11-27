package com.koibots.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class shooter extends SubsystemBase {
    CANSparkMax shooterMotorL;
    CANSparkMax shooterMotorR;
    SparkMaxPIDController pidController;
    RelativeEncoder encoderR;
    RelativeEncoder encoderL;


    double shooterSetpointR;
    double shooterSetpointL;

    public shooter(){
        shooterMotorL = new CANSparkMax(1, MotorType.kBrushless);
        shooterMotorR = new CANSparkMax(2, MotorType.kBrushless);
        shooterMotorR.setInverted(true);
        pidController = shooterMotorL.getPIDController();

        shooterSetpointR = 0;
        shooterSetpointL = 0;


        encoderR = shooterMotorR.getEncoder();
        encoderL = shooterMotorL.getEncoder();
        
    }

    @Override
    public void periodic() {
        if (encoderL.getVelocity() == shooterSetpointL) {
            shooterMotorL.set(shooterSetpointL);
        } else {
            shooterMotorL.set(0);
        }
        if (encoderR.getVelocity() == shooterSetpointR) {
            shooterMotorR.set(shooterSetpointR);
        } else {
            shooterMotorR.set(0);  
        }

        
    }

    private void setVelocity(double velocityL, double velocityR){
        shooterSetpointL = velocityL;
        shooterSetpointR = velocityR;
    }

    public Command shootCommand(){
        return Commands.runOnce(
            () -> this.setVelocity(.2, .2), this
        );
    }
}
