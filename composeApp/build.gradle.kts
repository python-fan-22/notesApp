import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.gradle.jvm.tasks.Jar
import java.io.File

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    kotlin("plugin.serialization") version "2.2.0"

}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

// Read the buildTarget property from gradle.properties
val buildTarget = project.findProperty("buildTarget") as String? ?: "jar"

kotlin {
    jvm()
    linuxX64()

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}


compose.desktop {
    application {
        mainClass = "com.music.notes.notesapp.MainKt"

        nativeDistributions {
            // Target formats based on buildTarget property
            targetFormats(TargetFormat.Deb)

            
            packageName = "notes"
            packageVersion = "1.0.0"

            appResourcesRootDir.set(File("/home/reuben/IdeaProjects/notesApp/composeApp/src/jvmMain/composeResources"))


            // Enhanced Linux configuration
            linux {

                // Set icon, menu entry, etc. if needed
                iconFile.set(project.file("/home/reuben/IdeaProjects/notesApp/img.png"))
                
                // Set installation directory
                installationPath = "/opt/notesapp"
                
                // Set desktop entry details
                menuGroup = "Music"
                shortcut = true

            }
        }
    }
}

tasks.register<Copy>("embedAppData") {
    from(project.layout.projectDirectory.dir("appData"))
    into(layout.buildDirectory.dir("compose/binaries/main/appData"))
}

tasks.whenTaskAdded {
    if (name == "packageDeb") {
        dependsOn("embedAppData")
    }
}
