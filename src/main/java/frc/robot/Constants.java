package frc.robot;

import static edu.wpi.first.units.Units.*;

import edu.wpi.first.units.Angle;
import edu.wpi.first.units.Distance;
import edu.wpi.first.units.Measure;
import edu.wpi.first.units.Velocity;

public class Constants {
    public class DriveConstants {
        public static final Measure<Velocity<Distance>> MAX_SPEED = MetersPerSecond.of(3);

        public static final double kP = 0;
        public static final double kV = 0;

        public static final int CURRENT_LIMIT = 60;

        public static final Measure<Distance> WHEEL_RADIUS = Inches.of(2);

        public static final int LEAD_LEFT_ID = 1;
        public static final int LEAD_RIGHT_ID = 2;
        public static final int FRONT_LEFT_ID = 3;
        public static final int FRONT_RIGHT_ID = 4;
        public static final int BACK_LEFT_ID = 5;
        public static final int BACK_RIGHT_ID = 6;
    }

    public class ShooterConstants{
        public static final Measure<Velocity<Angle>> INTAKE_SPEED = RPM.of(1500);
        public static final Measure<Velocity<Angle>> SHOOT_SPEED = RPM.of(1500);

        public static final double PID_kP = 0;
        public static final double PID_kV = 0;

        public static final int TOP_ID = 9;
        public static final int BOTTOM_ID = 8;
    }
  
    public class PivotConstants{ //TODO: put in actual Constants values
        public static final int PIVOT_MOTOR = 7;
        public static final int Conversion = 1;
        public static final double PID_kP = 0.01;
        public static final double PID_kV = 0.01;
        public static final double FF_ks = 0.01;
        public static final double FF_kg = 9.81;
        public static final double FF_kv = 0.01;
        public static final double MAX_VELOCITY = Math.PI/2.0;
        public static final double MAX_ACCELERATION = Math.PI;
    }
    
}