package com.kidscalculator.app

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    
    private lateinit var display: TextView
    private lateinit var tts: TextToSpeech
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var themeManager: ThemeManager
    
    private var currentInput = ""
    private var operator = ""
    private var operand1 = 0.0
    private var isNewInput = true
    
    companion object {
        private const val PREFS_NAME = "KidsCalculatorPrefs"
        private const val KEY_USER_NAME = "user_name"
        private const val MAX_INPUT_LENGTH = 10 // Maximum digits for child-friendly use
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        display = findViewById(R.id.display)
        tts = TextToSpeech(this, this)
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        themeManager = ThemeManager(this)
        
        setupButtons()
        applyCurrentTheme()
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
        } else {
            // TTS failed to initialize - app will still work without voice feedback
            // Could add visual feedback here if needed
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
                val number = when (buttonId) {
                    R.id.btn_0 -> "0"
                    R.id.btn_1 -> "1"
                    R.id.btn_2 -> "2"
                    R.id.btn_3 -> "3"
                    R.id.btn_4 -> "4"
                    R.id.btn_5 -> "5"
                    R.id.btn_6 -> "6"
                    R.id.btn_7 -> "7"
                    R.id.btn_8 -> "8"
                    R.id.btn_9 -> "9"
                    else -> index.toString()
                }
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
        }
        
        // Clear button
        findViewById<Button>(R.id.btn_clear).setOnClickListener {
            onClearPressed()
            speakText(getString(R.string.tts_clear))
        }
        
        // Decimal point button
        findViewById<Button>(R.id.btn_decimal).setOnClickListener {
            onDecimalPressed()
        }
        
        // Help button with name functionality
        val helpButton = findViewById<Button>(R.id.btn_help)
        helpButton.setOnClickListener {
            onHelpButtonClick()
        }
        helpButton.setOnLongClickListener {
            onHelpButtonLongClick()
            true
        }
        
        // Theme switcher button
        findViewById<Button>(R.id.btn_theme).setOnClickListener {
            onThemeButtonClick()
        }
    }
    
    private fun onNumberPressed(number: String) {
        if (isNewInput) {
            currentInput = number
            isNewInput = false
        } else {
            // Prevent input that's too long for children to handle
            if (currentInput.length < MAX_INPUT_LENGTH) {
                currentInput += number
            } else {
                // Give feedback that input is too long
                speakText(getString(R.string.tts_number_too_long))
                return
            }
        }
        updateDisplay(currentInput)
    }
    
    private fun onDecimalPressed() {
        if (isNewInput) {
            currentInput = "0."
            isNewInput = false
        } else {
            // Check input length limit
            if (currentInput.length >= MAX_INPUT_LENGTH) {
                speakText(getString(R.string.tts_number_too_long))
                return
            }
            // Only add decimal point if there isn't one already
            if (!currentInput.contains(".") && !currentInput.contains(",")) {
                currentInput += "."
            }
        }
        updateDisplay(currentInput)
        speakText("точка")
    }
    
    private fun parseNumber(input: String): Double {
        if (input.isEmpty()) {
            throw NumberFormatException("Empty input")
        }
        
        // Validate input length to prevent overflow issues
        if (input.length > MAX_INPUT_LENGTH + 2) { // +2 for decimal point and sign
            throw NumberFormatException("Input too long")
        }
        
        // Replace comma with dot for parsing and handle multiple decimal separators
        var normalizedInput = input.replace(",", ".")
        
        // Remove any extra decimal points (keep only the first one)
        val firstDotIndex = normalizedInput.indexOf('.')
        if (firstDotIndex != -1) {
            val beforeDot = normalizedInput.substring(0, firstDotIndex)
            val afterDot = normalizedInput.substring(firstDotIndex + 1).replace(".", "")
            normalizedInput = "$beforeDot.$afterDot"
        }
        
        // Handle cases where input starts or ends with decimal point
        if (normalizedInput.startsWith(".")) {
            normalizedInput = "0$normalizedInput"
        }
        if (normalizedInput.endsWith(".")) {
            normalizedInput += "0"
        }
        
        return normalizedInput.toDouble()
    }
    
    private fun onOperatorPressed(op: String) {
        if (currentInput.isNotEmpty()) {
            if (operator.isNotEmpty()) {
                // Perform calculation with previous operator
                onEqualsPressed()
            }
            try {
                operand1 = parseNumber(currentInput)
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
                val operand2 = parseNumber(currentInput)
                val result = when (operator) {
                    "+" -> operand1 + operand2
                    "-" -> operand1 - operand2
                    "*" -> operand1 * operand2
                    "/" -> {
                        if (operand2 != 0.0) {
                            operand1 / operand2
                        } else {
                            // Reset calculator state on division by zero
                            currentInput = ""
                            operator = ""
                            operand1 = 0.0
                            isNewInput = true
                            updateDisplay("Error")
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
                
                // Speak "equals [result]" in Russian
                speakText("${getString(R.string.tts_equals)} $resultText")
                
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
    
    private fun onHelpButtonClick() {
        val savedName = sharedPreferences.getString(KEY_USER_NAME, null)
        if (savedName.isNullOrEmpty()) {
            // Provide helpful guidance for first-time users
            speakText(getString(R.string.tts_help))
            showNameDialog()
        } else {
            speakText("${getString(R.string.tts_hello_prefix)} $savedName")
        }
    }
    
    private fun onHelpButtonLongClick() {
        showNameDialog()
    }
    
    private fun showNameDialog() {
        val editText = EditText(this)
        editText.hint = getString(R.string.dialog_name_hint)
        
        // Pre-fill with existing name if available
        val savedName = sharedPreferences.getString(KEY_USER_NAME, null)
        if (!savedName.isNullOrEmpty()) {
            editText.setText(savedName)
        }
        
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_name_title))
            .setView(editText)
            .setPositiveButton(getString(R.string.dialog_name_save)) { _, _ ->
                val name = editText.text.toString().trim()
                if (name.isNotEmpty()) {
                    saveUserName(name)
                    speakText("${getString(R.string.tts_hello_prefix)} $name")
                }
            }
            .setNegativeButton(getString(R.string.dialog_name_cancel), null)
            .show()
    }
    
    private fun saveUserName(name: String) {
        sharedPreferences.edit()
            .putString(KEY_USER_NAME, name)
            .apply()
    }
    
    private fun onThemeButtonClick() {
        val newTheme = themeManager.toggleTheme()
        applyCurrentTheme()
        speakText(getString(R.string.tts_theme_switched))
    }
    
    private fun applyCurrentTheme() {
        val isLionKing = themeManager.isLionKingTheme()
        
        // Update main layout background
        val mainLayout = findViewById<android.widget.LinearLayout>(android.R.id.content).getChildAt(0) as android.widget.LinearLayout
        mainLayout.setBackgroundColor(getColor(
            if (isLionKing) R.color.lion_king_background else R.color.kid_background
        ))
        
        // Update display background and text color
        display.setBackgroundColor(getColor(
            if (isLionKing) R.color.lion_king_surface else R.color.kid_surface
        ))
        display.setTextColor(getColor(
            if (isLionKing) R.color.lion_king_on_surface else R.color.kid_on_surface
        ))
        
        // Update number buttons
        val numberButtons = arrayOf(
            R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4,
            R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9, R.id.btn_decimal
        )
        numberButtons.forEach { buttonId ->
            val button = findViewById<Button>(buttonId)
            button.backgroundTintList = getColorStateList(
                if (isLionKing) R.color.lion_king_number_button else R.color.number_button
            )
            button.setTextColor(getColor(
                if (isLionKing) R.color.lion_king_on_primary else R.color.kid_on_primary
            ))
        }
        
        // Update operator buttons
        val operatorButtons = arrayOf(R.id.btn_plus, R.id.btn_minus, R.id.btn_multiply, R.id.btn_divide)
        operatorButtons.forEach { buttonId ->
            val button = findViewById<Button>(buttonId)
            button.backgroundTintList = getColorStateList(
                if (isLionKing) R.color.lion_king_operator_button else R.color.operator_button
            )
            button.setTextColor(getColor(
                if (isLionKing) R.color.lion_king_on_secondary else R.color.kid_on_secondary
            ))
        }
        
        // Update equals button
        findViewById<Button>(R.id.btn_equals).apply {
            backgroundTintList = getColorStateList(
                if (isLionKing) R.color.lion_king_equals_button else R.color.equals_button
            )
            setTextColor(getColor(
                if (isLionKing) R.color.lion_king_on_primary else R.color.kid_on_primary
            ))
        }
        
        // Update clear button
        findViewById<Button>(R.id.btn_clear).apply {
            backgroundTintList = getColorStateList(
                if (isLionKing) R.color.lion_king_clear_button else R.color.clear_button
            )
            setTextColor(getColor(
                if (isLionKing) R.color.lion_king_on_error else R.color.kid_on_error
            ))
        }
        
        // Update theme button
        findViewById<Button>(R.id.btn_theme).apply {
            backgroundTintList = getColorStateList(
                if (isLionKing) R.color.lion_king_tertiary else R.color.lion_king_gold
            )
        }
        
        // Update help button
        findViewById<Button>(R.id.btn_help).apply {
            backgroundTintList = getColorStateList(
                if (isLionKing) R.color.lion_king_accent else R.color.kid_tertiary
            )
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