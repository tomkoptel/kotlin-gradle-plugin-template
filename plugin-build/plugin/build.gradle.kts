plugins {
    `kotlin-dsl`
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish")
}

dependencies {
    // region Add AGP
//    compileOnly("com.android.tools.build:gradle:4.0.1") {
//        because("Auto-wiring into Android projects")
//    }
    // endregion

    // region implementation
    implementation(kotlin("stdlib-jdk7"))
    implementation(gradleApi())
    // endregion

    // region testImplementation
    testImplementation(TestingLib.JUNIT)
    // endregion
}

// region Use Java8
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
// endregion

// region gradle metadata
gradlePlugin {
    plugins {
        create(PluginCoordinates.ID) {
            id = PluginCoordinates.ID
            implementationClass = PluginCoordinates.IMPLEMENTATION_CLASS
            version = PluginCoordinates.VERSION
        }
    }
}
// endregion

// region Configuration Block for the Plugin Marker artifact on Plugin Central
pluginBundle {
    website = PluginBundle.WEBSITE
    vcsUrl = PluginBundle.VCS
    description = PluginBundle.DESCRIPTION
    tags = PluginBundle.TAGS

    plugins {
        getByName(PluginCoordinates.ID) {
            displayName = PluginBundle.DISPLAY_NAME
        }
    }
}
// endregion

// region setupPluginUploadFromEnvironment
tasks.create("setupPluginUploadFromEnvironment") {
    doLast {
        val key = System.getenv("GRADLE_PUBLISH_KEY")
        val secret = System.getenv("GRADLE_PUBLISH_SECRET")

        if (key == null || secret == null) {
            throw GradleException("gradlePublishKey and/or gradlePublishSecret are not defined environment variables")
        }

        System.setProperty("gradle.publish.key", key)
        System.setProperty("gradle.publish.secret", secret)
    }
}
// endregion
