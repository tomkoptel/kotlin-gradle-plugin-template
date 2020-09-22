package com.ncorti.kotlin.gradle

abstract class ExternalLibrariesExtension {
    val androidxCoreKtx = "androidx.core:core-ktx"
    val androidX = AndroidX

    object AndroidX {
        val coreKtx = "androidx.core:core-ktx"
    }
}
