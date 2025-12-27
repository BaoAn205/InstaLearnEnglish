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

rootProject.name = "InstaLearnEnglish"
include(":app")
include(":feature:home")
include(":feature:station1")
include(":feature:station23")
include(":feature:station4")
include(":feature:station5")
include(":core:data")
include(":core:ui")
include(":core:common")
