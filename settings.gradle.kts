pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Talk"
include(":app")
include(":resources")
include(":data")
//domain
include("domain:websocket")
project(":domain:websocket").projectDir = file("domain/websocket")
// features
include(":feature:course")
project(":feature:course").projectDir = file("features/course")
include(":feature:record")
project(":feature:record").projectDir = file("features/record")
