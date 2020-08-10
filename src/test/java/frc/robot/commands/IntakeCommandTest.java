package frc.robot.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ca.gregk.frcmocks.ctre.MockCTREController;
import ca.gregk.frcmocks.rev.MockCANSparkMax;
import ca.gregk.frcmocks.scheduler.MockButton;
import ca.gregk.frcmocks.scheduler.MockHardwareExtension;
import ca.gregk.frcmocks.scheduler.TestWithScheduler;
import ca.gregk.frcmocks.wpilib.MockDoubleSolenoid;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.Intake;

/**
 * Tests to ensure functionality of IntakeCommand and associated subsystems.
 */
public class IntakeCommandTest {
    
    CommandScheduler scheduler = null;

    MockCTREController<TalonSRX> intakeTalon;
    MockCANSparkMax intakeSpark;
    MockDoubleSolenoid intakePiston;

    Intake intake;

    IntakeCommand command;
    

    static final String CONTROL_MODE_MESSAGE = "Talon in wrong control mode";
    static final double SPEED_TOLERANCE = 0.001;

    /**
     * Setup scheduler/HAL and subsystems.
     */
    @Before
    public void setup(){
        // Setup scheduler system
        TestWithScheduler.schedulerStart();
        TestWithScheduler.schedulerClear();
        MockHardwareExtension.beforeAll();

        // Setup subsystems
        intakeTalon = new MockCTREController<>(TalonSRX.class);
        intakeSpark = new MockCANSparkMax();
        intakePiston = new MockDoubleSolenoid();
        intake = new Intake(intakeTalon.getMock(), intakeSpark.getMock(), intakePiston.getMock());

        // Create the command
        command = new IntakeCommand(intake);
    }

    /**
     * Test behaviour on command initialization.
     */
    @Test
    public void onInit(){
        // Arrange
        MockButton button = new MockButton();
        button.whileHeld(command);

        // Act
        button.push();
        CommandScheduler.getInstance().run();

        // Assert
        assertTrue("Intake not deployed", intake.isDeployed());
        assertEquals(CONTROL_MODE_MESSAGE, ControlMode.PercentOutput, intakeTalon.controlMode);
        assertEquals("Talon not spinning", Intake.INTAKE_SPEED, intakeTalon.setpoint, SPEED_TOLERANCE);
        assertFalse("Spark in PID mode", intakeSpark.pidMode);
        assertEquals("Spark not spinning", Intake.INTAKE_SPEED, intakeSpark.setpoint, SPEED_TOLERANCE);
    }

    /**
     * Test behaviour on command end.
     */
    @Test
    public void onEnd(){
        // Arrange
        MockButton button = new MockButton();
        button.whileHeld(command);

        // Act
        button.push();
        CommandScheduler.getInstance().run();
        button.release();
        CommandScheduler.getInstance().run();
        
        // Assert
        // assertFalse("Intake not retracted", intake.isDeployed());
        assertEquals(CONTROL_MODE_MESSAGE, ControlMode.PercentOutput, intakeTalon.controlMode);
        assertEquals("Talon not stopped", 0, intakeTalon.setpoint, 0);        
        assertFalse("Spark in PID mode", intakeSpark.pidMode);
        assertEquals("Spark not stopped", 0, intakeSpark.setpoint, 0);
    }


    /**
     * Release scheduler and HAL.
     */
    @After
    public void after(){
        TestWithScheduler.schedulerDestroy();
        MockHardwareExtension.afterAll();
    }

}