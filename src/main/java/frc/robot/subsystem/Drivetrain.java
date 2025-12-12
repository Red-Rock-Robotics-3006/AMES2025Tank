package frc.robot.subsystem;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.SmartDashboardNumber;

public class Drivetrain extends SubsystemBase{
    private static Drivetrain instance = null;

    private SparkMax fl = new SparkMax(0, MotorType.kBrushless);
    private SparkMax fr = new SparkMax(0, MotorType.kBrushless);
    private SparkMax bl = new SparkMax(0, MotorType.kBrushless);
    private SparkMax br = new SparkMax(0, MotorType.kBrushless);

    private SmartDashboardNumber maxDrive = new SmartDashboardNumber("dt/max drive", 1);
    private SmartDashboardNumber maxTurn = new SmartDashboardNumber("dt/max turn", 0.5);

    public SmartDashboardNumber autoDriveSpeed = new SmartDashboardNumber("auto/drive speed", 0.3);
    public SmartDashboardNumber autoTurnSpeed = new SmartDashboardNumber("auto/turn speed", 0);

    private Drivetrain() {
        SparkMaxConfig leftConfig = new SparkMaxConfig();
        leftConfig.idleMode(IdleMode.kBrake).inverted(true);
        SparkMaxConfig rightConfig = new SparkMaxConfig();
        rightConfig.idleMode(IdleMode.kBrake).inverted(false);

        fl.configure(leftConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        bl.configure(leftConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        fr.configure(rightConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        br.configure(rightConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    public void drive(double drive, double turn) {
        this.driveRaw(drive * maxDrive.getNumber(), turn * maxTurn.getNumber());
    }

    public void driveRaw(double drive, double turn) {
        fl.set(drive + turn);
        bl.set(drive + turn);
        fr.set(drive - turn);
        br.set(drive - turn);
    }

    public void driveAuto() {
        this.driveRaw(autoDriveSpeed.getNumber(), autoTurnSpeed.getNumber());
    }
    
    public static Drivetrain getInstance() {
        if (instance == null) instance = new Drivetrain();
        return instance;
    }
}
