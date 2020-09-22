import com.android.build.gradle.internal.dsl.BaseAppModuleExtension

// region Delegation Magic?
val kBS: Build_gradle = this
//kBS.plugins {
//    id("com.android.application")
//    kotlin("android")
//    kotlin("android.extensions")
//}
// endregion

plugins {
    // region PluginDependenciesSpecScope vs PluginDependenciesSpec
    val dependenciesSpec = this
    val spec = dependenciesSpec.id("com.android.application")
    // spec.apply(true)
    // spec.version("4.0.1")
    // endregion

    kotlin("android")
    kotlin("android.extensions")

    id("project-report")

    // region Convention plugin
    id("com.ncorti.kotlin.gradle.libraries")
    // endregion
}

// region Verbose Android
val ext = extensions
val andExtension = extensions.getByType(BaseAppModuleExtension::class.java)
// endregion

android {
    compileSdkVersion(30)
    buildToolsVersion("30.0.2")

    defaultConfig {
        applicationId = "com.android.worldwide.app"

        minSdkVersion(23)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.android.worldwide.app.CustomRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

// region Dependency Constraints
dependencies {
    constraints {
        androidTestImplementation("androidx.test:runner:1.3.0!!")
        androidTestImplementation("androidx.test:core-ktx") {
            val rejectVersions = (3..5).map { "alpha0$it" }.toTypedArray()

            version {
                prefer("1.3.0")
                reject(*rejectVersions)
            }
        }
    }
}
// endregion

dependencies {
    // region BOM
    implementation(enforcedPlatform(project(":platform")))
    implementation(myLibs.androidX.coreKtx)
    implementation("androidx.appcompat:appcompat")
    // endregion

    implementation(kotlin("stdlib-jdk8"))

    // region androidTestImplementationDependenciesMetadata
    androidTestImplementation("androidx.test:core-ktx")
    androidTestImplementation("androidx.test:runner") {
        because("Gives launchActivity DSL.")
    }
    // endregion
}

// region What is my task type?
// cstroe & Thomas Keller
// 	(☝ ՞ਊ ՞)☝
// https://stackoverflow.com/questions/10422054/is-there-a-way-to-list-task-dependencies-in-gradle
//gradle.taskGraph.whenReady(closureOf<TaskExecutionGraph> {
//    println("Found task graph: $this")
//    println("Found " + allTasks.size + " tasks.")
//    allTasks.forEach { task ->
//        println(task)
//        task.dependsOn.forEach { dep ->
//            println("  - $dep")
//        }
//    }
//})
// endregion
