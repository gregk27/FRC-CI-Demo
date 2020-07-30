package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;

/**
 * Command that deploys and runs intake.
 */
public class IntakeCommand extends CommandBase{
    
    Intake intake;

    /**
     * Create a new intake command.
     * @param intake The robot's intake
     */
    public IntakeCommand(Intake intake){
        addRequirements(intake);
        this.intake = intake;

    }

    @Override
    public void initialize() {
        intake.deploy();
        intake.setSpeed(Intake.INTAKE_SPEED);
    }

    @Override
    public void end(boolean interrupted) {
        intake.stop();
        intake.retract();
    }

}