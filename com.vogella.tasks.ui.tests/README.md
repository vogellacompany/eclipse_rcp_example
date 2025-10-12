# Tasks UI Tests

This test plug-in contains automated tests for the `com.vogella.tasks.ui` RCP application.

## Features

- **Menu Structure Tests**: Verifies that all expected menus are present and accessible
- **UI Structure Tests**: Tests the application window and UI components
- **Login Bypass**: Provides a mechanism to skip login dialogs during testing

## Running the Tests

Tests can be run using Maven/Tycho:

```bash
mvn clean verify
```

## Bypassing Login Screen

To bypass the login screen during testing, set the system property `skipLogin` to `true`:

```
-DskipLogin=true
```

This can be configured in:
1. Maven surefire configuration
2. Eclipse Run Configuration (VM arguments)
3. Programmatically using `LoginTestHelper.enableSkipLogin()`

## Test Classes

- `MenuStructureTest`: Tests the menu structure of the application
- `UIStructureTest`: Tests the UI structure and components
- `LoginTestHelper`: Utility class for controlling login behavior in tests

## Dependencies

The test plug-in requires:
- JUnit 5 (Jupiter)
- SWTBot for UI testing
- Eclipse E4 RCP bundles
- com.vogella.tasks.ui bundle
