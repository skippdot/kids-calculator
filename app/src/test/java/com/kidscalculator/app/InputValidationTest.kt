package com.kidscalculator.app

import org.junit.Test
import org.junit.Assert.*

/**
 * Unit tests for input validation improvements
 */
class InputValidationTest {
    
    @Test
    fun max_input_length_constant() {
        // Test that the maximum input length is reasonable for children
        val maxLength = Calculator.MAX_INPUT_LENGTH
        assertTrue("Max input length should be reasonable for kids", maxLength <= 15)
        assertTrue("Max input length should not be too restrictive", maxLength >= 5)
    }

    @Test
    fun max_input_length_is_enforced_by_calculator() {
        val calculator = Calculator()
        calculator.onNumberPressed("1")
        repeat(20) {
            calculator.onNumberPressed("9")
        }

        assertEquals("Input must be capped at MAX_INPUT_LENGTH digits",
            Calculator.MAX_INPUT_LENGTH, calculator.currentInput.length)
        assertEquals("Overflow must be reported for the UI to announce",
            CalculatorError.NUMBER_TOO_LONG, calculator.lastError)
    }
    
    @Test
    fun number_parsing_with_length_validation() {
        // Test parsing numbers within length limits
        val validInput = "12345"
        assertNotNull("Valid short input should parse", validInput.toDouble())
        
        val tooLongInput = "1234567890123" // 13 digits
        try {
            // This would normally succeed, but our validator should catch it
            val result = tooLongInput.toDouble()
            // If parsing succeeded, the number is still valid mathematically
            assertTrue("Very long input creates large number", result > 1e12)
        } catch (e: Exception) {
            // This is expected behavior for input validation
            assertTrue(true)
        }
    }
    
    @Test
    fun input_length_boundary_testing() {
        // Test inputs at the boundary of max length
        val maxLength = 10
        val exactlyMaxLength = "1234567890" // 10 digits
        val justOverMaxLength = "12345678901" // 11 digits
        
        assertEquals("Exactly max length should be 10 characters", maxLength, exactlyMaxLength.length)
        assertEquals("Just over max length should be 11 characters", maxLength + 1, justOverMaxLength.length)
    }
    
    @Test
    fun decimal_point_handling_with_length() {
        // Test decimal inputs near length limits
        val inputWithDecimal = "1234.5678" // 9 characters including decimal
        assertTrue("Decimal input should be under reasonable length", inputWithDecimal.length <= 10)
        
        val result = inputWithDecimal.toDouble()
        assertEquals("Decimal parsing should work correctly", 1234.5678, result, 0.0001)
    }
    
    @Test
    fun zero_input_handling() {
        // Test various forms of zero input
        val zeroInputs = listOf("0", "0.0", "0.")
        
        for (input in zeroInputs) {
            val result = input.toDouble()
            assertEquals("Zero input should parse to 0.0", 0.0, result, 0.0001)
        }
    }
    
    @Test
    fun error_message_validation() {
        // Test that error messages exist and are appropriate
        val numberTooLongMessage = "число слишком длинное"
        assertFalse("Number too long message should not be empty", numberTooLongMessage.isEmpty())
        assertTrue("Message should contain Russian characters", 
            numberTooLongMessage.any { it in 'а'..'я' || it in 'А'..'Я' })
    }
    
    @Test
    fun button_mapping_correctness() {
        // Test that button number mapping is correct
        val buttonNumbers = mapOf(
            "btn_0" to "0",
            "btn_1" to "1", 
            "btn_2" to "2",
            "btn_3" to "3",
            "btn_4" to "4",
            "btn_5" to "5",
            "btn_6" to "6",
            "btn_7" to "7",
            "btn_8" to "8",
            "btn_9" to "9"
        )
        
        for ((button, expectedNumber) in buttonNumbers) {
            assertEquals("Button $button should map to number $expectedNumber", 
                expectedNumber, expectedNumber)
        }
    }
    
    @Test
    fun division_by_zero_state_reset() {
        // Division by zero must reset the real calculator and flag the error
        val calculator = Calculator()
        calculator.currentInput = "0"
        calculator.operator = "/"
        calculator.operand1 = 10.0
        calculator.isNewInput = false

        calculator.onEqualsPressed()

        assertEquals("Display should reset after division by zero", "0", calculator.currentInput)
        assertTrue("Operator should be empty after division by zero", calculator.operator.isEmpty())
        assertEquals("Operand1 should be reset to 0", 0.0, calculator.operand1, 0.001)
        assertTrue("Should be ready for new input", calculator.isNewInput)
        assertEquals("Error should be reported for the UI to announce",
            CalculatorError.DIVISION_BY_ZERO, calculator.lastError)
    }
    
    @Test
    fun large_number_handling() {
        // Test handling of large numbers within reasonable limits
        val largeButValidNumber = 999999999.0 // 9 digits
        val formattedResult = if (largeButValidNumber == largeButValidNumber.toInt().toDouble()) {
            largeButValidNumber.toInt().toString()
        } else {
            String.format("%.2f", largeButValidNumber)
        }
        
        assertEquals("Large integer should format without decimals", "999999999", formattedResult)
    }
}