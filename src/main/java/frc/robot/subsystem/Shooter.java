package frc.robot.subsystem;

import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.SmartDashboardNumber;

public class Shooter extends SubsystemBase{
    private static Shooter instance = null;

    private SparkFlex shooterMotor = new SparkFlex(60, MotorType.kBrushless);
    private SparkFlex indexMotor = new SparkFlex(61, MotorType.kBrushless);

    private SmartDashboardNumber indexSpeed = new SmartDashboardNumber("index speed", 0.3);
    private SmartDashboardNumber shootSpeed = new SmartDashboardNumber("shoot speed", 3800);
    private SmartDashboardNumber fartherShootSpeed = new SmartDashboardNumber("far shoot speed", 4515);
    
    private SmartDashboardNumber kP = new SmartDashboardNumber("shooter/kp", 0.0002);
    private SmartDashboardNumber kI = new SmartDashboardNumber("shooter/ki", 0.0);
    private SmartDashboardNumber kD = new SmartDashboardNumber("shooter/kd", 0.0000045);

    private SmartDashboardNumber tolerance = new SmartDashboardNumber("shooter/tolerance", 10);
    
    private boolean usePID = false;

    private PIDController controller;

    private double target = 0;

    private boolean stopped = true;

    private SparkFlexConfig shooterConfigs;

    private Shooter() {
        indexMotor.configure(new SparkFlexConfig().idleMode(IdleMode.kBrake).inverted(false), ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        shooterConfigs = new SparkFlexConfig();
        shooterConfigs.idleMode(IdleMode.kBrake);
        shooterConfigs.inverted(false);

        controller = new PIDController(kP.getNumber(), kI.getNumber(), kD.getNumber());

        shooterMotor.configure(shooterConfigs, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        // controller = shooterMotor.getClosedLoopController();

        SmartDashboard.putBoolean("use pid", true);

    }

    public void startShooter() {
        this.setShooterRPM(shootSpeed.getNumber());
    }

    public void startFarShot() {
        this.setShooterRPM(fartherShootSpeed.getNumber());
    }

    private void setShooterRPM(double rpm) {
        if (usePID)
            // controller.setReference(shootSpeed.getNumber(), ControlType.kVelocity);
            target = rpm;
        else
            shooterMotor.set(rpm / 6000d);
        stopped = false;
    }

    public void startIndex() {
        indexMotor.set(indexSpeed.getNumber());
    }

    public void stopShooter() {
        shooterMotor.set(0);
        stopped = true;
    }

    public void stopIndex() {
        indexMotor.set(0);
    }

    public void stop() {
        stopShooter();
        stopIndex();
    }

    public boolean atTargetVelocity() {
        // return shooterMotor.getEncoder().getVelocity() > tolerance.getNumber() * shootSpeed.getNumber();
        return Double.compare(shooterMotor.getEncoder().getVelocity() + tolerance.getNumber(), target) > 0;
    }

    public Command shootCommand() {
        return Commands.sequence(
            Commands.runOnce(() -> this.startShooter(), this),
            Commands.waitUntil(() -> this.atTargetVelocity()),
            Commands.runOnce(() -> this.startIndex(), this)
        );
    }

    public Command shootFarCommand() {
        return Commands.sequence(
            Commands.runOnce(() -> this.startFarShot(), this),
            Commands.waitUntil(() -> this.atTargetVelocity()),
            Commands.runOnce(() -> this.startIndex(), this)
        );
    }

    public Command stopCommand() {
        return this.runOnce(() -> this.stop());
    }

    @Override
    public void periodic() {
        double currentVelo = shooterMotor.getEncoder().getVelocity();
        SmartDashboard.putNumber("live shooter speed", currentVelo);
        controller.setPID(kP.getNumber(), kI.getNumber(), kD.getNumber());
        double ff = target / 6784;
        double pidVal = controller.calculate(currentVelo, target);
        double val = ff + pidVal;

        SmartDashboard.putNumber("val", val);
        SmartDashboard.putNumber("pid val", pidVal);

        usePID = SmartDashboard.getBoolean("use pid", false);

        if (usePID && !stopped) {
            shooterMotor.set(val);
        } else if (stopped) shooterMotor.set(0);

        SmartDashboard.putBoolean("at velocity", this.atTargetVelocity());
    }

    public static Shooter getInstance() {
        if (instance == null) instance = new Shooter();
        return instance;
    }
}
