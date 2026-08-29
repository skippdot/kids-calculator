package com.kidscalculator.app

import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mockito.*

/**
 * Unit tests for calculator business logic
 * Tests the core calculator methods
 */
class CalculatorTest {
    
    private lateinit var calculator: Calculator
    
    @Before
    fun setup() {
        calculator = Calculator()
    }
    
    // Test parseNumber method directly
    @Test
    fun parseNumber_with_dot_should_work() {
        val result = calculator.parseNumber("12.34")
        assertEquals(12.34, result, 0.001)
    }
    
    @Test
    fun parseNumber_with_comma_should_work() {
        val result = calculator.parseNumber("12,34")
        assertEquals(12.34, result, 0.001)
    }
    
    @Test
    fun parseNumber_with_leading_dot_should_work() {
        val result = calculator.parseNumber(".5")
        assertEquals(0.5, result, 0.001)
    }
    
    @Test
    fun parseNumber_with_trailing_dot_should_work() {
        val result = calculator.parseNumber("5.")
        assertEquals(5.0, result, 0.001)
    }
    
    @Test
    fun parseNumber_with_multiple_dots_should_keep_first() {
        val result = calculator.parseNumber("12.34.56")
        assertEquals(12.3456, result, 0.001)
    }
    
    @Test
    fun parseNumber_with_empty_string_should_throw_exception() {
        try {
            calculator.parseNumber("")
            fail("Should have thrown NumberFormatException")
        } catch (e: NumberFormatException) {
            // Expected
        }
    }
    
    @Test
    fun parseNumber_with_invalid_characters_should_throw_exception() {
        try {
            calculator.parseNumber("12a34")
            fail("Should have thrown NumberFormatException")
        } catch (e: NumberFormatException) {
            // Expected
        }
    }
    
    @Test
    fun parseNumber_with_complex_comma_case() {
        // Test the specific case that was crashing: "15455723,63"
        val result = calculator.parseNumber("15455723,63")
        assertEquals(15455723.63, result, 0.001)
    }
    
    // Test calculator logic flows
    @Test
    fun calculator_addition_flow() {
        // Simulate: enter 2, press +, enter 3, press =
        calculator.currentInput = "2"
        calculator.operator = ""
        calculator.operand1 = 0.0
        calculator.isNewInput = true
        
        // Simulate operator button press
        calculator.onOperatorPressed("+")
        
        // Verify operand1 was set correctly
        assertEquals(2.0, calculator.operand1, 0.001)
        assertEquals("+", calculator.operator)
        assertTrue(calculator.isNewInput)
        
        // Simulate entering second number
        calculator.currentInput = "3"
        calculator.isNewInput = false
        
        // Simulate equals button press
        calculator.onEqualsPressed()
        
        // Verify result
        assertEquals("5", calculator.currentInput)
        assertTrue(calculator.isNewInput)
        assertEquals("", calculator.operator)
    }
    
    @Test
    fun calculator_subtraction_flow() {
        calculator.currentInput = "10"
        calculator.operator = ""
        calculator.operand1 = 0.0
        calculator.isNewInput = true
        
        calculator.onOperatorPressed("-")
        calculator.currentInput = "3"
        calculator.isNewInput = false
        
        calculator.onEqualsPressed()
        
        assertEquals("7", calculator.currentInput)
    }
    
    @Test
    fun calculator_multiplication_flow() {
        calculator.currentInput = "4"
        calculator.operator = ""
        calculator.operand1 = 0.0
        calculator.isNewInput = true
        
        calculator.onOperatorPressed("*")
        calculator.currentInput = "3"
        calculator.isNewInput = false
        
        calculator.onEqualsPressed()
        
        assertEquals("12", calculator.currentInput)
    }
    
    @Test
    fun calculator_division_flow() {
        calculator.currentInput = "10"
        calculator.operator = ""
        calculator.operand1 = 0.0
        calculator.isNewInput = true
        
        calculator.onOperatorPressed("/")
        calculator.currentInput = "2"
        calculator.isNewInput = false
        
        calculator.onEqualsPressed()
        
        assertEquals("5", calculator.currentInput)
    }
    
    @Test
    fun calculator_division_by_zero_should_be_handled() {
        calculator.currentInput = "10"
        calculator.operator = ""
        calculator.operand1 = 0.0
        calculator.isNewInput = true
        
        calculator.onOperatorPressed("/")
        calculator.currentInput = "0"
        calculator.isNewInput = false
        
        calculator.onEqualsPressed()
        
        // Should not crash - clear state
        assertEquals("0", calculator.currentInput)
    }
    
    @Test
    fun calculator_decimal_formatting_integer_result() {
        calculator.currentInput = "4"
        calculator.operator = ""
        calculator.operand1 = 0.0
        calculator.isNewInput = true
        
        calculator.onOperatorPressed("+")
        calculator.currentInput = "1"
        calculator.isNewInput = false
        
        calculator.onEqualsPressed()
        
        // Should format as integer, not decimal
        assertEquals("5", calculator.currentInput)
    }
    
    @Test
    fun calculator_decimal_formatting_decimal_result() {
        calculator.currentInput = "5"
        calculator.operator = ""
        calculator.operand1 = 0.0
        calculator.isNewInput = true
        
        calculator.onOperatorPressed("/")
        calculator.currentInput = "2"
        calculator.isNewInput = false
        
        calculator.onEqualsPressed()
        
        // Should format as decimal without trailing zeros
        assertEquals("2.5", calculator.currentInput)
    }
    
    @Test
    fun calculator_chained_operations() {
        // Test: 2 + 3 * 4 = 20 (left to right evaluation)
        calculator.currentInput = "2"
        calculator.operator = ""
        calculator.operand1 = 0.0
        calculator.isNewInput = true
        
        calculator.onOperatorPressed("+")
        calculator.currentInput = "3"
        calculator.isNewInput = false
        
        // When we press another operator, it should calculate 2+3=5 first
        calculator.onOperatorPressed("*")
        assertEquals("5", calculator.currentInput)
        assertEquals(5.0, calculator.operand1, 0.001)
        assertEquals("*", calculator.operator)
        
        calculator.currentInput = "4"
        calculator.isNewInput = false
        
        calculator.onEqualsPressed()
        assertEquals("20", calculator.currentInput)
    }
    
    @Test
    fun calculator_clear_resets_state() {
        calculator.currentInput = "123"
        calculator.operator = "+"
        calculator.operand1 = 456.0
        calculator.isNewInput = false
        
        calculator.onClearPressed()
        
        assertEquals("0", calculator.currentInput)
        assertEquals("", calculator.operator)
        assertEquals(0.0, calculator.operand1, 0.001)
        assertTrue(calculator.isNewInput)
    }
    
    @Test
    fun calculator_number_input_new_input() {
        calculator.currentInput = "previous"
        calculator.isNewInput = true
        
        calculator.onNumberPressed("5")
        
        assertEquals("5", calculator.currentInput)
        assertFalse(calculator.isNewInput)
    }
    
    @Test
    fun calculator_number_input_append() {
        calculator.currentInput = "12"
        calculator.isNewInput = false
        
        calculator.onNumberPressed("3")
        
        assertEquals("123", calculator.currentInput)
        assertFalse(calculator.isNewInput)
    }
    
    @Test
    fun calculator_decimal_input_new_input() {
        calculator.currentInput = "previous"
        calculator.isNewInput = true
        
        calculator.onDecimalPressed()
        
        assertEquals("0.", calculator.currentInput)
        assertFalse(calculator.isNewInput)
    }
    
    @Test
    fun calculator_decimal_input_append() {
        calculator.currentInput = "12"
        calculator.isNewInput = false
        
        calculator.onDecimalPressed()
        
        assertEquals("12.", calculator.currentInput)
        assertFalse(calculator.isNewInput)
    }
    
    @Test
    fun calculator_decimal_input_already_has_decimal() {
        calculator.currentInput = "12.34"
        calculator.isNewInput = false
        
        calculator.onDecimalPressed()
        
        // Should not add another decimal point
        assertEquals("12.34", calculator.currentInput)
    }
    
    @Test
    fun calculator_decimal_input_already_has_comma() {
        calculator.currentInput = "12,34"
        calculator.isNewInput = false
        
        calculator.onDecimalPressed()
        
        // Should not add decimal point if comma already exists
        assertEquals("12,34", calculator.currentInput)
    }
    
    @Test
    fun calculator_error_handling_invalid_number_format() {
        calculator.currentInput = "invalid"
        calculator.operator = ""
        calculator.operand1 = 0.0
        calculator.isNewInput = true
        
        calculator.onOperatorPressed("+")
        
        // Should handle the error gracefully
        assertEquals("0", calculator.currentInput)
    }
    
    @Test
    fun calculator_error_handling_invalid_equals() {
        calculator.currentInput = "invalid"
        calculator.operator = "+"
        calculator.operand1 = 5.0
        calculator.isNewInput = false
        
        calculator.onEqualsPressed()
        
        // Should handle the error gracefully
        assertEquals("0", calculator.currentInput)
        assertEquals("", calculator.operator)
        assertTrue(calculator.isNewInput)
    }
    
    @Test
    fun calculator_nan_and_infinity_handling() {
        // Test result validation in onEqualsPressed
        calculator.currentInput = "0"
        calculator.operator = "/"
        calculator.operand1 = 0.0
        calculator.isNewInput = false

        calculator.onEqualsPressed()

        // Should handle NaN result
        assertEquals("0", calculator.currentInput)
        assertEquals("", calculator.operator)
        assertTrue(calculator.isNewInput)
    }

    // Tests for error reporting (lastError)

    @Test
    fun division_by_zero_sets_error_state() {
        calculator.currentInput = "10"
        calculator.isNewInput = true
        calculator.onOperatorPressed("/")
        calculator.currentInput = "0"
        calculator.isNewInput = false

        calculator.onEqualsPressed()

        assertEquals(CalculatorError.DIVISION_BY_ZERO, calculator.lastError)
        assertEquals("0", calculator.currentInput)
    }

    @Test
    fun division_by_zero_error_set_when_chaining_operators() {
        calculator.currentInput = "10"
        calculator.isNewInput = true
        calculator.onOperatorPressed("/")
        calculator.currentInput = "0"
        calculator.isNewInput = false

        calculator.onOperatorPressed("+")

        assertEquals(CalculatorError.DIVISION_BY_ZERO, calculator.lastError)
        assertEquals("0", calculator.currentInput)
    }

    @Test
    fun division_symbol_by_zero_sets_error_state() {
        // Same as above but with the display symbol "÷"
        calculator.currentInput = "10"
        calculator.isNewInput = true
        calculator.onOperatorPressed("÷")
        calculator.currentInput = "0"
        calculator.isNewInput = false

        calculator.onEqualsPressed()

        assertEquals(CalculatorError.DIVISION_BY_ZERO, calculator.lastError)
    }

    @Test
    fun invalid_input_sets_error_state() {
        calculator.currentInput = "invalid"
        calculator.operator = "+"
        calculator.operand1 = 5.0
        calculator.isNewInput = false

        calculator.onEqualsPressed()

        assertEquals(CalculatorError.INVALID_NUMBER, calculator.lastError)
        assertEquals("0", calculator.currentInput)
    }

    @Test
    fun error_is_cleared_by_next_successful_action() {
        calculator.currentInput = "10"
        calculator.isNewInput = true
        calculator.onOperatorPressed("/")
        calculator.currentInput = "0"
        calculator.isNewInput = false
        calculator.onEqualsPressed()
        assertEquals(CalculatorError.DIVISION_BY_ZERO, calculator.lastError)

        calculator.onNumberPressed("5")

        assertNull(calculator.lastError)
        assertEquals("5", calculator.currentInput)
    }

    @Test
    fun error_is_cleared_by_clear_button() {
        calculator.currentInput = "10"
        calculator.isNewInput = true
        calculator.onOperatorPressed("/")
        calculator.currentInput = "0"
        calculator.isNewInput = false
        calculator.onEqualsPressed()
        assertEquals(CalculatorError.DIVISION_BY_ZERO, calculator.lastError)

        calculator.onClearPressed()

        assertNull(calculator.lastError)
    }

    // Tests for input length limit

    @Test
    fun input_is_limited_to_max_length_digits() {
        calculator.onNumberPressed("1")
        repeat(Calculator.MAX_INPUT_LENGTH - 1) {
            assertTrue(calculator.onNumberPressed("2"))
        }
        assertEquals(Calculator.MAX_INPUT_LENGTH, calculator.currentInput.length)

        // The 11th digit must be rejected
        val accepted = calculator.onNumberPressed("3")

        assertFalse(accepted)
        assertEquals(CalculatorError.NUMBER_TOO_LONG, calculator.lastError)
        assertEquals(Calculator.MAX_INPUT_LENGTH, calculator.currentInput.length)
    }

    @Test
    fun decimal_point_does_not_count_toward_digit_limit() {
        calculator.onNumberPressed("1")
        repeat(Calculator.MAX_INPUT_LENGTH - 2) {
            calculator.onNumberPressed("2")
        }
        calculator.onDecimalPressed()

        // 9 digits + "." so far; one more digit is still allowed
        assertTrue(calculator.onNumberPressed("5"))
        assertFalse(calculator.onNumberPressed("6"))
    }

    // Tests for result formatting and overflow

    @Test
    fun result_trailing_zeros_are_trimmed() {
        // 10 / 4 = 2.50 -> displayed as 2.5
        calculator.currentInput = "10"
        calculator.isNewInput = true
        calculator.onOperatorPressed("/")
        calculator.currentInput = "4"
        calculator.isNewInput = false

        calculator.onEqualsPressed()

        assertEquals("2.5", calculator.currentInput)
    }

    @Test
    fun result_keeps_two_meaningful_decimals() {
        // 1 / 4 = 0.25 -> both decimals kept
        calculator.currentInput = "1"
        calculator.isNewInput = true
        calculator.onOperatorPressed("/")
        calculator.currentInput = "4"
        calculator.isNewInput = false

        calculator.onEqualsPressed()

        assertEquals("0.25", calculator.currentInput)
    }

    @Test
    fun result_too_large_for_display_sets_error() {
        calculator.currentInput = "9999999999"
        calculator.isNewInput = true
        calculator.onOperatorPressed("*")
        calculator.currentInput = "9999999999"
        calculator.isNewInput = false

        calculator.onEqualsPressed()

        assertEquals(CalculatorError.NUMBER_TOO_LONG, calculator.lastError)
        assertEquals("0", calculator.currentInput)
    }

    @Test
    fun unknown_operator_sets_invalid_number_error() {
        calculator.currentInput = "3"
        calculator.operator = "%"
        calculator.operand1 = 5.0
        calculator.isNewInput = false

        calculator.onEqualsPressed()

        assertEquals(CalculatorError.INVALID_NUMBER, calculator.lastError)
        assertEquals("0", calculator.currentInput)
    }

    @Test
    fun negative_results_are_supported() {
        calculator.currentInput = "3"
        calculator.isNewInput = true
        calculator.onOperatorPressed("-")
        calculator.currentInput = "5"
        calculator.isNewInput = false

        calculator.onEqualsPressed()

        assertEquals("-2", calculator.currentInput)
        assertNull(calculator.lastError)
    }
}