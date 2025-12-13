package frc.robot.subsystem;

import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.SmartDashboardNumber;

public class Intake extends SubsystemBase{
    private static Intake instance = null;
    private SparkFlex intakeMotor = new SparkFlex(17, MotorType.kBrushless);

    private SmartDashboardNumber intakeSpeed = new SmartDashboardNumber("intake/intake speed", 0.4);
    private SmartDashboardNumber secondaryIntakeSpeed = new SmartDashboardNumber("intake/secondary intake speed", 0.3);
    private SmartDashboardNumber retractSpeed = new SmartDashboardNumber("intake/intake retract speed", -0.2);

    private Intake() {
        SparkFlexConfig config = new SparkFlexConfig();
        config.idleMode(IdleMode.kBrake).inverted(false);
    }

    public void startSecondaryIntakeSpeed() {
        intakeMotor.set(secondaryIntakeSpeed.getNumber());
    }

    public void startIntake() {
        intakeMotor.set(intakeSpeed.getNumber());
    }

    public void stop() {
        intakeMotor.set(0);
    }

    public void retractIntake() {
        intakeMotor.set(retractSpeed.getNumber());
    }

    public Command startSecondaryIntakeSpeedCommand() {
        return this.runOnce(() -> this.startSecondaryIntakeSpeed());
    }

    public Command startIntakeCommand() {
        return this.runOnce(() -> this.startIntake());
    }

    public Command stopCommand() {
        return this.runOnce(() -> this.stop());
    }

    public Command startIntakeRetractionCommand() {
        return this.runOnce(() -> this.retractIntake());
    }

    public static Intake getInstance() {
        if (instance == null) instance = new Intake();
        return instance;
    }
}
