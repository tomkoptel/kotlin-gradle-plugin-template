package com.ncorti.kotlin.gradle.template.plugin

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.AppliedPlugin
import org.gradle.api.tasks.Copy

abstract class TemplatePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.withType(AppPlugin::class.java) {
            val extension = project.extensions.getByName("android") as BaseAppModuleExtension
            project.applyLintVariantBaseLinePlugin {
                extension.applicationVariants.all { variant ->
                    createPluginTasks(project, variant.name)
                }
            }
        }
        project.plugins.withType(LibraryPlugin::class.java) {
            val extension = project.extensions.getByName("android") as LibraryExtension
            project.applyLintVariantBaseLinePlugin {
                extension.libraryVariants.all { variant ->
                    createPluginTasks(project, variant.name)
                }
            }
        }
    }

    private fun Project.applyLintVariantBaseLinePlugin(configure: (AppliedPlugin) -> Unit) {
        val lintBaselinePlugin = "com.nimroddayan.lint-variant-baseline"
        pluginManager.apply(lintBaselinePlugin)
        pluginManager.withPlugin(lintBaselinePlugin, configure)
    }

    private fun createPluginTasks(project: Project, variantName: String) {
        val variantNameCaps = variantName.capitalize()
        val lintBaseline = "generateLintBaseline$variantNameCaps"
        val taskProvider = project.tasks.named(lintBaseline, Copy::class.java)

        project.tasks.register("listUnusedStrings$variantNameCaps") { task ->
            task.dependsOn(taskProvider)
            task.description = "Allows to list all unused strings for $variantName"
            task.group = "Pre Lint"
        }
    }
}
