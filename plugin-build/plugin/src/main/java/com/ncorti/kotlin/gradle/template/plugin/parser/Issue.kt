package com.ncorti.kotlin.gradle.template.plugin.parser

internal data class Issue(
    val id: String,
    val severity: String,
    val message: String,
    val category: String,
    val priority: String,
    val summary: String,
    val explanation: String,
    val errorLine1: String,
    val errorLine2: String,
    val location: Location
) {
    private companion object {
        val PATTERN = "`R.string.(.*)`".toRegex()
    }

    val isUnusedString: Boolean get() = id == "UnusedResources" && message.contains("R.string")
    val extractStringKey: String? get() = PATTERN.findAll(message).firstOrNull()?.groupValues?.lastOrNull()
}
