# CI Demo
This is a repository with setup that features 3 CI components for an FRC Java robot
 - Build: Verifies result of `gradlew build -x tests`
 - Tests: Unit tests
 - Syntax: Checkstyle check

# Tests
This executes JUnit tests located in `src/test/java/frc/robot` in four steps. They are run from lowest level (subsystems interfacing with hardware) to highest level (full robot).

For mocking FRC hardware, a variety of wrappers are included from https://github.com/gregk27/FRCMocks. These allow for tracking a variety of parameters such as motor controller setpoints, and solenoid values.

To implement unit tests outside of `src/java/main`, they must be added to the `.classpath` file (hidden by default in vscode). This can be done with the following snippet:
```XML
<classpathentry kind="src" output="bin/test" path="src/test/java">
    <attributes>
        <attribute name="gradle_scope" value="test"/>
        <attribute name="gradle_used_by_scope" value="test"/>
        <attribute name="test" value="true"/>
    </attributes>
</classpathentry>
```

## Subsystems
Subsystem tests include all tests under `robot/subystems`. These tests verify that subsystem functions interface properly with mock hardware. Ex. `Intake.deploy()` actually deploys the intake.

## Commands
Commands tests inlcude all tests under `robot/commands` (excluding `robot/commands/autonomous`). These tests verify that commands have the intended effects on their respective subsystems. Ex: `IntakeCommand` both deploys and starts the intake on button press, then stops/retracts on release.

### Autonomous commands
Autonomous command tests inlucde all tests under `robot/commands/autonomous`, these are run after commands as most depend on commands.

## Sim
Sim tests are tests at the top level (directly under `robot`). These tests may depend on a combination of subsystems and commands, and are therefore run last.

# Syntax
Syntax tests are run using [Checkstyle](https://checkstyle.sourceforge.io/), a highly configurable linter. These tests allow for enforcing code standards to increase consistency and legibilty. The configuration in this repository is `/checkstyle.xml`. These tests can be run manually with the command `gradlew checkstyle`, and syntax highlighting can be applied with a [VS Code extension](https://marketplace.visualstudio.com/items?itemName=shengchen.vscode-checkstyle).