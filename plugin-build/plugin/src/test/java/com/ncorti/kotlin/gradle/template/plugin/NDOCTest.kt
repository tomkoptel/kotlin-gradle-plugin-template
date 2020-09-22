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
        val elGradle = Book(name = "gradle")
        bookShelf.add(elGradle)

        // region NDOC getByName
        val gradleByName = bookShelf.getByName("gradle")

        assertEquals(gradleByName, elGradle)
        // endregion

        // region NDOC access with get operator
        // NOTE you need explicitly import org.gradle.kotlin.dsl.get
        assertEquals(bookShelf["gradle"], elGradle)
        // endregion

        // region NDOC access with named
        val gradleBookProvider = bookShelf.named("gradle")
        assertEquals(gradleBookProvider.get(), elGradle)
        // endregion

        // region NDOC getting
        // NOTE you need explicitly import org.gradle.kotlin.dsl.getValue
        val gradle by bookShelf.getting

        assertEquals(gradleByName, elGradle)
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
