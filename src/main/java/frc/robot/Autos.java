package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystem.Drivetrain;
import frc.robot.subsystem.Intake;
import frc.robot.subsystem.Shooter;

public class Autos {
    private static Intake intake = Intake.getInstance();
    private static Shooter shooter = Shooter.getInstance();
    private static Drivetrain drivetrain = Drivetrain.getInstance();

    public static final double kAutoShootWaitTime = 3;
    public static final double kAutoDriveLeaveTime = 5;
    public static final double kAutoDriveBackTime = 1;

    public static Command shootLeavAuto() {
        return Commands.sequence(
            // Commands.runOnce(() -> drivetrain.driveAuto(), drivetrain),
            // Commands.waitSeconds(kAutoDriveBackTime),
            // Commands.runOnce(() -> drivetrain.driveRaw(0, 0), drivetrain),
            intake.startIntakeCommand(),
            shooter.shootCommand(),
            Commands.waitSeconds(kAutoShootWaitTime),
            shooter.stopCommand(),
            intake.stopCommand(),
            Commands.runOnce(() -> drivetrain.driveAutoRevers(), drivetrain),
            Commands.waitSeconds(kAutoDriveLeaveTime),
            Commands.runOnce(() -> drivetrain.driveRaw(0, 0), drivetrain)
        );
    }

    public static Command farLeaveAuto() {
        return Commands.sequence(
            intake.startSecondaryIntakeSpeedCommand(),
            shooter.shootFarCommand(),
            Commands.waitSeconds(kAutoShootWaitTime),
            shooter.stopCommand(),
            intake.stopCommand(),
            Commands.runOnce(() -> drivetrain.driveAutoRevers(), drivetrain),
            Commands.waitSeconds(kAutoDriveLeaveTime),
            Commands.runOnce(() -> drivetrain.driveRaw(0, 0), drivetrain)
        );
    }

    public static Command leaveAuto() {
        return Commands.sequence(
            Commands.runOnce(() -> drivetrain.driveAuto(), drivetrain),
            Commands.waitSeconds(kAutoDriveLeaveTime),
            Commands.runOnce(() -> drivetrain.driveRaw(0, 0), drivetrain)
        );
    }

    public static Command emptyAuto() {
        return Commands.print("good luck drivers");
    }
}
