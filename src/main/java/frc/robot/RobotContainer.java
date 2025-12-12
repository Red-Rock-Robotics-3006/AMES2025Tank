// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystem.Drivetrain;
import frc.robot.subsystem.Intake;
import frc.robot.subsystem.Shooter;

public class RobotContainer {
  private CommandXboxController controller = new CommandXboxController(0);
  private Drivetrain dt = Drivetrain.getInstance();
  private Intake intake = Intake.getInstance();
  private Shooter shooter = Shooter.getInstance();

  private SendableChooser<Command> autoChooser = new SendableChooser<>();

  public RobotContainer() {
    configureBindings();

    autoChooser.setDefaultOption("Shoot Leave Auto", Autos.shootLeavAuto());
    autoChooser.addOption("No Auto", Autos.emptyAuto());
  }

  private void configureBindings() {
    dt.setDefaultCommand(
      Commands.run(() -> dt.drive(-controller.getLeftY(), controller.getRightX()), dt)
    );

    controller.leftTrigger(0.15).onTrue(
      intake.startIntakeCommand()
    ).onFalse(
      intake.stopCommand()
    );

    controller.rightTrigger(0.15).onTrue(
      shooter.shootCommand()
    ).onFalse(
      shooter.stopCommand()
    );

    controller.leftBumper().onTrue(
      intake.startIntakeRetractionCommand()
    ).onFalse(
      intake.stopCommand()
    );
  }

  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }
}
