plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
}

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

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("androidx.core:core-ktx:1.3.1")
    implementation("androidx.appcompat:appcompat:1.2.0")

    androidTestImplementation("androidx.test:core-ktx:1.3.0")
    androidTestImplementation("androidx.test:runner:1.3.0")
}

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
