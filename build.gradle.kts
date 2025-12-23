plugins {
    alias(libs.plugins.android.kotlin.multiplatform.library) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.vanniktech.mavenPublish) apply false
    alias(libs.plugins.sonarqube)
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