package com.ncorti.kotlin.gradle.template.plugin

import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert.assertNotNull
import org.junit.Test

class TemplatePluginTest {

    @Test
    fun `plugin is applied correctly to the project`() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("com.android.application")
        project.pluginManager.apply("com.ncorti.kotlin.gradle.template.plugin")
        project.pluginManager.withPlugin(TemplatePlugin.lintBaselinePlugin) {
            val provider = project.tasks.named("listUnusedStringsDebug")
            assertNotNull(provider.get())
        }
    }
}
