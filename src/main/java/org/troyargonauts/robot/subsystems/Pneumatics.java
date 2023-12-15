package org.troyargonauts.robot.subsystems;

import static org.troyargonauts.robot.Constants.Pneumatics.*;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Pneumatics extends SubsystemBase {
    DoubleSolenoid solenoid;
    Compressor compressor;

    public Pneumatics() {
        solenoid = new DoubleSolenoid(PneumaticsModuleType.REVPH, FORWARD_CHANNEL_ID, REVERSE_CHANNEL_ID);
        solenoid.set(DoubleSolenoid.Value.kOff);

         compressor = new Compressor(PneumaticsModuleType.CTREPCM);
         compressor.enableDigital();
    }

    public void forward() {
        solenoid.set(DoubleSolenoid.Value.kForward);
    }

    public void backward() {
        solenoid.set(DoubleSolenoid.Value.kReverse);
    }

    public void off() {
        solenoid.set(DoubleSolenoid.Value.kOff);
    }
}
