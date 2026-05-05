package com.aissia.anilist

import com.aissia.anilist.presentation.toFormattedDuration
import com.aissia.anilist.presentation.toLanguage
import org.junit.Assert.assertEquals
import org.junit.Test

class UtilsTest {

    // --- toFormattedDuration ---

    @Test
    fun `toFormattedDuration returns hours and minutes when 90 minutes`() {
        assertEquals("1h 30m", 90.toFormattedDuration())
    }

    @Test
    fun `toFormattedDuration returns only minutes when under 60`() {
        assertEquals("45m", 45.toFormattedDuration())
    }

    @Test
    fun `toFormattedDuration returns 0m for zero`() {
        assertEquals("0m", 0.toFormattedDuration())
    }

    @Test
    fun `toFormattedDuration returns exact hours when no remainder`() {
        assertEquals("2h 0m", 120.toFormattedDuration())
    }

    // --- toLanguage ---

    @Test
    fun `toLanguage returns Japanese for JP`() {
        assertEquals("Japanese", "JP".toLanguage())
    }

    @Test
    fun `toLanguage returns Korean for KR`() {
        assertEquals("Korean", "KR".toLanguage())
    }

    @Test
    fun `toLanguage returns English for US`() {
        assertEquals("English", "US".toLanguage())
    }

    @Test
    fun `toLanguage returns English for GB`() {
        assertEquals("English", "GB".toLanguage())
    }

    @Test
    fun `toLanguage returns Unknown for null`() {
        assertEquals("Unknown", null.toLanguage())
    }

    @Test
    fun `toLanguage returns code itself for unrecognised country`() {
        assertEquals("XY", "XY".toLanguage())
    }
}
