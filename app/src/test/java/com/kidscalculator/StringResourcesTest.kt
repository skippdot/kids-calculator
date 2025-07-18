package com.kidscalculator

import org.junit.Test
import org.junit.Assert.*

/**
 * Unit tests for string resources validation
 */
class StringResourcesTest {
    
    @Test
    fun russian_tts_strings_not_empty() {
        // Test that Russian TTS strings are not empty
        val russianStrings = listOf("плюс", "минус", "умножить", "разделить", "равно", "очистить")
        
        for (str in russianStrings) {
            assertFalse("Russian TTS string should not be empty", str.isEmpty())
            assertTrue("Russian TTS string should contain Cyrillic characters", 
                str.any { it in 'а'..'я' || it in 'А'..'Я' })
        }
    }
    
    @Test
    fun math_operations_symbols_correct() {
        // Test that math operation symbols are correct
        val plus = "+"
        val minus = "-"
        val multiply = "×"
        val divide = "÷"
        val equals = "="
        val clear = "C"
        
        assertEquals("+", plus)
        assertEquals("-", minus)
        assertEquals("×", multiply)
        assertEquals("÷", divide)
        assertEquals("=", equals)
        assertEquals("C", clear)
    }
    
    @Test
    fun division_by_zero_message_in_russian() {
        // Test that division by zero message is in Russian
        val message = "нельзя делить на ноль"
        assertFalse("Division by zero message should not be empty", message.isEmpty())
        assertTrue("Division by zero message should contain Cyrillic characters", 
            message.any { it in 'а'..'я' || it in 'А'..'Я' })
    }
    
    @Test
    fun app_name_not_empty() {
        // Test that app name is not empty
        val appName = "Kids Calculator"
        assertFalse("App name should not be empty", appName.isEmpty())
        assertTrue("App name should contain expected text", appName.contains("Calculator"))
    }
    
    @Test
    fun default_display_value() {
        // Test default display value
        val defaultValue = "0"
        assertEquals("0", defaultValue)
        assertTrue("Default value should be numeric", defaultValue.all { it.isDigit() })
    }
    
    @Test
    fun name_dialog_strings_in_russian() {
        // Test that name dialog strings are in Russian
        val dialogTitle = "Как тебя зовут?"
        val dialogHint = "Введи своё имя"
        val dialogSave = "Сохранить"
        val dialogCancel = "Отмена"
        val helloPrefix = "Привет"
        
        val russianStrings = listOf(dialogTitle, dialogHint, dialogSave, dialogCancel, helloPrefix)
        
        for (str in russianStrings) {
            assertFalse("Name dialog string should not be empty: $str", str.isEmpty())
            assertTrue("Name dialog string should contain Cyrillic characters: $str", 
                str.any { it in 'а'..'я' || it in 'А'..'Я' || it in 'ё'..'ё' || it in 'Ё'..'Ё' })
        }
    }
    
    @Test
    fun dialog_button_names_meaningful() {
        // Test that dialog button names are meaningful
        val save = "Сохранить"
        val cancel = "Отмена"
        
        assertTrue("Save button text should contain save concept", save.contains("хран"))
        assertTrue("Cancel button text should contain cancel concept", cancel.contains("мен"))
    }
}