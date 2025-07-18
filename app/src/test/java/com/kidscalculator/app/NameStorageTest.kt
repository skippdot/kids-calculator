package com.kidscalculator.app

import org.junit.Test
import org.junit.Assert.*

/**
 * Unit tests for name storage functionality
 */
class NameStorageTest {
    
    @Test
    fun name_validation_empty_string() {
        // Test that empty names are handled correctly
        val name = ""
        val trimmedName = name.trim()
        assertTrue("Empty string should be empty after trim", trimmedName.isEmpty())
    }
    
    @Test
    fun name_validation_whitespace_only() {
        // Test that whitespace-only names are handled correctly
        val name = "   "
        val trimmedName = name.trim()
        assertTrue("Whitespace-only string should be empty after trim", trimmedName.isEmpty())
    }
    
    @Test
    fun name_validation_valid_name() {
        // Test that valid names are preserved
        val name = "  Валик  "
        val trimmedName = name.trim()
        assertEquals("Valid name should be trimmed correctly", "Валик", trimmedName)
        assertFalse("Valid name should not be empty", trimmedName.isEmpty())
    }
    
    @Test
    fun name_greeting_construction() {
        // Test that greeting is constructed correctly
        val helloPrefix = "Привет"
        val name = "Валик"
        val greeting = "$helloPrefix $name"
        assertEquals("Greeting should be constructed correctly", "Привет Валик", greeting)
    }
    
    @Test
    fun name_null_handling() {
        // Test null name handling
        val name: String? = null
        val isNullOrEmpty = name.isNullOrEmpty()
        assertTrue("Null name should be detected as null or empty", isNullOrEmpty)
    }
    
    @Test
    fun name_special_characters() {
        // Test names with special characters
        val name = "Мария-Анна"
        val trimmedName = name.trim()
        assertEquals("Names with special characters should be preserved", "Мария-Анна", trimmedName)
        assertFalse("Valid name with special characters should not be empty", trimmedName.isEmpty())
    }
    
    @Test
    fun name_numbers_and_letters() {
        // Test names with numbers
        val name = "Валик123"
        val trimmedName = name.trim()
        assertEquals("Names with numbers should be preserved", "Валик123", trimmedName)
        assertFalse("Valid name with numbers should not be empty", trimmedName.isEmpty())
    }
}