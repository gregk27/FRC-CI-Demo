package frc.robot.subsystems;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import org.junit.Before;
import org.junit.Test;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import ler.mocks.ctre.MockCTREController;
import ler.mocks.rev.MockCANSparkMax;
import ler.mocks.wpilib.MockDoubleSolenoid;

/**
 * JUnit test to assert functionality of the intake subsystem.
 */
public class IntakeTest {
    
    MockCTREController<TalonSRX> talon;
    MockCANSparkMax spark;
    MockDoubleSolenoid solenoid;

    Intake intake;

    static final String CONTROL_MODE_MESSAGE = "Talon in wrong control mode";
    static final double SPEED_TOLERANCE = 0.001;

    /**
     * Setup hardware.
     */
    @Before
    public void setup(){
        // Create hardware devices
        talon = new MockCTREController<>(TalonSRX.class);
        spark = new MockCANSparkMax();
        solenoid = new MockDoubleSolenoid();

        // Create the subsystem instance
        intake = new Intake(talon.getMock(), spark.getMock(), solenoid.getMock());
    }

    /**
     * Ensure that constructor initialises subsystem to correct state.
     */
    @Test
    public void testConstructor(){
        // Assert
        // Check that intake is retracted
        assertEquals("Intake not retracted", Intake.RETRACTED, solenoid.state);
        // Check that motors are stopped
        assertEquals(CONTROL_MODE_MESSAGE, ControlMode.PercentOutput, talon.controlMode);
        assertEquals("Talon not at 0", 0, talon.setpoint, 0);

        assertFalse(CONTROL_MODE_MESSAGE, spark.pidMode);
        assertEquals("Spark not at 0", 0, spark.setpoint, 0);
    }

    /**
     * Test that the setSpeed function works.
     */
    @Test
    public void testSetSpeed(){
        // Act
        intake.setSpeed(Intake.INTAKE_SPEED);

        // Assert
        assertEquals(CONTROL_MODE_MESSAGE, ControlMode.PercentOutput, talon.controlMode);
        assertEquals("Talon at wrong setpoint", Intake.INTAKE_SPEED, talon.setpoint, SPEED_TOLERANCE);

        assertFalse(CONTROL_MODE_MESSAGE, spark.pidMode);
        assertEquals("Spark at wrong setpoint", Intake.INTAKE_SPEED, spark.setpoint, SPEED_TOLERANCE);
    }

    /**
     * Test that the stop function works.
     */
    @Test
    public void testStop(){
        // Arrange
        // Set the motors to an elevated speed
        spark.setpoint = 1;
        talon.setpoint = 1;

        // Act
        intake.stop();

        // Assert
        assertEquals(CONTROL_MODE_MESSAGE, ControlMode.PercentOutput, talon.controlMode);
        assertEquals("Talon not at 0", 0, talon.setpoint, 0);

        assertFalse(CONTROL_MODE_MESSAGE, spark.pidMode);
        assertEquals("Spark not at 0", 0, spark.setpoint, 0);
    }

    /**
     * Test the intake deploy function.
     */
    @Test
    public void testDeploy(){
        // Arrange
        solenoid.state = Value.kOff;

        // Act
        intake.deploy();

        // Assert
        assertEquals("Piston not depolyed", Intake.DEPLOYED, solenoid.state);
    }

    /**
     * Test the intake retract function.
     */
    @Test
    public void testRetract(){
        // Arrange
        solenoid.state = Value.kOff;

        // Act
        intake.retract();

        // Assert
        assertEquals("Piston not depolyed", Intake.RETRACTED, solenoid.state);
    }

    /**
     * Test the isDeployed function.
     */
    @Test
    public void testIsDeployed(){
            // Arrange
            solenoid.state = Value.kOff;
            // Assert
            assertFalse("Should return false", intake.isDeployed());
            
            // Arrange
            solenoid.state = Intake.RETRACTED;
            // Assert
            assertFalse("Should return false", intake.isDeployed());
            
            // Arrange
            solenoid.state = Intake.DEPLOYED;
            // Assert
            assertTrue("Should return true", intake.isDeployed());
    }

}