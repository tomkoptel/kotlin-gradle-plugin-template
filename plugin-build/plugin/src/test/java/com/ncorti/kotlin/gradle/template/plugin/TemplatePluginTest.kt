package com.ncorti.kotlin.gradle.template.plugin

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class TemplatePluginTest {
    @get:Rule
    val testProjectDir = TemporaryFolder()

    @Test
    fun `plugin is applied correctly to the project`() {
        val project = ProjectBuilder.builder().build()
        val internal = project as ProjectInternal
        project.pluginManager.apply("com.android.application")
        project.pluginManager.apply("com.ncorti.kotlin.gradle.template.plugin")

        project.plugins.withType(AppPlugin::class.java) {
            val android = project.extensions.getByName("android") as BaseAppModuleExtension

            android.apply {
                compileSdkVersion(30)
                buildToolsVersion("30.0.2")
                defaultConfig.apply {
                    minSdkVersion(23)
                    targetSdkVersion(30)
                }
                buildTypes.apply {
                    getByName("release") {
                        it.isMinifyEnabled = true
                        it.proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
                    }
                }
                lintOptions.baselineFile = testProjectDir.newFile()
            }
        }

        project.pluginManager.withPlugin(TemplatePlugin.lintBaselinePlugin) {
            project.afterEvaluate {
                val provider = project.tasks.named("listUnusedStringsDebug")
                assertNotNull(provider.get())
            }
        }
        internal.evaluate()
    }
}
