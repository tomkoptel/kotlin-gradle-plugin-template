import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.android.build.gradle.internal.coverage.JacocoOptions
import com.android.build.gradle.internal.CompileOptions
import com.android.build.gradle.internal.dsl.DefaultConfig
import com.android.build.gradle.internal.dsl.BuildType
import com.android.build.gradle.api.AndroidSourceSet
import org.gradle.api.NamedDomainObjectContainer

// region Delegation Magic?
// val buildGradle: Build_gradle = this

// Will it work?
//val kBS: KotlinBuildScript = buildGradle

// Will it work?
//buildGradle.plugins {
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

    // region kotlin-android
    kotlin("android")
    kotlin("android.extensions")
    // endregion

    // region project-report
    id("project-report")
    // endregion

    // region Convention plugin
    id("com.ncorti.kotlin.gradle.libraries")
    // endregion
}

// region ExtensionContainer
val extensionAware: ExtensionAware = project
val ext: ExtensionContainer = extensionAware.extensions
// endregion

// region Configure Android Extension: With Accessor
project.android { compileSdkVersion(30) }
// endregion

// region Configure Android Extension: With ExtensionContainer
val andExtension: BaseAppModuleExtension = ext.getByType(BaseAppModuleExtension::class.java)
andExtension.compileSdkVersion(30)
// or
andExtension.apply { compileSdkVersion(30) }
// endregion

// region Configure Android Extension: ExtensionContainer is a Convention
val convention: Convention = project.extensions as Convention
val andExtension2: BaseAppModuleExtension = convention.getByType(BaseAppModuleExtension::class.java)
andExtension2.compileSdkVersion(30)
// endregion

// region Configure Android Extension: With a Plugin convention
project.configure<BaseAppModuleExtension> {
    val receiver: BaseAppModuleExtension = this
    receiver.compileSdkVersion(30)
}
// endregion

// region Android Extension internals
val moduleExtension: BaseAppModuleExtension = ext.getByType(BaseAppModuleExtension::class.java)
val jacoco: JacocoOptions = moduleExtension.jacoco
val compileOptions: CompileOptions = moduleExtension.compileOptions
val defaultConfig: DefaultConfig = moduleExtension.defaultConfig
val buildTypes: NamedDomainObjectContainer<BuildType> = moduleExtension.buildTypes
val sourceSets: NamedDomainObjectContainer<AndroidSourceSet> = moduleExtension.sourceSets
// endregion

// region NDOC: getByName
val releaseBuildType = buildTypes.getByName("release")
releaseBuildType.isMinifyEnabled = false

moduleExtension.buildTypes {
    val container: NamedDomainObjectContainer<BuildType> = this

    val releaseBT = container.findByName("release")
    releaseBT?.isMinifyEnabled = false

    val requireRelease = container.getByName("release")
    requireRelease.isMinifyEnabled = false
}
// endregion

// region NDOC: getting
moduleExtension.buildTypes {
    val container: NamedDomainObjectContainer<BuildType> = this
    val release by container.getting
    release.isMinifyEnabled = false
}

moduleExtension.buildTypes {
    val container: NamedDomainObjectContainer<BuildType> = this
    val release by container.getting {
        isMinifyEnabled = false
    }
}
// endregion

// region Android Standard Config Boilerplate
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
// endregion

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
