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
        
        // Should format as decimal
        assertEquals("2.50", calculator.currentInput)
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
}