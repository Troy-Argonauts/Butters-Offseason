package org.troyargonauts.robot.subsystems;

import static org.troyargonauts.robot.Constants.Pneumatics.*;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.troyargonauts.robot.RobotContainer;

public class Pneumatics extends SubsystemBase {
    DoubleSolenoid solenoid;
    Compressor compressor;

    public Pneumatics() {
        solenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, FORWARD_CHANNEL_ID, REVERSE_CHANNEL_ID);
        solenoid.set(DoubleSolenoid.Value.kOff);

        compressor = new Compressor(PneumaticsModuleType.CTREPCM);
        compressor.enableDigital();
    }

    @Override
    public void periodic() {
        SmartDashboard.putString("Pneumatics State", getState().toString());
    }

    public void setState(DoubleSolenoid.Value value) {
        solenoid.set(value);
    }

    public DoubleSolenoid.Value getState() {
        return solenoid.get();
    }
}
