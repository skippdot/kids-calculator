package com.kidscalculator.app

import org.junit.Test
import org.junit.Assert.*
import org.junit.Before

class MainActivityUnitTest {
    
    private lateinit var calculator: Calculator
    
    @Before
    fun setup() {
        calculator = Calculator()
    }
    
    @Test
    fun testCalculatorInitialization() {
        assertEquals("", calculator.currentInput)
        assertEquals("", calculator.operator)
        assertEquals(0.0, calculator.operand1, 0.001)
        assertTrue(calculator.isNewInput)
    }
    
    @Test
    fun testNumberInput() {
        calculator.onNumberPressed("5")
        assertEquals("5", calculator.currentInput)
        
        calculator.onNumberPressed("3")
        assertEquals("53", calculator.currentInput)
    }
    
    @Test
    fun testClearOperation() {
        calculator.onNumberPressed("5")
        calculator.onNumberPressed("3")
        calculator.onClearPressed()
        
        assertEquals("", calculator.currentInput)
        assertEquals("", calculator.operator)
        assertEquals(0.0, calculator.operand1, 0.001)
        assertTrue(calculator.isNewInput)
    }
    
    @Test
    fun testAdditionOperation() {
        calculator.onNumberPressed("2")
        calculator.onOperatorPressed("+")
        calculator.onNumberPressed("3")
        calculator.onEqualsPressed()
        
        assertEquals("5", calculator.currentInput)
    }
    
    @Test
    fun testSubtractionOperation() {
        calculator.onNumberPressed("5")
        calculator.onOperatorPressed("-")
        calculator.onNumberPressed("2")
        calculator.onEqualsPressed()
        
        assertEquals("3", calculator.currentInput)
    }
    
    @Test
    fun testMultiplicationOperation() {
        calculator.onNumberPressed("3")
        calculator.onOperatorPressed("×")
        calculator.onNumberPressed("4")
        calculator.onEqualsPressed()
        
        assertEquals("12", calculator.currentInput)
    }
    
    @Test
    fun testDivisionOperation() {
        calculator.onNumberPressed("8")
        calculator.onOperatorPressed("÷")
        calculator.onNumberPressed("2")
        calculator.onEqualsPressed()
        
        assertEquals("4", calculator.currentInput)
    }
    
    @Test
    fun testDivisionByZero() {
        calculator.onNumberPressed("5")
        calculator.onOperatorPressed("÷")
        calculator.onNumberPressed("0")
        calculator.onEqualsPressed()
        
        // After division by zero, calculator clears
        assertEquals("", calculator.currentInput)
    }
    
    @Test
    fun testDecimalInput() {
        calculator.onNumberPressed("5")
        calculator.onDecimalPressed()
        calculator.onNumberPressed("2")
        
        assertEquals("5.2", calculator.currentInput)
    }
    
    @Test
    fun testChainedOperations() {
        // 2 + 3 * 4 = 20 (left to right evaluation)
        calculator.onNumberPressed("2")
        calculator.onOperatorPressed("+")
        calculator.onNumberPressed("3")
        calculator.onOperatorPressed("×")
        calculator.onNumberPressed("4")
        calculator.onEqualsPressed()
        
        assertEquals("20", calculator.currentInput)
    }
    
    @Test
    fun testMultipleDecimalPrevention() {
        calculator.onNumberPressed("5")
        calculator.onDecimalPressed()
        calculator.onNumberPressed("2")
        calculator.onDecimalPressed()
        calculator.onNumberPressed("3")
        
        assertEquals("5.23", calculator.currentInput)
    }
}