package frc.robot.subsystem;

import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.SmartDashboardNumber;

public class Shooter extends SubsystemBase{
    private static Shooter instance = null;
    private static boolean usePID = true;

    private SparkFlex shooterMotor = new SparkFlex(0, MotorType.kBrushed);
    private SparkFlex indexMotor = new SparkFlex(0, MotorType.kBrushed);

    private SmartDashboardNumber indexSpeed = new SmartDashboardNumber("index speed", 0.3);
    private SmartDashboardNumber shootSpeed = new SmartDashboardNumber("shoot speed", 400);

    private SmartDashboardNumber kP = new SmartDashboardNumber("shooter/kp", 0.01);
    private SmartDashboardNumber kI = new SmartDashboardNumber("shooter/ki", 0.0);
    private SmartDashboardNumber kD = new SmartDashboardNumber("shooter/kd", 0.0);
    
    
    private SparkClosedLoopController controller;

    private SparkFlexConfig shooterConfigs;

    private Shooter() {
        indexMotor.configure(new SparkFlexConfig().idleMode(IdleMode.kBrake).inverted(false), ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        shooterConfigs = new SparkFlexConfig();
        shooterConfigs.idleMode(IdleMode.kBrake);
        shooterConfigs.inverted(false);

        shooterMotor.configure(shooterConfigs, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        controller = shooterMotor.getClosedLoopController();

    }

    public void startShooter() {
        if (usePID)
            controller.setReference(shootSpeed.getNumber(), ControlType.kVelocity);
        else
            shooterMotor.set(shootSpeed.getNumber() / 6000d);
    }

    public void startIndex() {
        indexMotor.set(indexSpeed.getNumber());
    }

    public void stopShooter() {
        if (usePID) 
            controller.setReference(0, ControlType.kVelocity);
        else
            shooterMotor.set(0);
    }

    public void stopIndex() {
        indexMotor.set(0);
    }

    public void stop() {
        stopShooter();
        stopIndex();
    }

    public boolean atTargetVelocity() {
        return 
    }

    public Command shootCommand() {
        return Commands.sequence(
            Commands.runOnce(() -> this.startShooter(), this),
            Commands.waitUntil(() -> this.atTargetVelocity()),
            Commands.runOnce(() -> this.startIndex(), this)
        );
    }

    public Command stopCommand() {
        return this.runOnce(() -> this.stop());
    }

    @Override
    public void periodic() {
        
    }

    public static Shooter getInstance() {
        if (instance == null) instance = new Shooter();
        return instance;
    }
}
