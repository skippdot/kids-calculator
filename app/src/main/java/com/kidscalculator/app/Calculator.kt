package com.kidscalculator.app

import java.util.Locale
import kotlin.math.abs

/**
 * Errors the calculator can report to the UI so it can give
 * child-friendly feedback (e.g. via TTS) instead of failing silently.
 */
enum class CalculatorError {
    DIVISION_BY_ZERO,
    NUMBER_TOO_LONG,
    INVALID_NUMBER
}

/**
 * Calculator business logic separated from Android Activity
 */
class Calculator {

    var currentInput = "0"
    var operator = ""
    var operand1 = 0.0
    var isNewInput = true

    /**
     * Set when the last action failed (division by zero, input too long,
     * unparseable number). Cleared at the start of every new action.
     */
    var lastError: CalculatorError? = null
        private set

    companion object {
        /** Maximum number of digits a child can type into one number. */
        const val MAX_INPUT_LENGTH = 10

        /** Largest result magnitude that still fits the 10-digit display. */
        private const val MAX_RESULT_MAGNITUDE = 9_999_999_999.0
    }

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

    /**
     * @return true if the digit was accepted, false if the input is already
     *         at [MAX_INPUT_LENGTH] digits (lastError is set to NUMBER_TOO_LONG).
     */
    fun onNumberPressed(number: String): Boolean {
        lastError = null
        if (isNewInput) {
            currentInput = number
            isNewInput = false
        } else {
            if (digitCount(currentInput) >= MAX_INPUT_LENGTH) {
                lastError = CalculatorError.NUMBER_TOO_LONG
                return false
            }
            currentInput += number
        }
        return true
    }

    fun onDecimalPressed() {
        lastError = null
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
        lastError = null
        try {
            if (operator.isNotEmpty() && !isNewInput) {
                // Calculate the result of the previous operation
                val operand2 = parseNumber(currentInput)
                val result = performCalculation(operator, operand2) ?: return

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
            lastError = CalculatorError.INVALID_NUMBER
        }
    }

    fun onEqualsPressed() {
        lastError = null
        try {
            if (operator.isNotEmpty() && !isNewInput) {
                val operand2 = parseNumber(currentInput)
                val result = performCalculation(operator, operand2) ?: return

                currentInput = formatResult(result)
                operator = ""
                isNewInput = true
            }
        } catch (e: NumberFormatException) {
            // Invalid input, clear and start fresh
            clear()
            lastError = CalculatorError.INVALID_NUMBER
        }
    }

    fun onClearPressed() {
        clear()
        lastError = null
    }

    /**
     * Runs the pending operation, or clears state and records the reason
     * when the result can't be shown. Returns null in that case.
     */
    private fun performCalculation(op: String, operand2: Double): Double? {
        if ((op == "÷" || op == "/") && operand2 == 0.0) {
            clear()
            lastError = CalculatorError.DIVISION_BY_ZERO
            return null
        }

        val result = calculate(operand1, op, operand2)

        if (result.isNaN() || result.isInfinite()) {
            clear()
            lastError = CalculatorError.INVALID_NUMBER
            return null
        }

        if (abs(result) > MAX_RESULT_MAGNITUDE) {
            clear()
            lastError = CalculatorError.NUMBER_TOO_LONG
            return null
        }

        return result
    }

    private fun clear() {
        currentInput = "0"
        operator = ""
        operand1 = 0.0
        isNewInput = true
    }

    private fun digitCount(input: String): Int {
        return input.count { it.isDigit() }
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
            // Locale.US keeps the display consistent with the "." button on
            // devices whose locale uses "," as the decimal separator.
            val formatted = String.format(Locale.US, "%.2f", result)
                .trimEnd('0')
                .trimEnd('.')
            if (formatted == "-0") "0" else formatted
        }
    }
}
