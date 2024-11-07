package com.koibots.robot.Subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.koibots.robot.Constants;

import edu.wpi.first.cscore.VideoException;
import edu.wpi.first.wpilibj.motorcontrol.Victor;
import edu.wpi.first.wpilibj.motorcontrol.VictorSP;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class TankDrive extends SubsystemBase {

    public static TankDrive Tank_Drive = new TankDrive();
    private final CANSparkMax rightMainMotor;
    private final CANSparkMax leftMainMotor;
    private final VictorSP leftBackMotor = new VictorSP(0);
    private final VictorSP leftFrontMotor = new VictorSP(1);
    private final VictorSP rightBackMotor = new VictorSP(0);
    private final VictorSP rightFrontMotor = new VictorSP(1);


    public TankDrive(){
        rightMainMotor = new CANSparkMax(Constants.TankDriveConstants.RIGHT_MOTOR, MotorType.kBrushless);
        leftMainMotor = new CANSparkMax(Constants.TankDriveConstants.LEFT_MOTOR, MotorType.kBrushless);
        
        //check to see if these motors need to be inverted!

    
    }

    public static TankDrive get() {
        return Tank_Drive;          //returns the subsystem, not the class.
    }

    public void setRightSpeed(double speed){
        rightMainMotor.set(speed);

    }

    public void setLeftSpeed(double speed){
        leftMainMotor.set(speed);
    }

    @Override
    public void periodic(){
        leftBackMotor.setVoltage(leftMainMotor.getAppliedOutput()*leftMainMotor.getBusVoltage());
        rightBackMotor.setVoltage(rightMainMotor.getAppliedOutput()*rightMainMotor.getBusVoltage());
    }
    
}
