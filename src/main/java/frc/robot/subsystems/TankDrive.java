package frc.robot.Subsystems;

import static edu.wpi.first.units.Units.*;

import edu.wpi.first.wpilibj.motorcontrol.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;
import monologue.Logged;
import monologue.Annotations.Log;

import java.util.function.DoubleSupplier;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkLowLevel.MotorType;

public class TankDrive extends SubsystemBase implements Logged {
    private final CANSparkMax leadLeft;
    private final VictorSP followFrontLeft;
    private final VictorSP followBackLeft;
    
    private final CANSparkMax leadRight;
    private final VictorSP followFrontRight;
    private final VictorSP followBackRight;
    
    private final RelativeEncoder leftEncoder;
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

        leadLeft.setSmartCurrentLimit(DriveConstants.CURRENT_LIMIT);
        leadRight.setSmartCurrentLimit(DriveConstants.CURRENT_LIMIT);

        followFrontLeft = new VictorSP(DriveConstants.FRONT_LEFT_ID);
        followBackLeft = new VictorSP(DriveConstants.BACK_LEFT_ID);
        followFrontRight = new VictorSP(DriveConstants.FRONT_RIGHT_ID);
        followBackRight = new VictorSP(DriveConstants.BACK_RIGHT_ID);

        leftEncoder = leadLeft.getAlternateEncoder(8192);
        rightEncoder = leadRight.getAlternateEncoder(8192);

        leftEncoder.setVelocityConversionFactor(DriveConstants.WHEEL_RADIUS.in(Meters));
        rightEncoder.setVelocityConversionFactor(DriveConstants.WHEEL_RADIUS.in(Meters));

        leftPID = leadLeft.getPIDController();
        rightPID = leadRight.getPIDController();
        
        leftPID.setFeedbackDevice(leftEncoder);
        rightPID.setFeedbackDevice(rightEncoder);

        leftPID.setP(DriveConstants.kP);
        leftPID.setFF(DriveConstants.kV);
        rightPID.setP(DriveConstants.kP);
        rightPID.setFF(DriveConstants.kV);

        SmartDashboard.putBoolean("DriveTest/run", false);
        SmartDashboard.putNumber("DriveTest/speed", 0.0);
        SmartDashboard.putBoolean("DriveTest/leadLeft", false);
        SmartDashboard.putBoolean("DriveTest/followFrontLeft", false);
        SmartDashboard.putBoolean("DriveTest/followBackLeft", false);
        SmartDashboard.putBoolean("DriveTest/leadRight", false);
        SmartDashboard.putBoolean("DriveTest/followFrontRight", false);
        SmartDashboard.putBoolean("DriveTest/followBackRight", false);
    }
    
    @Override
    public void periodic() {
        leftVelocity = leftEncoder.getVelocity();
        rightVelocity = rightEncoder.getVelocity();
        leftVoltage = leadLeft.getAppliedOutput() * leadLeft.getBusVoltage();
        rightVoltage = leadRight.getAppliedOutput() * leadRight.getBusVoltage();
        leadLeftCurrent = leadLeft.getOutputCurrent();
        leadRightCurrent = leadRight.getOutputCurrent();

        followFrontLeft.setVoltage(leftVoltage);
        followBackLeft.setVoltage(leftVoltage);
        followFrontRight.setVoltage(rightVoltage);
        followBackRight.setVoltage(rightVoltage);

        SmartDashboard.putNumber("DriveTest/speed",
                Math.max(-1.0, Math.min(1.0, SmartDashboard.getNumber("DriveTest/speed", 0.0)))
        );
    }

    @Override
    public void simulationPeriodic() {
        leftVelocity = leftSetpoint;
        rightVelocity = rightSetpoint;
    }

    private void tankDrive(double left, double right) {
        leftPID.setReference(left * DriveConstants.MAX_SPEED.in(MetersPerSecond), ControlType.kVelocity);
        rightPID.setReference(right * DriveConstants.MAX_SPEED.in(MetersPerSecond), ControlType.kVelocity);
        leftSetpoint = left * DriveConstants.MAX_SPEED.in(MetersPerSecond);
        rightSetpoint = left * DriveConstants.MAX_SPEED.in(MetersPerSecond);
    }
    
    public Command driveCommand(DoubleSupplier joysstickLeft, DoubleSupplier joystickRight) {
        return Commands.run(
            () -> this.tankDrive(joysstickLeft.getAsDouble(), joystickRight.getAsDouble()),
            this);
    }

    public void testMotorDirectionPeriodic() {
        double speed = 0.0;
        if (SmartDashboard.getBoolean("DriveTest/run", false)) {
            speed = Math.max(-1.0, Math.min(1.0, SmartDashboard.getNumber("DriveTest/speed", 0.0)));
            System.out.println("speed = " + speed);
        }
        rightSetpoint = speed;
        leftSetpoint = speed;

        leadLeft.set(SmartDashboard.getBoolean(
                "DriveTest/leadLeft", false) ? speed : 0.0);
        followFrontLeft.set(SmartDashboard.getBoolean(
                "DriveTest/followFrontLeft", false) ? speed : 0.0);
        followBackLeft.set(SmartDashboard.getBoolean(
                "DriveTest/followBackLeft", false) ? speed : 0.0);
        leadRight.set(SmartDashboard.getBoolean(
                "DriveTest/leadRight", false) ? speed : 0.0);
        followFrontRight.set(SmartDashboard.getBoolean(
                "DriveTest/followFrontRight", false) ? speed : 0.0);
        followBackRight.set(SmartDashboard.getBoolean(
                "DriveTest/followBackRight", false) ? speed : 0.0);
    }

    public void stop() {
        leadLeft.set(0);
        followFrontLeft.set(0);
        followBackLeft.set(0);

        leadRight.set(0);
        followFrontRight.set(0);
        followBackRight.set(0);

        rightSetpoint = 0.0;
        leftSetpoint = 0.0;

        SmartDashboard.putBoolean("DriveTest/run", false);
        SmartDashboard.putBoolean("DriveTest/leadLeft", false);
        SmartDashboard.putBoolean("DriveTest/followFrontLeft", false);
        SmartDashboard.putBoolean("DriveTest/followBackLeft", false);
        SmartDashboard.putBoolean("DriveTest/leadRight", false);
        SmartDashboard.putBoolean("DriveTest/followFrontRight", false);
        SmartDashboard.putBoolean("DriveTest/followBackRight", false);
    }
}