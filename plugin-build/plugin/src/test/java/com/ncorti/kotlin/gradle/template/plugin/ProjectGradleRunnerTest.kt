package com.ncorti.kotlin.gradle.template.plugin

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class ProjectGradleRunnerTest {
    @get:Rule
    val testProjectDir = TemporaryFolder()

    @Before
    fun setupProject() {
        val buildGradle = testProjectDir.newFile("build.gradle")
        buildGradle.writeText(
            """
                buildscript {
                    repositories {
                        google()
                        jcenter()
                    }
                    dependencies {
                        classpath("com.android.tools.build:gradle:4.0.1")
                    }
                }
                plugins {
                    id 'com.android.application'
                    id 'com.ncorti.kotlin.gradle.template.plugin'
                }
                repositories {
                    google()
                    jcenter()
                }
                android {
                    compileSdkVersion(30)
                    buildToolsVersion("30.0.2")
                
                    defaultConfig {
                        applicationId = "com.dummy.android.app"
                
                        minSdkVersion(23)
                        targetSdkVersion(30)
                        versionCode = 1
                        versionName = "1.0"
                    }
                    android {
                        lintOptions {
                            // It is recommended to set the baseline file path to build dir
                            // because the variant specific one will be copied from variant source dir just before lint starts
                            baseline file("build/lint-baseline.xml")
                            checkDependencies false
                            checkReleaseBuilds true
                            abortOnError true
                        }
                    }
                }
            """.trimIndent()
        )

        with(testProjectDir) {
            newFolder("src", "main")
            newFile("src/main/AndroidManifest.xml")
        }.writeText(
            """
                <?xml version="1.0" encoding="utf-8"?>
                <manifest xmlns:android="http://schemas.android.com/apk/res/android"
                    package="com.dummy.android.app">
                    <application android:label="@string/app_name"/>
                </manifest>
            """.trimIndent()
        )

        with(testProjectDir) {
            newFolder("src", "main", "res", "values")
            newFile("src/main/res/values/strings.xml")
        }.writeText(
            """
                <resources>
                    <string name="app_name">Dummy app</string>
                </resources>
            """.trimIndent()
        )

        testProjectDir.newFolder("src", "debug")
    }

    @Test
    fun testProject() {
        val gradleRunner = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withArguments(listOf("listUnusedStringsDebug", "-Dlint.baselines.continue=true"))
            .withPluginClasspath()
            .build()
        println(gradleRunner.output)
        gradleRunner.task(":listUnusedStringsDebug")?.outcome == TaskOutcome.SUCCESS
    }
}
