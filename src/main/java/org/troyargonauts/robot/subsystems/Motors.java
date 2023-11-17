package org.troyargonauts.robot.subsystems;

import static org.troyargonauts.robot.Constants.Motors.*;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.troyargonauts.common.motors.MotorCreation;
import org.troyargonauts.common.motors.wrappers.LazyCANSparkMax;
import org.troyargonauts.common.motors.wrappers.LazyTalon;

import java.util.List;

public class Motors extends SubsystemBase {
//    LazyTalon<TalonFX> one, two, three, four;
    LazyCANSparkMax one, two, three, four;

//    MotorControllerGroup<TalonFX> motors;
    edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup motors;

    public Motors() {
//        one = MotorCreation.createDefaultTalonFX(ONE_ID);
//        two = MotorCreation.createDefaultTalonFX(TWO_ID);
//        three = MotorCreation.createDefaultTalonFX(THREE_ID);
//        four = MotorCreation.createDefaultTalonFX(FOUR_ID);
        one = new LazyCANSparkMax(ONE_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
        two = new LazyCANSparkMax(TWO_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
        three = new LazyCANSparkMax(THREE_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
        four = new LazyCANSparkMax(FOUR_ID, CANSparkMaxLowLevel.MotorType.kBrushless);

//        motors = new MotorControllerGroup<>(one, List.of(two, three, four), false, false);
        motors = new MotorControllerGroup(one, two, three, four);
    }

    public void set(double speed) {
//        motors.getMaster().set(speed);
        motors.set(speed);
    }
}
