// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.troyargonauts.robot;

import static org.troyargonauts.robot.Constants.Controller.*;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import org.troyargonauts.robot.subsystems.Motors;
import org.troyargonauts.robot.subsystems.Pneumatics;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {

    Motors motors = new Motors();
    Pneumatics pneumatics = new Pneumatics();

    CommandXboxController control = new CommandXboxController(CONTROL_ID);

    public RobotContainer() {
        // Configure the trigger bindings
        configureBindings();
    }

    /**
     * Use this method to define your trigger->command mappings.
     */
    private void configureBindings() {
        motors.setDefaultCommand(
                new RunCommand(
                        () -> {
                            motors.set(control.getLeftX());
                        }, motors
                )
        );

        control.a().onTrue(new InstantCommand(pneumatics::forward, pneumatics));
        control.b().onTrue(new InstantCommand(pneumatics::backward, pneumatics));
        control.x().onTrue(new InstantCommand(pneumatics::off, pneumatics));
    }
}