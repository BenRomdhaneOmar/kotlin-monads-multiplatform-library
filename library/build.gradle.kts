import dev.detekt.gradle.Detekt
import dev.detekt.gradle.DetektCreateBaselineTask
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.vanniktech.mavenPublish)
    alias(libs.plugins.sonarqube)
    alias(libs.plugins.kover)
    alias(libs.plugins.detekt)
}

repositories {
    mavenCentral()
}

group = "com.benromdhane.omar.offroadsoft"
version = "0.0.0"

kotlin {
    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_25)
        }
    }
    jvmToolchain(25)
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
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(
                        JvmTarget.JVM_25
                    )
                }
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
            implementation(libs.kotest.assertions.core)
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

    kover {
        reports {
        }
    }
    detekt {
        buildUponDefaultConfig = true
        allRules = false
        config.setFrom("$projectDir/detekt-config/detekt.yml")
        baseline = file("$projectDir/detekt-config/baseline.xml")
    }
    tasks.withType<Detekt>().configureEach {
        reports {
            html.required.set(true)
            checkstyle.required.set(true)
            sarif.required.set(true)
            markdown.required.set(true)
        }
    }
    tasks.withType<Detekt>().configureEach {
        jvmTarget = "25"
    }
    tasks.withType<DetektCreateBaselineTask>().configureEach {
        jvmTarget = "25"
    }

    sonarqube {
        properties {
            val koverReport =
                allprojects.mapNotNull { project ->
                    val reportPath = "${project.projectDir}/build/reports/kover/report.xml"
                    if (File(reportPath).exists()) reportPath else null
                }
                    .joinToString(",")
            property("sonar.coverage.jacoco.xmlReportPaths", koverReport)
            val detektReports =
                allprojects.mapNotNull { project ->
                    val reportPath = "${project.projectDir}/build/reports/detekt/detekt.xml"
                    if (File(reportPath).exists()) reportPath else null
                }
                    .joinToString(",")
            property("sonar.kotlin.detekt.reportPaths", detektReports)
        }
    }
    tasks.named("sonar") {
        dependsOn(subprojects.map { it.tasks.named("koverXmlReport") })
    }
}

publishing {
    publications {

    }
}

mavenPublishing {
    publishToMavenCentral()

//    signAllPublications()

    coordinates(group.toString(), "kotlin-monads-multiplatform-library", version.toString())

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
