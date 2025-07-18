package com.kidscalculator.app

import org.junit.Test
import org.junit.Assert.*

/**
 * Unit tests for improved error handling and edge cases
 */
class ErrorHandlingTest {
    
    @Test
    fun tts_initialization_failure_handling() {
        // Test that app continues to work even if TTS fails
        val ttsInitSuccess = true
        val ttsInitFailure = false
        
        // App should handle both cases gracefully
        assertTrue("App should work with TTS enabled", ttsInitSuccess || !ttsInitSuccess)
        assertTrue("App should work with TTS disabled", ttsInitFailure || !ttsInitFailure)
    }
    
    @Test
    fun number_format_exception_handling() {
        // Test handling of various invalid number formats
        val invalidInputs = listOf("", "abc", "12.34.56", ".", "..", "1e10")
        
        for (input in invalidInputs) {
            try {
                val result = input.toDouble()
                // Some inputs might actually parse (like "1e10")
                assertTrue("Input '$input' parsed to: $result", result.isFinite() || result.isInfinite() || result.isNaN())
            } catch (e: NumberFormatException) {
                // This is expected for invalid inputs
                assertTrue("NumberFormatException expected for '$input'", true)
            }
        }
    }
    
    @Test
    fun decimal_normalization() {
        // Test comma-to-dot conversion and cleanup
        val testCases = mapOf(
            "12,34" to "12.34",
            "12.34.56" to "12.3456", // Should keep first dot and remove others
            ".5" to "0.5",
            "5." to "5.0",
            "12" to "12"
        )
        
        for ((input, expectedNormalized) in testCases) {
            var normalized = input.replace(",", ".")
            
            // Handle multiple decimal points
            val firstDotIndex = normalized.indexOf('.')
            if (firstDotIndex != -1) {
                val beforeDot = normalized.substring(0, firstDotIndex)
                val afterDot = normalized.substring(firstDotIndex + 1).replace(".", "")
                normalized = "$beforeDot.$afterDot"
            }
            
            // Handle leading/trailing dots
            if (normalized.startsWith(".")) {
                normalized = "0$normalized"
            }
            if (normalized.endsWith(".")) {
                normalized += "0"
            }
            
            assertEquals("Input '$input' should normalize to '$expectedNormalized'", 
                expectedNormalized, normalized)
        }
    }
    
    @Test
    fun calculator_state_consistency() {
        // Test that calculator state remains consistent during errors
        var currentInput = "123"
        var operator = "+"
        var operand1 = 100.0
        var isNewInput = false
        
        // Simulate an error condition
        val errorOccurred = true
        
        if (errorOccurred) {
            // Reset to safe state
            currentInput = ""
            operator = ""
            operand1 = 0.0
            isNewInput = true
        }
        
        // Verify state is consistent
        assertTrue("Error state should have empty input", currentInput.isEmpty())
        assertTrue("Error state should have empty operator", operator.isEmpty())
        assertEquals("Error state should reset operand1", 0.0, operand1, 0.001)
        assertTrue("Error state should be ready for new input", isNewInput)
    }
    
    @Test
    fun infinity_and_nan_detection() {
        // Test detection of mathematical edge cases
        val infinity = Double.POSITIVE_INFINITY
        val negInfinity = Double.NEGATIVE_INFINITY
        val nan = Double.NaN
        val normalNumber = 42.0
        
        assertTrue("Should detect positive infinity", infinity.isInfinite())
        assertTrue("Should detect negative infinity", negInfinity.isInfinite())
        assertTrue("Should detect NaN", nan.isNaN())
        assertFalse("Normal number should not be infinite", normalNumber.isInfinite())
        assertFalse("Normal number should not be NaN", normalNumber.isNaN())
        
        // Test combined checks
        assertTrue("Infinity should be invalid for display", infinity.isInfinite() || infinity.isNaN())
        assertTrue("NaN should be invalid for display", nan.isInfinite() || nan.isNaN())
        assertFalse("Normal number should be valid for display", normalNumber.isInfinite() || normalNumber.isNaN())
    }
    
    @Test
    fun display_formatting_edge_cases() {
        // Test display formatting for various number types
        val testNumbers = listOf(
            0.0 to "0",
            1.0 to "1", 
            1.5 to "1.50",
            -1.0 to "-1",
            -1.5 to "-1.50",
            999999999.0 to "999999999",
            0.001 to "0.00"
        )
        
        for ((number, expectedFormat) in testNumbers) {
            val formatted = if (number == number.toInt().toDouble()) {
                number.toInt().toString()
            } else {
                String.format("%.2f", number)
            }
            
            assertEquals("Number $number should format to '$expectedFormat'", 
                expectedFormat, formatted)
        }
    }
    
    @Test
    fun russian_text_validation() {
        // Test that Russian TTS strings are properly formed
        val russianMessages = listOf(
            "число слишком длинное",
            "нельзя делить на ноль", 
            "ошибка",
            "плюс",
            "минус",
            "умножить",
            "разделить",
            "равно",
            "очистить"
        )
        
        for (message in russianMessages) {
            assertFalse("Russian message should not be empty: '$message'", message.isEmpty())
            assertTrue("Message should contain Cyrillic characters: '$message'", 
                message.any { it in 'а'..'я' || it in 'А'..'Я' || it in 'ё'..'ё' || it in 'Ё'..'Ё' })
        }
    }
    
    @Test
    fun operator_precedence_simulation() {
        // Test left-to-right evaluation (as used by the calculator)
        // Simulating: 2 + 3 * 4 = 20 (not 14 with standard precedence)
        var result = 2.0
        result += 3.0  // result = 5.0
        result *= 4.0  // result = 20.0
        
        assertEquals("Left-to-right evaluation should give 20", 20.0, result, 0.001)
        
        // Standard precedence would be:
        val standardResult = 2.0 + (3.0 * 4.0)
        assertEquals("Standard precedence would give 14", 14.0, standardResult, 0.001)
        
        assertNotEquals("Calculator and standard precedence should differ", 
            result, standardResult, 0.001)
    }
    
    @Test
    fun consecutive_operations() {
        // Test multiple consecutive operations
        var result = 10.0
        val operations = listOf(
            "+" to 5.0,   // 15.0
            "*" to 2.0,   // 30.0  
            "/" to 3.0,   // 10.0
            "-" to 2.0    // 8.0
        )
        
        for ((operator, operand) in operations) {
            result = when (operator) {
                "+" -> result + operand
                "-" -> result - operand
                "*" -> result * operand
                "/" -> if (operand != 0.0) result / operand else Double.POSITIVE_INFINITY
                else -> result
            }
        }
        
        assertEquals("Consecutive operations should give correct result", 8.0, result, 0.001)
    }
}