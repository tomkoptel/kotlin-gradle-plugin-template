package com.ncorti.kotlin.gradle.template.plugin

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.ncorti.kotlin.gradle.template.plugin.parser.Issue
import com.ncorti.kotlin.gradle.template.plugin.parser.lintIssues
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.AppliedPlugin
import org.gradle.api.tasks.Copy
import java.io.File

abstract class TemplatePlugin : Plugin<Project> {
    companion object {
        const val lintBaselinePlugin = "com.nimroddayan.lint-variant-baseline"
    }

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
        pluginManager.apply(lintBaselinePlugin)
        pluginManager.withPlugin(lintBaselinePlugin, configure)
    }

    private fun createPluginTasks(project: Project, variantName: String) {
        val variantNameCaps = variantName.capitalize()
        val lintBaseline = "generateLintBaseline$variantNameCaps"
        val taskProvider = project.tasks.named(lintBaseline, Copy::class.java)

        project.tasks.register("listUnusedStrings$variantNameCaps") { task ->
            val outputDirPath = "${project.buildDir}/unusedRes$variantNameCaps"
            val outputFilePath = "$outputDirPath/strings.csv"

            task.description = "Allows to list all unused strings for $variantName"
            task.group = "Pre Lint"
            task.inputs.files(taskProvider).withPropertyName("baseLineFiles")
            task.outputs.dir(outputDirPath).withPropertyName("outputDir")
            task.outputs.file(outputFilePath).withPropertyName("outputFile")
            task.dependsOn(taskProvider)

            task.doLast {
                val baselineFile = it.inputs.files.asFileTree.files.firstOrNull { file ->
                    file.name.contains("baseline", ignoreCase = true) ?: false
                }

                baselineFile?.let {
                    val outputFile = File(outputFilePath).apply {
                        if (!exists()) {
                            createNewFile()
                        }
                    }
                    listUnusedStrings(project, baselineFile, outputFile)
                }
            }
        }
    }

    private fun listUnusedStrings(project: Project, baselineFile: File, outputFile: File) {
        baselineFile.lintIssues()
            .asSequence()
            .filter(Issue::isUnusedString)
            .mapNotNull {
                project.logger.info("Recorded issue $it")
                it.extractStringKey
            }
            .forEach { issue ->
                outputFile.appendText("$issue\n")
            }
        project.logger.info("The unused strings recorded at $outputFile")
    }
}
