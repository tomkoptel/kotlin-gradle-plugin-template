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
                }
                """.trimIndent()
        )

        with(testProjectDir) {
            newFolder("src", "main")
            newFile("src/main/AndroidManifest.xml")
        }.writeText(
                """
                <?xml version="1.0" encoding="utf-8"?>
                <manifest xmlns:android="http://schemas.android.com/apk/res/android">
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
    }

    @Test
    fun testProject() {
        val gradleRunner = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withPluginClasspath()
                .build()
        gradleRunner.task(":help")?.outcome == TaskOutcome.SUCCESS
    }
}
