pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        mavenLocal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        maven("https://androidx.dev/storage/compose-compiler/repository/") {
            content {
                includeGroupByRegex("androidx\\.compose\\.compiler.*")
            }
        }
        mavenCentral()
        mavenLocal()
    }
}

rootProject.name = "kotlin-monads-multiplatform-library"
include(":library")
