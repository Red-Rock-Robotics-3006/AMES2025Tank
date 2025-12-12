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

    public static final double kAutoShootWaitTime = 1;
    public static final double kAutoDriveLeaveTime = 5;

    public static Command shootLeavAuto() {
        return Commands.sequence(
            shooter.shootCommand(),
            Commands.waitSeconds(kAutoShootWaitTime),
            shooter.stopCommand(),
            Commands.runOnce(() -> drivetrain.driveAuto(), drivetrain),
            Commands.waitSeconds(kAutoDriveLeaveTime),
            Commands.runOnce(() -> drivetrain.driveRaw(0, 0), drivetrain)
        );
    }

    public static Command emptyAuto() {
        return Commands.print("good luck drivers");
    }
}
