package com.tests

import androidx.test.espresso.matcher.ViewMatchers.assertThat
// import com.google.common.truth.Truth.assertThat
import com.shjman.polygon.ui.home.AddModelUnit
import org.hamcrest.CoreMatchers
import org.junit.Test

class AddModelUnitTest {

    @Test
    fun `empty model returns false`() {
        val result = AddModelUnit.validateModelInput(
            model = ""
        )
        assertThat(result, CoreMatchers.`is`(false))
    }

    @Test
    fun `lada model returns false`() {
        val result = AddModelUnit.validateModelInput(
            model = "sdaflada"
        )
        assertThat(result, CoreMatchers.`is`(false))
    }

    @Test
    fun `whitespace model returns false`() {
        val result = AddModelUnit.validateModelInput(
            model = "     "
        )
        assertThat(result, CoreMatchers.`is`(false))
    }

    @Test
    fun `valid model returns true`() {
        val result = AddModelUnit.validateModelInput(
            model = "tesla"
        )
        assertThat(result, CoreMatchers.`is`(true))
    }
}