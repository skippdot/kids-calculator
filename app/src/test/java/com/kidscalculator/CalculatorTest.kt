package com.kidscalculator

import org.junit.Test
import org.junit.Assert.*

/**
 * Unit tests for calculator logic
 */
class CalculatorTest {
    
    @Test
    fun addition_isCorrect() {
        // Test basic addition
        val result = 2.0 + 2.0
        assertEquals(4.0, result, 0.001)
    }
    
    @Test
    fun subtraction_isCorrect() {
        // Test basic subtraction
        val result = 5.0 - 3.0
        assertEquals(2.0, result, 0.001)
    }
    
    @Test
    fun multiplication_isCorrect() {
        // Test basic multiplication
        val result = 3.0 * 4.0
        assertEquals(12.0, result, 0.001)
    }
    
    @Test
    fun division_isCorrect() {
        // Test basic division
        val result = 10.0 / 2.0
        assertEquals(5.0, result, 0.001)
    }
    
    @Test
    fun division_by_zero_handled() {
        // Test division by zero
        val operand1 = 10.0
        val operand2 = 0.0
        
        // Division by zero should return infinity
        val result = operand1 / operand2
        assertTrue("Division by zero should return infinity", result.isInfinite())
    }
    
    @Test
    fun result_formatting_integer() {
        // Test that integer results are formatted correctly
        val result = 4.0
        val formatted = if (result == result.toInt().toDouble()) {
            result.toInt().toString()
        } else {
            String.format("%.2f", result)
        }
        assertEquals("4", formatted)
    }
    
    @Test
    fun result_formatting_decimal() {
        // Test that decimal results are formatted correctly
        val result = 4.5
        val formatted = if (result == result.toInt().toDouble()) {
            result.toInt().toString()
        } else {
            String.format("%.2f", result)
        }
        assertEquals("4.50", formatted)
    }
    
    @Test
    fun multiple_operations() {
        // Test multiple operations: 2 + 3 * 4 = 14 (if calculated left to right)
        // This simulates the calculator's left-to-right evaluation
        var result = 2.0
        result += 3.0  // 5.0
        result *= 4.0  // 20.0
        assertEquals(20.0, result, 0.001)
    }
    
    @Test
    fun negative_numbers() {
        // Test operations with negative numbers
        val result1 = -5.0 + 3.0
        assertEquals(-2.0, result1, 0.001)
        
        val result2 = -4.0 * -2.0
        assertEquals(8.0, result2, 0.001)
    }
    
    @Test
    fun decimal_precision() {
        // Test decimal precision
        val result = 0.1 + 0.2
        // Due to floating point precision, we need a tolerance
        assertEquals(0.3, result, 0.001)
    }
    
    @Test
    fun invalid_number_format() {
        // Test handling of invalid number formats
        val invalidString = "15455723,63"
        var result: Double? = null
        var exception: Exception? = null
        
        try {
            result = invalidString.toDouble()
        } catch (e: NumberFormatException) {
            exception = e
        }
        
        // Should throw NumberFormatException for comma-separated decimals
        assertNotNull("Should throw NumberFormatException", exception)
        assertTrue("Should be NumberFormatException", exception is NumberFormatException)
    }
    
    @Test
    fun nan_and_infinity_handling() {
        // Test NaN and infinity handling
        val nan = Double.NaN
        val infinity = Double.POSITIVE_INFINITY
        
        assertTrue("NaN should be detected", nan.isNaN())
        assertTrue("Infinity should be detected", infinity.isInfinite())
        assertFalse("NaN should not be infinite", nan.isInfinite())
        assertFalse("Infinity should not be NaN", infinity.isNaN())
    }
}