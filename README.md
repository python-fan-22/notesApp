This is a Kotlin Multiplatform project targeting Desktop (JVM).

* [/composeApp](./composeApp/src) is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
    - [commonMain](./composeApp/src/commonMain/kotlin) is for code that's common for all targets.
    - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
      For example, if you want to use Apple's CoreCrypto for the iOS part of your Kotlin app,
      the [iosMain](./composeApp/src/iosMain/kotlin) folder would be the right place for such calls.
      Similarly, if you want to edit the Desktop (JVM) specific part, the [jvmMain](./composeApp/src/jvmMain/kotlin)
      folder is the appropriate location.

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…

## Building and Running the Application

You can build the application in two different formats:
1. As a JAR file (cross-platform)
2. As a native Linux executable (Ubuntu/Debian)

### Choosing the Build Target

The build target is controlled by the `buildTarget` property in `gradle.properties`. You can:

1. Edit the `gradle.properties` file and set `buildTarget=jar` or `buildTarget=linux`
2. Or specify the property on the command line: `-PbuildTarget=jar` or `-PbuildTarget=linux`

### Creating a JAR Distribution Package

To create a complete JAR distribution package, run:

```bash
# Either edit gradle.properties to set buildTarget=jar or use:
./gradlew composeApp:createDistribution -PbuildTarget=jar
```

This will create a zip file at `build/distributions/notesApp-jar-distribution.zip` that includes:
- The main application JAR file
- A `libs` folder containing all dependencies
- An `appData` folder containing all necessary data files

### Creating a Linux Distribution Package

To create a native Linux distribution package, run:

```bash
# Either edit gradle.properties to set buildTarget=linux or use:
./gradlew composeApp:createDistribution -PbuildTarget=linux
```

This will create:
1. A Debian package (`.deb`) in `build/compose/binaries/main/deb/`
2. An unzipped distribution in `build/compose/binaries/main/app/` with:
   - The executable file
   - A `libs` folder containing all dependencies
   - An `appData` folder containing all necessary data files
3. A zip file at `build/distributions/notesApp-linux-distribution.zip` containing the above

### Extracting and Running the JAR Distribution

1. Extract the zip file to your desired location:
   ```bash
   unzip build/distributions/notesApp-jar-distribution.zip -d /path/to/destination
   ```

2. Navigate to the extracted directory and run the application:
   ```bash
   cd /path/to/destination
   java -jar composeApp-fat.jar
   ```

The application uses relative paths for resources, so it will automatically find the `appData` folder as long as it's in the same directory as the JAR file.

### Installing and Running the Linux Distribution

You can either:

1. Install the Debian package:
   ```bash
   sudo dpkg -i build/compose/binaries/main/deb/notesApp_1.0.0-1_amd64.deb
   ```
   Then run the application from your applications menu or by typing `notesApp` in the terminal.

2. Or use the unzipped distribution:
   ```bash
   cd build/compose/binaries/main/app
   ./notesApp
   ```

### Building Just the Fat JAR (Alternative)

If you only want to build the fat JAR without creating the full distribution, run:

```bash
./gradlew composeApp:createFatJar
```

This will create a JAR file at `build/distribution/composeApp-fat.jar` that includes all dependencies. Note that you'll need to manually ensure the `appData` folder is in the same directory as the JAR when running it.