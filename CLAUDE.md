# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build System

This project uses **Apache Maven with Tycho 5.0.4** to build Eclipse OSGi bundles, features, and products.
The Tycho version is set centrally in `.mvn/maven.config`, which also sets `tycho.localArtifacts=ignore`, and `.mvn/extensions.xml` picks it up via `${tycho.version}`.
The build targets **Java 25** (`maven.compiler.release` in the root `pom.xml`).
Use the Maven wrapper (Maven 3.9.11) for consistent builds.

```bash
# Build entire project
./mvnw clean verify

# Build and install to local Maven repo
./mvnw clean install

# Build a single module (run from repo root)
./mvnw clean verify -pl com.vogella.tasks.services

# Run all tests
./mvnw clean verify -pl com.vogella.tasks.services.tests

# Skip tests
./mvnw clean verify -DskipTests

# Build the native installers (opt-in profile, see Installer.adoc)
./mvnw clean verify -Pinstaller
```

The `installer` module is not part of the default reactor.
It is activated by the `installer` profile and produces native packages with `jpackage` under `installer/target/installer/`.

## Target Platform

Dependencies are resolved from `target-platform/target-platform.target`. This file pins:
- Eclipse 2026-06 release train (Equinox, Platform, P2)
- JUnit 5.14.4 (Jupiter engine + Platform)
- Mockito 5.23.0
- GSON 2.13.2
- SWTBot (for UI tests)
- Nebula Widgets

When adding new dependencies, add them to the target file rather than to individual pom.xml files.

## Architecture

This is a multi-module Eclipse RCP training project with two main applications:

### 1. Starter Example (`com.example.e4.*`)
A minimal E4 RCP application demonstrating basic workbench setup.

### 1b. Plug-in Customization Example (`com.example.rcp.plugincustomizing`)
A classic IDE-application based RCP product showing how `plugin_customization.ini` presets preferences.
This module is pomless: it has no `pom.xml` and is built through the `tycho-build` extension.

### 2. Task Management Application (`com.vogella.tasks.*`)
A more complete E4 application with OSGi services, DI, and event-driven architecture:

- **`com.vogella.tasks.model`** — Domain model (`Task`, `TaskService` interface). No Eclipse UI dependencies.
- **`com.vogella.tasks.services`** — OSGi DS component implementing `TaskService`. Uses `FrameworkUtil`/`BundleContext` for service registration.
- **`com.vogella.tasks.events`** — Event topic constants shared across bundles.
- **`com.vogella.tasks.ui`** — E4 parts, handlers, and application model (`Application.e4xmi`). Uses `@Inject`, `IEventBroker`, `EPartService`.
- **`com.vogella.tasks.ui.contribute`** and **`com.vogella.contribute.parts`** — Additional menus/parts contributed via `fragment.e4xmi`.
- **`com.vogella.tasks.update`** — p2 update handler. Prompts for the repository location and remembers it in `InstanceScope` preferences.
- **`com.vogella.tasks.feature`** / **`com.vogella.tasks.product`** — Packaging artifacts.

### Common Utility Bundles
- `com.vogella.swt.widgets` — Reusable SWT widgets
- `com.vogella.service.imageloader` — Image loading OSGi service
- `com.vogella.eclipse.css` — CSS theme overrides
- `com.vogella.osgi.taskconsumer` — Example of consuming an OSGi service

## Packaging Types (pom.xml)

| `<packaging>` | Purpose |
|---|---|
| `eclipse-plugin` | Standard OSGi bundle |
| `eclipse-test-plugin` | Test bundle, executed by Tycho Surefire |
| `eclipse-feature` | Aggregates plugins for distribution |
| `eclipse-repository` | Generates a P2 update site |

## Testing

### OSGi Integration Tests (`eclipse-test-plugin`)
Tests run inside an OSGi container via Tycho Surefire. The root pom.xml configures Tycho Surefire globally with the E4 workbench application.

Existing test modules:
- `com.vogella.tasks.services.tests` — Tests the `TaskService` OSGi service by retrieving it via `BundleContext.getServiceReference()`.
- `com.vogella.tasks.ui.tests` — Fragment test plugin (`Fragment-Host: com.vogella.tasks.ui`) for UI bundle testing.
- `com.example.e4.swtbot.tests` — End-to-end UI tests using SWTBot (commented out in root pom.xml).

### Adding a New Test Plugin
1. Create a new directory with `META-INF/MANIFEST.MF` (packaging: `eclipse-test-plugin`), `build.properties`, `pom.xml`, and `src/`.
2. In `MANIFEST.MF`, set `Bundle-SymbolicName`, declare `Import-Package` for `org.junit.jupiter.api`, and `Require-Bundle` for any tested bundles.
3. Add the module to the root `pom.xml` `<modules>` list.
4. Tests use JUnit 5 (`@Test`, `@BeforeEach`, etc.) and can access OSGi APIs via `FrameworkUtil.getBundle(this.getClass()).getBundleContext()`.

### Regular Unit Tests (non-OSGi)
To run plain JUnit tests without an OSGi container, use `eclipse-plugin` packaging (not `eclipse-test-plugin`) and configure `maven-surefire-plugin` in the module's `pom.xml` instead of relying on Tycho Surefire.
