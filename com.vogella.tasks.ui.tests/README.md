# Tasks UI Tests

This test plug-in contains automated tests for the `com.vogella.tasks.ui` RCP application.

## Important: Build Configuration

**Note:** This test plug-in is commented out in the main `pom.xml` by default, similar to other UI test plugins in this repository. UI tests require a graphical display and can be unreliable in CI environments even with Xvfb.

To enable the tests, uncomment the module in `pom.xml`:
```xml
<module>com.vogella.tasks.ui.tests</module>
```

## Features

- **Menu Structure Tests**: Verifies that all expected menus are present and accessible
- **UI Structure Tests**: Tests the application window and UI components  
- **Login Bypass**: Provides a mechanism to skip login dialogs during testing

## Running the Tests

### Option 1: Run tests in Eclipse IDE
1. Import the test plugin into Eclipse
2. Right-click on a test class
3. Select "Run As" > "JUnit Plug-in Test"

### Option 2: Build with Maven (requires display)

First, uncomment the test module in `pom.xml`, then:

**On Linux with Xvfb (X virtual framebuffer):**

```bash
Xvfb :99 -screen 0 1024x768x24 &
export DISPLAY=:99
mvn clean verify
```

**On systems with a display:**
```bash
mvn clean verify
```

**To compile without running tests:**
```bash
mvn clean verify -DskipTests
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

For CI/CD pipelines:
1. Keep the test module commented out in `pom.xml` (default)
2. Tests can still be run manually in Eclipse for development
3. This prevents build failures due to display/UI issues in CI

## Why Tests Are Disabled by Default

UI tests in Eclipse RCP applications:
- Require a graphical display (X11 on Linux, native display on Windows/Mac)
- Can be flaky even with Xvfb due to timing issues
- May fail with different window managers or display configurations
- Are better run interactively during development

For these reasons, UI tests are disabled in the CI build but remain available for local testing and development.
