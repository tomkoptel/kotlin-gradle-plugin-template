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
include(":platform")
includeBuild("plugin-build")
