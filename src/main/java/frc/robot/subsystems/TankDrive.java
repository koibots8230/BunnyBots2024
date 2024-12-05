package frc.robot.subsystems;

import static edu.wpi.first.units.Units.*;

import edu.wpi.first.wpilibj.motorcontrol.VictorSP;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;
import monologue.Logged;
import monologue.Annotations.Log;

import java.util.function.DoubleSupplier;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

public class TankDrive extends SubsystemBase implements Logged {
    private final CANSparkMax leadLeft;
    private final VictorSP followFrontLeft;
    private final VictorSP followBackLeft;
    
    private final CANSparkMax leadRight;
    private final VictorSP followFrontRight;
    private final VictorSP followBackRight;
    
    private final AbsoluteEncoder leftEncoder;
    private final RelativeEncoder rightEncoder;
    
    private final SparkPIDController leftPID;
    private final SparkPIDController rightPID;

    @Log private double leftVelocity;
    @Log private double leftVoltage;
    @Log private double leadLeftCurrent;
    @Log private double frontLeftCurrent;
    @Log private double backLeftCurrent;

    @Log private double rightVelocity;
    @Log private double rightVoltage;
    @Log private double leadRightCurrent;
    @Log private double frontRightCurrent;
    @Log private double backRightCurrent;

    @Log private double leftSetpoint;
    @Log private double rightSetpoint;

    public TankDrive() {
        leadLeft = new CANSparkMax(DriveConstants.LEAD_LEFT_ID, MotorType.kBrushed);
        leadRight = new CANSparkMax(DriveConstants.LEAD_RIGHT_ID, MotorType.kBrushed); 

        leadLeft.setIdleMode(IdleMode.kBrake);
        leadRight.setIdleMode(IdleMode.kBrake);

        leadLeft.setSmartCurrentLimit(DriveConstants.CURRENT_LIMIT);
        leadRight.setSmartCurrentLimit(DriveConstants.CURRENT_LIMIT);

        followFrontLeft = new VictorSP(DriveConstants.FRONT_LEFT_ID);
        followBackLeft = new VictorSP(DriveConstants.BACK_LEFT_ID);
        followFrontRight = new VictorSP(DriveConstants.FRONT_RIGHT_ID);
        followBackRight = new VictorSP(DriveConstants.BACK_RIGHT_ID);

        followBackLeft.addFollower(followFrontLeft);
        followBackRight.addFollower(followFrontRight);

        leftEncoder = leadLeft.getAbsoluteEncoder();
        rightEncoder = leadRight.getAlternateEncoder(8192);

        leftEncoder.setVelocityConversionFactor((DriveConstants.WHEEL_RADIUS.in(Meters)*2*Math.PI)/60.0);
        rightEncoder.setVelocityConversionFactor(-(DriveConstants.WHEEL_RADIUS.in(Meters)*2*Math.PI)/60.0);

        leftPID = leadLeft.getPIDController();
        rightPID = leadRight.getPIDController();
        
        leftPID.setFeedbackDevice(leftEncoder);
        rightPID.setFeedbackDevice(rightEncoder);

        leftPID.setP(DriveConstants.kP);
        leftPID.setFF(DriveConstants.kV);
        rightPID.setP(DriveConstants.kP);
        rightPID.setFF(DriveConstants.kV);
    }
    
    @Override
    public void periodic() {
        leftVelocity = leftEncoder.getVelocity() * 60;
        rightVelocity = rightEncoder.getVelocity();
        leftVoltage = leadLeft.getAppliedOutput() * leadLeft.getBusVoltage();
        rightVoltage = leadRight.getAppliedOutput() * leadRight.getBusVoltage();
        leadLeftCurrent = leadLeft.getOutputCurrent();
        leadRightCurrent = leadRight.getOutputCurrent();

        followFrontLeft.setVoltage(-leftVoltage);
        followFrontRight.setVoltage(-rightVoltage);
    }

    @Override
    public void simulationPeriodic() {
        leftVelocity = leftSetpoint;
        rightVelocity = rightSetpoint;
    }

    private void tankDrive(double left, double right) {
        leftPID.setReference(-left * DriveConstants.MAX_SPEED.in(MetersPerSecond), ControlType.kVelocity);
        rightPID.setReference(-right * DriveConstants.MAX_SPEED.in(MetersPerSecond), ControlType.kVelocity);
        leftSetpoint = left * DriveConstants.MAX_SPEED.in(MetersPerSecond);
        rightSetpoint = left * DriveConstants.MAX_SPEED.in(MetersPerSecond);
    }
    
    public Command driveCommand(DoubleSupplier joysstickLeft, DoubleSupplier joystickRight) {
        return Commands.run(
            () -> this.tankDrive(joysstickLeft.getAsDouble(), joystickRight.getAsDouble()),
            this);
    }


}