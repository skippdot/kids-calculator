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
        assertEquals("0", calculator.currentNumber)
        assertEquals("0", calculator.result)
        assertNull(calculator.operator)
        assertNull(calculator.pendingOperand)
    }
    
    @Test
    fun testNumberInput() {
        calculator.onNumberPressed("5")
        assertEquals("5", calculator.currentNumber)
        
        calculator.onNumberPressed("3")
        assertEquals("53", calculator.currentNumber)
    }
    
    @Test
    fun testClearOperation() {
        calculator.onNumberPressed("5")
        calculator.onNumberPressed("3")
        calculator.onClear()
        
        assertEquals("0", calculator.currentNumber)
        assertEquals("0", calculator.result)
        assertNull(calculator.operator)
        assertNull(calculator.pendingOperand)
    }
    
    @Test
    fun testAdditionOperation() {
        calculator.onNumberPressed("2")
        calculator.onOperatorPressed("+")
        calculator.onNumberPressed("3")
        calculator.onEqualsPressed()
        
        assertEquals("5", calculator.result)
    }
    
    @Test
    fun testSubtractionOperation() {
        calculator.onNumberPressed("5")
        calculator.onOperatorPressed("-")
        calculator.onNumberPressed("2")
        calculator.onEqualsPressed()
        
        assertEquals("3", calculator.result)
    }
    
    @Test
    fun testMultiplicationOperation() {
        calculator.onNumberPressed("3")
        calculator.onOperatorPressed("×")
        calculator.onNumberPressed("4")
        calculator.onEqualsPressed()
        
        assertEquals("12", calculator.result)
    }
    
    @Test
    fun testDivisionOperation() {
        calculator.onNumberPressed("8")
        calculator.onOperatorPressed("÷")
        calculator.onNumberPressed("2")
        calculator.onEqualsPressed()
        
        assertEquals("4", calculator.result)
    }
    
    @Test
    fun testDivisionByZero() {
        calculator.onNumberPressed("5")
        calculator.onOperatorPressed("÷")
        calculator.onNumberPressed("0")
        calculator.onEqualsPressed()
        
        assertEquals("0", calculator.result)
    }
    
    @Test
    fun testDecimalInput() {
        calculator.onNumberPressed("5")
        calculator.onDecimalPressed()
        calculator.onNumberPressed("2")
        
        assertEquals("5.2", calculator.currentNumber)
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
        
        assertEquals("20", calculator.result)
    }
    
    @Test
    fun testMultipleDecimalPrevention() {
        calculator.onNumberPressed("5")
        calculator.onDecimalPressed()
        calculator.onNumberPressed("2")
        calculator.onDecimalPressed()
        calculator.onNumberPressed("3")
        
        assertEquals("5.23", calculator.currentNumber)
    }
}