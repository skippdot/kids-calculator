package com.kidscalculator.app

/**
 * Calculator business logic separated from Android Activity
 */
class Calculator {
    
    var currentInput = "0"
    var operator = ""
    var operand1 = 0.0
    var isNewInput = true
    
    fun parseNumber(input: String): Double {
        if (input.isEmpty()) {
            throw NumberFormatException("Empty input")
        }
        
        var normalizedInput = input.replace(",", ".")
        
        // Handle multiple decimal points by keeping only the first one
        val dotIndex = normalizedInput.indexOf('.')
        if (dotIndex != -1) {
            val beforeDot = normalizedInput.substring(0, dotIndex)
            val afterDot = normalizedInput.substring(dotIndex + 1).replace(".", "")
            normalizedInput = beforeDot + "." + afterDot
        }
        
        // Handle case where input starts with decimal point
        if (normalizedInput.startsWith(".")) {
            normalizedInput = "0" + normalizedInput
        }
        
        // Handle case where input ends with decimal point
        if (normalizedInput.endsWith(".")) {
            normalizedInput = normalizedInput.substring(0, normalizedInput.length - 1)
        }
        
        return normalizedInput.toDouble()
    }
    
    fun onNumberPressed(number: String) {
        if (isNewInput) {
            currentInput = number
            isNewInput = false
        } else {
            currentInput += number
        }
    }
    
    fun onDecimalPressed() {
        if (isNewInput) {
            currentInput = "0."
            isNewInput = false
        } else {
            // Don't add decimal point if one already exists (dot or comma)
            if (!currentInput.contains(".") && !currentInput.contains(",")) {
                currentInput += "."
            }
        }
    }
    
    fun onOperatorPressed(op: String) {
        try {
            if (operator.isNotEmpty() && !isNewInput) {
                // Calculate the result of the previous operation
                val operand2 = parseNumber(currentInput)
                val result = calculate(operand1, operator, operand2)
                
                // Check for invalid results
                if (result.isNaN() || result.isInfinite()) {
                    clear()
                    return
                }
                
                currentInput = formatResult(result)
                operand1 = result
            } else {
                operand1 = parseNumber(currentInput)
            }
            
            operator = op
            isNewInput = true
        } catch (e: NumberFormatException) {
            // Invalid input, clear and start fresh
            clear()
        }
    }
    
    fun onEqualsPressed() {
        try {
            if (operator.isNotEmpty() && !isNewInput) {
                val operand2 = parseNumber(currentInput)
                val result = calculate(operand1, operator, operand2)
                
                // Check for invalid results
                if (result.isNaN() || result.isInfinite()) {
                    clear()
                    return
                }
                
                currentInput = formatResult(result)
                operator = ""
                isNewInput = true
            }
        } catch (e: NumberFormatException) {
            // Invalid input, clear and start fresh
            clear()
        }
    }
    
    fun onClearPressed() {
        clear()
    }
    
    private fun clear() {
        currentInput = "0"
        operator = ""
        operand1 = 0.0
        isNewInput = true
    }
    
    private fun calculate(operand1: Double, operator: String, operand2: Double): Double {
        return when (operator) {
            "+" -> operand1 + operand2
            "-" -> operand1 - operand2
            "×", "*" -> operand1 * operand2
            "÷", "/" -> if (operand2 != 0.0) operand1 / operand2 else Double.NaN
            else -> Double.NaN
        }
    }
    
    private fun formatResult(result: Double): String {
        return if (result == result.toLong().toDouble()) {
            result.toLong().toString()
        } else {
            String.format("%.2f", result)
        }
    }
}