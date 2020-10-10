package com.ncorti.kotlin.gradle.template.plugin

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy

abstract class TemplatePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.withType(AppPlugin::class.java) {
            val extension = project.extensions.getByName("android") as BaseAppModuleExtension
            project.pluginManager.withPlugin("com.nimroddayan.lint-variant-baseline") {
                extension.applicationVariants.all { variant ->
                    createPluginTasks(project, variant.name)
                }
            }
        }
        project.plugins.withType(LibraryPlugin::class.java) {
            val extension = project.extensions.getByName("android") as LibraryExtension
            project.pluginManager.withPlugin("com.nimroddayan.lint-variant-baseline") {
                extension.libraryVariants.all { variant ->
                    createPluginTasks(project, variant.name)
                }
            }
        }
    }

    private fun createPluginTasks(
        project: Project,
        variantName: String
    ) {
        val variantNameCaps = variantName.capitalize()
        val lintBaseline = "generateLintBaseline$variantNameCaps"

        project.tasks.named(lintBaseline, Copy::class.java) {
            val lintBaselineTask = this

            val listUnusedTask = project.tasks.register("listUnusedStrings$variantNameCaps") { task ->
                task.dependsOn(lintBaselineTask)
                task.description = "Allows to list all unused strings for $variantNameCaps"
                task.group = "Pre Lint"
            }
        }
    }
}
