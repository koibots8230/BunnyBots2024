package frc.robot.subsystems;

import frc.robot.Constants;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.trajectory.TrapezoidProfile.State;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.TrapezoidProfileSubsystem;

public class ShooterPivot extends TrapezoidProfileSubsystem {
    //need to put @Log in!

    private final CANSparkMax pivotMotor;
    private final AbsoluteEncoder pivotEncoder;
    private final SparkPIDController pivotPID;
    private final ArmFeedforward pivotFF;

    private double pivotVelocity;
    private double pivotVoltage;
    //TODO: add logging!!!!!!!!!

    public ShooterPivot() {
        super(new TrapezoidProfile.Constraints(Constants.PivotConstants.MAX_VELOCITY, 0));
        pivotMotor = new CANSparkMax(Constants.PivotConstants.PIVOT_MOTOR, MotorType.kBrushless);
        pivotEncoder = pivotMotor.getAbsoluteEncoder();
        pivotPID = pivotMotor.getPIDController();
        pivotFF = new ArmFeedforward(Constants.PivotConstants.FF_ks, Constants.PivotConstants.FF_kg, Constants.PivotConstants.FF_kv);

        pivotEncoder.setVelocityConversionFactor(Constants.PivotConstants.Conversion);
        //TODO: conversion needs to be a whole thing. probably an equation passed into this^^^
        pivotPID.setFeedbackDevice(pivotEncoder);
        pivotPID.setP(Constants.PivotConstants.PID_kP);
    }

    @Override
    public void useState(State state) {
        pivotVelocity = pivotEncoder.getVelocity();
        pivotVoltage = pivotMotor.getBusVoltage() * pivotMotor.getAppliedOutput();

        pivotPID.setReference(state.position, ControlType.kPosition, 0, pivotFF.calculate(state.position, state.velocity));
    }   

    private void movePivot(double position) {
        this.setGoal(new State(position, 0));
    }

    public Command movePivotCommand(double position){
        return Commands.runOnce(() -> this.movePivot(position), this);
    }
}
