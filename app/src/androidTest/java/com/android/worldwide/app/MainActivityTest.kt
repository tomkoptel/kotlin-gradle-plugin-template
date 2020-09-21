package com.android.worldwide.app

import androidx.test.core.app.launchActivity
import org.junit.Test

internal class MainActivityTest {
    @Test
    fun startActivity() {
        val scenario = launchActivity<MainActivity>()
        scenario.recreate()
    }
}
