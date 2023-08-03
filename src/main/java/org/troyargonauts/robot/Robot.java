// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.troyargonauts.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.util.datalog.BooleanLogEntry;
import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.util.datalog.StringLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import org.troyargonauts.robot.auton.routines.*;
import org.troyargonauts.robot.commands.StartingSequence;
import org.troyargonauts.robot.subsystems.*;

import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.Timer.*;

/**
 * The VM is configured to automatically run this class, and to call the methods corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
    private static DriveTrain driveTrain;
    private static Arm arm;
    private static Elevator elevator;
    private static Wrist wrist;
    private static LEDSystem ledSystem;
    private final SendableChooser<Command> chooser = new SendableChooser<>();
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private boolean hasArmLimitBeenPressed = false;
    private boolean hasWristLimitBeenPressed = false;
    private Command autonomousCommand;

    private static DataLogging dataLogger;

    public static LEDSystem getLEDSystem() {
        if (ledSystem == null) ledSystem = new LEDSystem();
        return ledSystem;
    }

    @Override
    public void robotInit() {
        LiveWindow.disableAllTelemetry();
        LiveWindow.setEnabled(false);

        dataLogger = new DataLogging();
        driveTrain = new DriveTrain();
        arm = new Arm();
        elevator = new Elevator();
        wrist = new Wrist();
        new RobotContainer();

        resetAllEncoders();

        CameraServer.startAutomaticCapture().setFPS(14);

        SmartDashboard.putData("Autonomous modes", chooser);
        chooser.addOption("Nothing", new WaitCommand(15));
        chooser.addOption("Homing Sequence", new StartingSequence().withTimeout(15));
        chooser.addOption("Home and Drive", new HomeDrive().withTimeout(15));
        chooser.addOption("Drive Out", new DriveOut().withTimeout(15));
        chooser.addOption("Drive Hybrid", new DriveHybrid().withTimeout(15));
        chooser.addOption("High Mobility", new HighMobility().withTimeout(15));
        chooser.addOption("Hybrid Mobility", new HybridMobility().withTimeout(15));
        chooser.addOption("Mid Mobility", new MidMobiltity().withTimeout(15));
        chooser.setDefaultOption("Auto Balance", new Balance().withTimeout(15));
        chooser.setDefaultOption("Mid Balance Cone", new MidBalance(GamePiece.CONE).withTimeout(15));
        chooser.setDefaultOption("Mid Balance Cube", new MidBalance(GamePiece.CUBE).withTimeout(15));
        chooser.setDefaultOption("High Balance", new HighBalance().withTimeout(15));

        arm.setDesiredTarget(Arm.ArmState.HOME);
        wrist.setDesiredTarget(Wrist.WristState.HOME);
        elevator.setDesiredTarget(Elevator.ElevatorState.HOME);

        scheduledExecutorService.scheduleAtFixedRate(() -> {
            elevator.run();

            if (arm.isHomeLimitPressed()) {
                hasArmLimitBeenPressed = true;
            }

            if (hasArmLimitBeenPressed) {
                arm.run();
            }

            if (wrist.getDownLimitSwitch()) {
                hasWristLimitBeenPressed = true;
            }

            if (hasWristLimitBeenPressed) {
                wrist.run();
            }
        }, 100, 10, TimeUnit.MILLISECONDS);

        DataLogManager.start();
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
        //double[] yawPitchRoll = new double[3];
        //Robot.getDataLogger().getPigeon().getYawPitchRoll(yawPitchRoll);
        //double angleData = Robot.getDrivetrain().getAngle();
    }

    @Override
    public void teleopPeriodic() {
    }

    @Override
    public void disabledInit() {
//        getDrivetrain().getDualSpeedTransmission().setGear(DualSpeedTransmission.Gear.HIGH);
//        arm.setDesiredTarget(Arm.ArmState.HOME);
//        wrist.setDesiredTarget(Wrist.WristState.HOME);
//        elevator.setDesiredTarget(Elevator.ElevatorState.HOME);
//        dataLogger.close();
    }

    @Override
    public void autonomousInit() {
        getDrivetrain().getDualSpeedTransmission().setGear(DualSpeedTransmission.Gear.LOW);
        resetAllEncoders();

        autonomousCommand = chooser.getSelected();
        if (autonomousCommand != null) {
            autonomousCommand.schedule();
        }
    }

    @Override
    public void teleopInit() {
        if (autonomousCommand != null) {
            autonomousCommand.cancel();
        }
        getDrivetrain().getDualSpeedTransmission().setGear(DualSpeedTransmission.Gear.LOW);
    }

    @Override
    public void testInit() {
        // Cancels all running commands at the start of test mode.
        CommandScheduler.getInstance().cancelAll();
    }


    /**
     * Returns driveTrain object
     *
     * @return DriveTrain object instantiated in Robot class
     */
    public static DriveTrain getDrivetrain() {
        if (driveTrain == null) driveTrain = new DriveTrain();
        return driveTrain;
    }

    public static Arm getArm() {
        if (arm == null) arm = new Arm();
        return arm;
    }

    public static Wrist getWrist() {
        if (wrist == null) wrist = new Wrist();
        return wrist;
    }

    public static Elevator getElevator() {
        if (elevator == null) elevator = new Elevator();
        return elevator;
    }

    public static DataLogging getDataLogger(){
        if(dataLogger == null) dataLogger = new DataLogging();
        return dataLogger;
    }

    public void resetAllEncoders() {
        driveTrain.resetEncoders();
        elevator.resetEncoders();
        arm.resetEncoders();
        wrist.resetEncoders();
    }
}
