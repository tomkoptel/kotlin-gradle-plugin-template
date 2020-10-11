package com.ncorti.kotlin.gradle.template.plugin

import org.junit.Assert.assertEquals
import org.junit.Test

class RegExTest {
    @Test
    fun a() {
        val result = "`R.string.(.*)`".toRegex().findAll("The resource `R.string.offers_section_title` appears to be unused")
        assertEquals(result.first().groupValues.last(), "offers_section_title")
    }
}
