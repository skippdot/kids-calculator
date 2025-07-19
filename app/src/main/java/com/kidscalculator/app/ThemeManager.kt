package com.kidscalculator.app

import android.content.Context
import android.content.SharedPreferences

/**
 * Manages theme switching and persistence for the Kids Calculator app
 */
class ThemeManager(context: Context) {
    
    private val sharedPreferences: SharedPreferences = 
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
    companion object {
        private const val PREFS_NAME = "KidsCalculatorPrefs"
        private const val KEY_THEME = "selected_theme"
        const val THEME_DEFAULT = "default"
        const val THEME_LION_KING = "lion_king"
        const val THEME_ANIMAL = "animal"
    }
    
    /**
     * Get the currently selected theme
     */
    fun getCurrentTheme(): String {
        return sharedPreferences.getString(KEY_THEME, THEME_DEFAULT) ?: THEME_DEFAULT
    }
    
    /**
     * Set the current theme and persist it
     */
    fun setTheme(theme: String) {
        sharedPreferences.edit()
            .putString(KEY_THEME, theme)
            .apply()
    }
    
    /**
     * Toggle between themes in sequence: default -> lion_king -> animal -> default
     */
    fun toggleTheme(): String {
        val currentTheme = getCurrentTheme()
        val newTheme = when (currentTheme) {
            THEME_DEFAULT -> THEME_LION_KING
            THEME_LION_KING -> THEME_ANIMAL
            THEME_ANIMAL -> THEME_DEFAULT
            else -> THEME_DEFAULT
        }
        setTheme(newTheme)
        return newTheme
    }
    
    /**
     * Check if Lion King theme is currently active
     */
    fun isLionKingTheme(): Boolean {
        return getCurrentTheme() == THEME_LION_KING
    }
    
    /**
     * Check if Animal theme is currently active
     */
    fun isAnimalTheme(): Boolean {
        return getCurrentTheme() == THEME_ANIMAL
    }
}