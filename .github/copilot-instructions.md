# Eclipse RCP Example Repository - Copilot Agent Instructions

## Repository Overview

This is an Eclipse RCP (Rich Client Platform) example repository used for commercial training by vogella GmbH and the Eclipse Rich Client Platform book. It contains two main Eclipse RCP applications demonstrating various E4 application model concepts, dependency injection, OSGi services, and Eclipse platform features.

**Repository Size:** ~2.4 GB (after build), ~300 KB source code (58 Java files across 19 OSGi bundles)
**Project Type:** Multi-module Eclipse Tycho/Maven build for Eclipse RCP applications
**Primary Language:** Java (JavaSE-21)
**Build System:** Apache Maven with Eclipse Tycho 5.0.0
**Framework:** Eclipse E4 Application Platform (Eclipse RCP)
**License:** EPL 2.0

## Critical Build Requirements

### Java Version Requirement (CRITICAL)

**ALWAYS use Java 21.** The project requires Java 21 due to Tycho 5.0.0 dependencies compiled with class file version 65.0.

To set Java 21:
```bash
export JAVA_HOME=/usr/lib/jvm/temurin-21-jdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
```

**Common Error:** If you see `class file version 65.0` or `compiled by a more recent version of the Java Runtime` errors, you are using the wrong Java version. Switch to Java 21 immediately.

### Maven Requirements

- **Maven Version:** 3.9.5+ (system Maven 3.9.11 works well)
- **Maven Wrapper:** The `./mvnw` wrapper is present but has Guice dependency injection issues with Tycho extensions. **ALWAYS use system `mvn` command instead of `./mvnw`**.

## Build Instructions

### Clean Build (from scratch)

**Time Required:** ~90 seconds on first run (downloads dependencies), ~40-60 seconds on subsequent builds

```bash
# Set Java 21 first (REQUIRED)
export JAVA_HOME=/usr/lib/jvm/temurin-21-jdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

# Build command
cd /home/runner/work/eclipse_rcp_example/eclipse_rcp_example
mvn clean verify -ntp
```

**Success Indicators:**
- `BUILD SUCCESS`
- `Total time: 01:26 min` (approximately)
- Product artifacts created in `com.vogella.tasks.product/target/products/` and `com.example.e4.product/target/products/`

### Incremental Build (after code changes)

```bash
# Same Java 21 setup required
mvn verify -ntp
```

### Build Artifacts

After successful build, product distributions are created:
- **Linux:** `com.vogella.tasks.product/target/products/taskmanagement-linux.gtk.x86_64.tar.gz` (~117 MB)
- **Windows:** `com.vogella.tasks.product/target/products/taskmanagement-win32.win32.x86_64.zip` (~115 MB)
- **macOS:** `com.vogella.tasks.product/target/products/taskmanagement-macosx.cocoa.x86_64.tar.gz` (~115 MB)

## Testing

### Unit Tests

The test module `com.example.e4.swtbot.tests` exists but is **commented out** in `pom.xml` (line 129-130). Tests are not run by default during the build.

To enable tests, uncomment:
```xml
<module>com.example.e4.swtbot.tests</module>
```

**Note:** Tests use SWTBot for UI testing and require X server (Xvfb) on Linux headless environments.

### Test Requirements (if enabled)

For UI tests on Linux, Xvfb must be running:
```bash
Xvfb :99 -screen 0 1920x1080x24 &
export DISPLAY=:99
```

## Project Structure

### Root Directory Files

- `README.adoc` - Project documentation with basic build instructions
- `pom.xml` - Parent Maven POM with Tycho configuration
- `.mvn/extensions.xml` - Tycho build extension configuration (version 5.0.0)
- `.travis.yml` - Legacy Travis CI configuration (outdated, not used)
- `target-platform/target-platform.target` - Eclipse target platform definition (Eclipse 2025-09 release)

### Key Modules (19 total)

**Example RCP Application (Wizard-generated):**
- `com.example.e4.rcp` - Simple RCP bundle with SamplePart
- `com.example.e4.feature` - Feature definition
- `com.example.e4.product` - Product definition
- `com.example.e4.renderer.swt` - Custom SWT renderer
- `com.example.e4.swtbot.tests` - SWTBot UI tests (commented out)

**Task Management Application (Main example):**
- `com.vogella.tasks.model` - Task domain model (exports `com.vogella.tasks.model` package)
- `com.vogella.tasks.services` - Task service implementations with JSON persistence
- `com.vogella.tasks.services.tests` - Service unit tests (not in build)
- `com.vogella.tasks.events` - Event constants
- `com.vogella.tasks.ui` - Main UI bundle with E4 application model (`Application.e4xmi`)
- `com.vogella.tasks.ui.contribute` - UI contributions via fragment
- `com.vogella.tasks.feature` - Feature aggregation
- `com.vogella.tasks.product` - Product definition with `taskmanagement.product` file
- `com.vogella.tasks.update` - P2 update handler

**Supporting Bundles:**
- `com.vogella.swt.widgets` - Custom SWT widgets
- `com.vogella.service.imageloader` - Image loading service
- `com.vogella.contribute.parts` - Additional parts contribution
- `com.vogella.eclipse.css` - CSS styling for Eclipse RCP
- `com.vogella.osgi.taskconsumer` - OSGi EventAdmin consumer example

**Build Outputs:**
- `updatesite` - P2 update site repository
- `target-platform` - Target platform definition

### Configuration Files Location

- **Target Platform:** `target-platform/target-platform.target` (references Eclipse 2025-09, JustJ JRE 21, SWTBot)
- **Product Definitions:**
  - `com.vogella.tasks.product/taskmanagement.product` (main to-do application)
  - `com.example.e4.product/com.example.e4.product` (example RCP)
- **E4 Application Models:**
  - `com.vogella.tasks.ui/Application.e4xmi` (main application model)
  - `com.example.e4.rcp/Application.e4xmi` (example application model)
- **Plugin Manifests:** Each bundle has `META-INF/MANIFEST.MF` and `build.properties`

### Key Dependencies

- **Eclipse Platform:** Eclipse 2025-09 release (via target platform)
- **Java Runtime:** JustJ OpenJDK 21 minimal JRE (bundled in products)
- **External Libraries:** Gson 2.8.6 (via Maven dependency in target platform)
- **Execution Environment:** JavaSE-21 (all bundles require this)

## GitHub Actions CI/CD

### Workflow Files

- `.github/workflows/maven.yml` - Main CI workflow
- `.github/workflows/tagged-releases.yml` - Release workflow for tagged versions

### CI Build Configuration (maven.yml)

**Trigger:** Push to `master` branch or any pull request
**Platform:** ubuntu-latest
**Java:** Temurin JDK 21
**Maven:** 3.9.9 (via stCarolas/setup-maven action)

**Important CI Steps:**
1. Xvfb setup for UI tests (even though tests are disabled)
   ```bash
   Xvfb :99 -screen 0 1920x1080x24 &
   export DISPLAY=:99
   ```
2. Build command: `mvn clean verify -ntp`
3. Automatic release of products to GitHub with tag "latest" (prerelease)

### Known CI Issues

- **Transient network errors** when downloading from Eclipse mirrors (https://www.eclipse.org/downloads/download.php) are logged but don't fail the build
- **SHA-1 digest warnings** for Gson 2.8.6 are expected and safe to ignore

## Common Issues and Workarounds

### Issue 1: Wrong Java Version

**Symptom:** `class file version 65.0, this version only recognizes up to 61.0`
**Solution:** Set `JAVA_HOME` to Java 21 as shown in Build Requirements section above

### Issue 2: Maven Wrapper Fails

**Symptom:** `ProvisionException: Unable to provision` or `No implementation for TargetPlatformArtifactResolver was bound`
**Solution:** Use system `mvn` command instead of `./mvnw`

### Issue 3: Eclipse Mirror Download Errors

**Symptom:** `Error processing mirrors URL` in build log
**Impact:** Build continues successfully; these are transient warnings
**Action:** No workaround needed

### Issue 4: Target Platform Resolution Slow

**Symptom:** First build takes 5-10 minutes
**Cause:** Downloading ~2+ GB of Eclipse platform artifacts
**Solution:** Subsequent builds use Maven local repository cache (~/.m2/repository/.cache/tycho)

## Validation Steps

After making code changes:

1. **Compile check:**
   ```bash
   mvn clean compile -ntp
   ```

2. **Full verification:**
   ```bash
   mvn clean verify -ntp
   ```

3. **Check for compilation errors** in bundles - look for `BUILD FAILURE` in output

4. **Validate MANIFEST.MF changes:**
   - Bundle-RequiredExecutionEnvironment must be `JavaSE-21`
   - Bundle-Version should end with `.qualifier` for SNAPSHOT versions
   - Export-Package and Import-Package must be syntactically correct

5. **Product validation:**
   - Products are built during `verify` phase
   - Check `com.vogella.tasks.product/target/products/` for artifacts

## Architecture Notes

### Eclipse E4 Application Model

The applications use Eclipse E4 application model defined in `.e4xmi` files:
- Application parts, handlers, commands, and menus defined declaratively
- Dependency injection using `@Inject` from Jakarta
- Services provided via OSGi service registry

### OSGi Bundle Structure

Each module is an OSGi bundle with:
- `META-INF/MANIFEST.MF` - Bundle metadata and dependencies
- `build.properties` - Defines what to include in bundle JAR
- `plugin.xml` (optional) - Extension point contributions
- `OSGI-INF/` (optional) - Declarative Services component definitions

### Tycho Polyglot

The project uses Tycho Polyglot Maven extension which generates POMs from:
- `META-INF/MANIFEST.MF` (for bundles)
- `feature.xml` (for features)
- `*.product` files (for products)

Actual Maven POMs are not checked in; they're generated at build time.

## Making Code Changes

When editing code:

1. **Java files:** Located in `src/` directories of each bundle
2. **Dependencies:** Declared in `META-INF/MANIFEST.MF` via `Require-Bundle` or `Import-Package`
3. **UI models:** Edit `.e4xmi` files (requires Eclipse IDE with E4 tools for visual editing)
4. **Services:** Use OSGi Declarative Services (annotations in code, `OSGI-INF/` XML)

### Adding Dependencies

To add a dependency to a bundle:
1. Add to target platform in `target-platform/target-platform.target` (for P2 dependencies)
2. Or add to `Require-Bundle` in `META-INF/MANIFEST.MF`
3. Or add to `<dependencies>` in target platform (for Maven artifacts)

### Common Patterns in Codebase

- **Dependency Injection:** `@Inject`, `@PostConstruct`, `@Focus`, `@Persist`
- **Event Communication:** OSGi EventAdmin service and `@EventTopic`
- **Data Binding:** JFace data binding framework
- **Services:** Defined as OSGi services, consumed via `@Inject` or service registry

## Trust These Instructions

These instructions have been validated by:
- Successful clean build from scratch (Java 21, Maven 3.9.11)
- Testing both system Maven and wrapper (wrapper fails, system works)
- Reviewing GitHub Actions workflow configuration
- Examining all configuration files and project structure
- Measuring actual build times

**If you encounter instructions that contradict CI build or README, trust these instructions** as they reflect actual tested behavior as of October 2025.

Only search or explore further if:
- These instructions are incomplete for your specific task
- You find an error in these instructions
- Project structure has changed significantly

For Eclipse RCP-specific questions not covered here, refer to:
- https://www.vogella.com/books/eclipsercp.html (Eclipse RCP book)
- https://learn.vogella.com/courses/details/rich-client-platform (training materials)
