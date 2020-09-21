pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        jcenter()
    }
}

rootProject.name = ("kotlin-gradle-plugin-template")

include(":app")
include(":example")
includeBuild("plugin-build")
