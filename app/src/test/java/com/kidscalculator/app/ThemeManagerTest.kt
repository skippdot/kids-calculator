package com.kidscalculator.app

import android.content.Context
import android.content.SharedPreferences
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mockito.*

/**
 * Unit tests for ThemeManager backed by mocked SharedPreferences,
 * so the real production code is exercised.
 */
class ThemeManagerTest {

    private lateinit var context: Context
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var themeManager: ThemeManager

    private var storedTheme: String? = null

    @Before
    fun setup() {
        storedTheme = null
        context = mock(Context::class.java)
        sharedPreferences = mock(SharedPreferences::class.java)
        editor = mock(SharedPreferences.Editor::class.java)

        `when`(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPreferences)
        `when`(sharedPreferences.getString(anyString(), anyString()))
            .thenAnswer { invocation -> storedTheme ?: invocation.getArgument(1) }
        `when`(sharedPreferences.edit()).thenReturn(editor)
        `when`(editor.putString(anyString(), anyString())).thenAnswer { invocation ->
            storedTheme = invocation.getArgument(1)
            editor
        }

        themeManager = ThemeManager(context)
    }

    @Test
    fun theme_constants_are_correct() {
        assertEquals("default", ThemeManager.THEME_DEFAULT)
        assertEquals("lion_king", ThemeManager.THEME_LION_KING)
        assertEquals("animal", ThemeManager.THEME_ANIMAL)
    }

    @Test
    fun theme_names_are_different() {
        assertNotEquals(ThemeManager.THEME_DEFAULT, ThemeManager.THEME_LION_KING)
        assertNotEquals(ThemeManager.THEME_DEFAULT, ThemeManager.THEME_ANIMAL)
        assertNotEquals(ThemeManager.THEME_LION_KING, ThemeManager.THEME_ANIMAL)
    }

    @Test
    fun default_theme_is_returned_when_nothing_stored() {
        assertEquals(ThemeManager.THEME_DEFAULT, themeManager.getCurrentTheme())
    }

    @Test
    fun set_theme_persists_and_is_returned() {
        themeManager.setTheme(ThemeManager.THEME_ANIMAL)

        assertEquals(ThemeManager.THEME_ANIMAL, themeManager.getCurrentTheme())
        verify(editor, atLeastOnce()).apply()
    }

    @Test
    fun toggle_from_default_goes_to_lion_king() {
        val newTheme = themeManager.toggleTheme()

        assertEquals(ThemeManager.THEME_LION_KING, newTheme)
        assertEquals(ThemeManager.THEME_LION_KING, themeManager.getCurrentTheme())
    }

    @Test
    fun toggle_from_lion_king_goes_to_animal() {
        themeManager.setTheme(ThemeManager.THEME_LION_KING)

        assertEquals(ThemeManager.THEME_ANIMAL, themeManager.toggleTheme())
    }

    @Test
    fun toggle_from_animal_goes_back_to_default() {
        themeManager.setTheme(ThemeManager.THEME_ANIMAL)

        assertEquals(ThemeManager.THEME_DEFAULT, themeManager.toggleTheme())
    }

    @Test
    fun toggle_from_unknown_theme_falls_back_to_default() {
        themeManager.setTheme("garbage_value")

        assertEquals(ThemeManager.THEME_DEFAULT, themeManager.toggleTheme())
    }

    @Test
    fun full_toggle_cycle_returns_to_start() {
        val first = themeManager.toggleTheme()
        val second = themeManager.toggleTheme()
        val third = themeManager.toggleTheme()

        assertEquals(ThemeManager.THEME_LION_KING, first)
        assertEquals(ThemeManager.THEME_ANIMAL, second)
        assertEquals(ThemeManager.THEME_DEFAULT, third)
    }

    @Test
    fun is_lion_king_theme_reflects_current_theme() {
        assertFalse(themeManager.isLionKingTheme())

        themeManager.setTheme(ThemeManager.THEME_LION_KING)

        assertTrue(themeManager.isLionKingTheme())
        assertFalse(themeManager.isAnimalTheme())
    }

    @Test
    fun is_animal_theme_reflects_current_theme() {
        assertFalse(themeManager.isAnimalTheme())

        themeManager.setTheme(ThemeManager.THEME_ANIMAL)

        assertTrue(themeManager.isAnimalTheme())
        assertFalse(themeManager.isLionKingTheme())
    }
}
