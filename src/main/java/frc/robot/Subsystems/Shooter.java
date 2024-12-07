package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ShooterConstants;
import monologue.Logged;
import monologue.Annotations.Log;

public class Shooter extends SubsystemBase implements Logged {
    private CANSparkMax topMotor;
    private CANSparkMax bottomMotor;
    private SparkPIDController topPidController;
    private SparkPIDController bottomPidController;
    private RelativeEncoder encoderBottom;
    private RelativeEncoder encoderTop;

    @Log double setpointBottom;
    @Log double setpointTop;
    @Log double topCurrent;
    @Log double topVoltage;
    @Log double topVelocity;
    @Log double bottomCurrent;
    @Log double bottomVoltage;
    @Log double bottomVelocity;

    public Shooter(){
        topMotor = new CANSparkMax(ShooterConstants.TOP_ID, MotorType.kBrushless);
        bottomMotor = new CANSparkMax(ShooterConstants.BOTTOM_ID, MotorType.kBrushless);
        bottomMotor.setInverted(true);
        topMotor.setSmartCurrentLimit(60);
        bottomMotor.setSmartCurrentLimit(60);
        topPidController = topMotor.getPIDController();
        topPidController.setP(ShooterConstants.PID_kP);
        topPidController.setFF(ShooterConstants.PID_kV);

        bottomPidController = bottomMotor.getPIDController();
        bottomPidController.setP(ShooterConstants.PID_kP);
        bottomPidController.setFF(ShooterConstants.PID_kV);

        encoderBottom = bottomMotor.getEncoder();
        encoderTop = topMotor.getEncoder();
        
    }

    @Override
    public void periodic() {
        topVelocity = encoderTop.getVelocity();
        bottomVelocity = encoderBottom.getVelocity();
        topCurrent = topMotor.getOutputCurrent();
        bottomCurrent = bottomMotor.getOutputCurrent();
        topVoltage = topMotor.getBusVoltage() * topMotor.getAppliedOutput();
        bottomVoltage = bottomMotor.getBusVoltage() * bottomMotor.getAppliedOutput();    
    }

    private void setVelocity(double velocityBottom, double velocityTop){
        setpointBottom = velocityBottom;
        setpointTop = velocityTop;

        topPidController.setReference(velocityTop, ControlType.kVelocity);
        bottomPidController.setReference(velocityBottom, ControlType.kVelocity);
    }

    public Command shootCommand(double velocityTop, double velocityBottom){
        return Commands.runOnce(
            () -> this.setVelocity(velocityBottom, velocityTop), this
        );
    }
}
