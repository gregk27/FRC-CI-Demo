package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * Dummy subsytem modeled after a 2-roller intake.
 */
public class Intake extends SubsystemBase{
    
    TalonSRX talon;
    CANSparkMax spark;
    DoubleSolenoid piston;

    static final Value DEPLOYED = Value.kForward;
    static final Value RETRACTED = Value.kReverse;

    public static final double INTAKE_SPEED = 0.8;

    /**
     * Create a new intake.
     * 
     * @param mainRoller The talon for the main roller
     * @param secondRoller The spark max for the secondary roller
     * @param piston The piston for deployment
     */
    public Intake(TalonSRX mainRoller, CANSparkMax secondRoller, DoubleSolenoid piston){
        this.talon = mainRoller;
        this.spark = secondRoller;
        this.piston = piston;

        //Initialize the subsystem
        stop();
        retract();
    }

    /**
     * Spin the rollers.
     * 
     * @param speed The speed to spin at;
     */
    public void setSpeed(double speed){
        talon.set(ControlMode.PercentOutput, speed);
        spark.set(speed);
    }

    /**
     * Stop the rollers.
     */
    public void stop(){
        setSpeed(0);
    }

    /**
     * Deploy the intake. 
     */
    public void deploy(){
        piston.set(DEPLOYED);
    }

    /**
     * Retract the intake.
     */
    public void retract(){
        piston.set(RETRACTED);
    }

    public boolean isDeployed(){
        return piston.get() == DEPLOYED;
    }

}