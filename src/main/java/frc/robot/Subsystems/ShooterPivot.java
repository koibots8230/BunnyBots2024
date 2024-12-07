package frc.robot.subsystems;

import frc.robot.Constants;
import frc.robot.Constants.PivotConstants;
import monologue.Logged;
import monologue.Annotations.Log;

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

public class ShooterPivot extends TrapezoidProfileSubsystem implements Logged {
    //need to put @Log in!

    private final CANSparkMax pivotMotor;
    private final AbsoluteEncoder pivotEncoder;
    private final SparkPIDController pivotPID;
    private final ArmFeedforward pivotFF;
    
    
    @Log private double position;
    @Log private double pivotVelocity;
    @Log private double pivotVoltage;
    //TODO: add logging!!!!!!!!!

    public ShooterPivot() {
        super(new TrapezoidProfile.Constraints(Constants.PivotConstants.MAX_VELOCITY, PivotConstants.MAX_ACCELERATION));
        pivotMotor = new CANSparkMax(Constants.PivotConstants.PIVOT_MOTOR, MotorType.kBrushless);
        pivotMotor.setInverted(true);
        pivotEncoder = pivotMotor.getAbsoluteEncoder();
        pivotPID = pivotMotor.getPIDController();
        pivotFF = new ArmFeedforward(Constants.PivotConstants.FF_ks, Constants.PivotConstants.FF_kg, Constants.PivotConstants.FF_kv);

        pivotEncoder.setVelocityConversionFactor(Constants.PivotConstants.Conversion);
        //TODO: conversion needs to be a whole thing. probably an equation passed into this
        pivotPID.setFeedbackDevice(pivotEncoder);
        pivotPID.setP(Constants.PivotConstants.PID_kP);

        pivotPID.setPositionPIDWrappingEnabled(true);
        pivotPID.setPositionPIDWrappingMaxInput(Math.PI * (3.0/2.0));
        pivotPID.setPositionPIDWrappingMinInput(0);
        pivotEncoder.setPositionConversionFactor(Constants.PivotConstants.Conversion);
    }

    @Override
    public void useState(State state) {
        pivotVelocity = pivotEncoder.getVelocity();
        pivotVoltage = pivotFF.calculate(state.position, state.velocity);
        position = pivotEncoder.getPosition();

        //System.out.println(state.position);
        //:)
        pivotPID.setReference(state.position, ControlType.kPosition, 0, pivotFF.calculate(state.position, state.velocity));
    }   

    private void movePivot(double position) {
        this.setGoal(new State(position, 0));
    }

    public Command movePivotCommand(double position){
        return Commands.runOnce(() -> this.movePivot(position), this);
    }
}
