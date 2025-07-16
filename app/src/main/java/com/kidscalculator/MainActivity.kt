package com.kidscalculator

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    
    private lateinit var display: TextView
    private lateinit var tts: TextToSpeech
    
    private var currentInput = ""
    private var operator = ""
    private var operand1 = 0.0
    private var isNewInput = true
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        display = findViewById(R.id.display)
        tts = TextToSpeech(this, this)
        
        setupButtons()
    }
    
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // Try to set Russian language first for child-friendly experience
            val russianResult = tts.setLanguage(Locale("ru", "RU"))
            if (russianResult == TextToSpeech.LANG_MISSING_DATA || russianResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Fallback to default language
                val defaultResult = tts.setLanguage(Locale.getDefault())
                if (defaultResult == TextToSpeech.LANG_MISSING_DATA || defaultResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                    // Final fallback to English
                    tts.setLanguage(Locale.ENGLISH)
                }
            }
            // Set speech rate slower for children
            tts.setSpeechRate(0.8f)
        }
    }
    
    private fun setupButtons() {
        // Number buttons
        val numberButtons = arrayOf(
            R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4,
            R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9
        )
        
        numberButtons.forEachIndexed { index, buttonId ->
            findViewById<Button>(buttonId).setOnClickListener {
                val number = if (buttonId == R.id.btn_0) "0" else index.toString()
                onNumberPressed(number)
                speakText(number)
            }
        }
        
        // Operator buttons
        findViewById<Button>(R.id.btn_plus).setOnClickListener {
            onOperatorPressed("+")
            speakText(getString(R.string.tts_plus))
        }
        
        findViewById<Button>(R.id.btn_minus).setOnClickListener {
            onOperatorPressed("-")
            speakText(getString(R.string.tts_minus))
        }
        
        findViewById<Button>(R.id.btn_multiply).setOnClickListener {
            onOperatorPressed("*")
            speakText(getString(R.string.tts_multiply))
        }
        
        findViewById<Button>(R.id.btn_divide).setOnClickListener {
            onOperatorPressed("/")
            speakText(getString(R.string.tts_divide))
        }
        
        // Equals button
        findViewById<Button>(R.id.btn_equals).setOnClickListener {
            onEqualsPressed()
            speakText(getString(R.string.tts_equals))
        }
        
        // Clear button
        findViewById<Button>(R.id.btn_clear).setOnClickListener {
            onClearPressed()
            speakText(getString(R.string.tts_clear))
        }
    }
    
    private fun onNumberPressed(number: String) {
        if (isNewInput) {
            currentInput = number
            isNewInput = false
        } else {
            currentInput += number
        }
        updateDisplay(currentInput)
    }
    
    private fun onOperatorPressed(op: String) {
        if (currentInput.isNotEmpty()) {
            if (operator.isNotEmpty()) {
                // Perform calculation with previous operator
                onEqualsPressed()
            }
            try {
                operand1 = currentInput.toDouble()
                operator = op
                isNewInput = true
            } catch (e: NumberFormatException) {
                // Handle invalid number format
                currentInput = ""
                updateDisplay("Error")
                speakText(getString(R.string.tts_error))
            }
        }
    }
    
    private fun onEqualsPressed() {
        if (currentInput.isNotEmpty() && operator.isNotEmpty()) {
            try {
                val operand2 = currentInput.toDouble()
                val result = when (operator) {
                    "+" -> operand1 + operand2
                    "-" -> operand1 - operand2
                    "*" -> operand1 * operand2
                    "/" -> {
                        if (operand2 != 0.0) {
                            operand1 / operand2
                        } else {
                            speakText(getString(R.string.tts_division_by_zero))
                            return
                        }
                    }
                    else -> operand2
                }
                
                // Check if result is valid
                if (result.isNaN() || result.isInfinite()) {
                    currentInput = ""
                    updateDisplay("Error")
                    speakText(getString(R.string.tts_error))
                    operator = ""
                    isNewInput = true
                    return
                }
                
                // Format result for display
                val resultText = if (result == result.toInt().toDouble()) {
                    result.toInt().toString()
                } else {
                    String.format("%.2f", result)
                }
                
                currentInput = resultText
                updateDisplay(currentInput)
                
                // Speak the result with Russian pronunciation
                speakText(resultText)
                
                operator = ""
                isNewInput = true
            } catch (e: NumberFormatException) {
                // Handle invalid number format
                currentInput = ""
                updateDisplay("Error")
                speakText(getString(R.string.tts_error))
                operator = ""
                isNewInput = true
            }
        }
    }
    
    private fun onClearPressed() {
        currentInput = ""
        operator = ""
        operand1 = 0.0
        isNewInput = true
        updateDisplay("0")
    }
    
    private fun updateDisplay(text: String) {
        display.text = if (text.isEmpty()) "0" else text
    }
    
    private fun speakText(text: String) {
        try {
            if (::tts.isInitialized) {
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
            }
        } catch (e: Exception) {
            // Silently handle TTS errors - don't crash the app
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
    }
}