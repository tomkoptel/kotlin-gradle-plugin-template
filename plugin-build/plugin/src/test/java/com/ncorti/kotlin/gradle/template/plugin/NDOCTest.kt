package com.ncorti.kotlin.gradle.template.plugin

import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert.assertEquals
import org.junit.Test

@Suppress("UnstableApiUsage")
class NDOCTest {
    private val project = ProjectBuilder.builder().build()
    private val bookShelf =
        project.objects.domainObjectContainer(Book::class.java)

    @Test
    fun create() {
        bookShelf.add(Book(name = "gradle"))
        bookShelf.add(Book(name = "kotlin"))

        // region NDOC give me a value!
        val gradleByName = bookShelf.getByName("gradle")
        // NOTE you need explicitly import org.gradle.kotlin.dsl.getValue
        val gradle by bookShelf.getting
        assertEquals(gradleByName, gradle)

        // NOTE you need explicitly import org.gradle.kotlin.dsl.get
        assertEquals(bookShelf["gradle"], gradle)
        assertEquals(bookShelf["gradle"], gradleByName)
        // endregion

        // region Object is a Bean
        gradle.title = "Gradle and the Philosopher's Stone"
        assertEquals(gradleByName.title, gradle.title)
        // endregion
    }

    data class Book(val name: String) {
        var title: String? = null
    }
}
