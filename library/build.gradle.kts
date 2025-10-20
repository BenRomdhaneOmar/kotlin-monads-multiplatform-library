import com.android.build.api.dsl.androidLibrary
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.vanniktech.mavenPublish)
}

group = "com.benromdhane.omar.offroadsoft"
version = "0.0.1"

kotlin {
    jvm()
    androidLibrary {
        namespace = "com.benromdhane.omar.offroadsoft.monads"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        withJava()
        withHostTestBuilder {}.configure {}
        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }

        compilations.configureEach {
            compilerOptions.configure {
                jvmTarget.set(
                    JvmTarget.JVM_21
                )
            }
        }
    }
    linuxX64()

    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {

        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        val androidDeviceTest by getting {
            dependencies {
                implementation(libs.runner)
                implementation(libs.espresso.core)
                implementation(libs.junit)
            }
        }

        val wasmJsMain by getting {
            dependencies {

            }
        }
    }
}

mavenPublishing {
    publishToMavenCentral()

    signAllPublications()

    coordinates(group.toString(), "library", version.toString())

    pom {
        name = "Off Road Soft kotlin monads multiplatform library"
        description = "A library to introduce monads to kotlin multiplatform."
        inceptionYear = "2025"
        licenses {
            license {
                name = "GNU Affero General Public License, Version 3.0"
                url = "https://www.gnu.org/licenses/agpl-3.0.html#license-text"
            }
        }
        developers {
            developer {
                id = "Omar"
                name = "Omar BEN ROMDHANE"
                email = "benromdhaneomar@gmail.com"
                organization = "Off Road Soft"
                url = "https://www.linkedin.com/in/benromdhaneomar/"
                roles = listOf(
                    "Software Architect",
                    "Technical Expert",
                    "Java/Kotlin backend developer"
                )
                timezone = "GMT+1/GMT+7"
            }
        }
    }
}
