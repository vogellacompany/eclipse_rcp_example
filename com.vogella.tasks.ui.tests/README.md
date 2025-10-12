# Tasks UI Tests

This test plug-in contains automated tests for the `com.vogella.tasks.ui` RCP application.

## Features

- **Menu Structure Tests**: Verifies that all expected menus are present and accessible
- **UI Structure Tests**: Tests the application window and UI components  
- **Login Bypass**: Provides a mechanism to skip login dialogs during testing

## Running the Tests

Tests require a graphical display (X11) to run since they test the UI. 

### Building without running tests

To build the project without running UI tests:

```bash
mvn clean verify -DskipTests
```

### Running tests with a display

On Linux with Xvfb (X virtual framebuffer):

```bash
Xvfb :99 -screen 0 1024x768x24 &
export DISPLAY=:99
mvn clean verify
```

On systems with a display, simply run:

```bash
mvn clean verify
```

## Bypassing Login Screen

To bypass the login screen during testing, set the system property `skipLogin` to `true`:

```
-DskipLogin=true
```

This is automatically configured in the test plug-in's build.properties and will be passed to tests when they run.

You can also configure this in:
1. Eclipse Run Configuration (VM arguments: `-DskipLogin=true`)
2. Programmatically using `LoginTestHelper.enableSkipLogin()`

## Test Classes

- `MenuStructureTest`: Tests the menu structure of the application
- `UIStructureTest`: Tests the UI structure and components
- `LoginTestHelper`: Utility class for controlling login behavior in tests

## Login Addon

The main application includes a `LoginAddon` class that demonstrates how to show a login dialog on startup. This addon respects the `skipLogin` system property, making it easy to bypass for automated testing.

## Dependencies

The test plug-in requires:
- JUnit 5 (Jupiter)
- SWTBot for UI testing
- Eclipse E4 RCP bundles
- com.vogella.tasks.ui bundle

## CI/CD Integration

For CI/CD pipelines without a display:
1. Use `-DskipTests` to skip test execution
2. Or configure Xvfb in your CI environment
3. Tests still compile and validate, ensuring code quality
