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
    private lateinit var calculator: Calculator
    
    companion object {
        private const val PREFS_NAME = "KidsCalculatorPrefs"
        private const val KEY_USER_NAME = "user_name"
        private const val MAX_INPUT_LENGTH = 10 // Maximum digits for child-friendly use
        
        // Keys for saving calculator state
        private const val KEY_CURRENT_INPUT = "current_input"
        private const val KEY_OPERATOR = "operator"
        private const val KEY_OPERAND1 = "operand1"
        private const val KEY_IS_NEW_INPUT = "is_new_input"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        display = findViewById(R.id.display)
        tts = TextToSpeech(this, this)
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        themeManager = ThemeManager(this)
        calculator = Calculator()
        
        // Restore calculator state if available
        savedInstanceState?.let { state ->
            calculator.currentInput = state.getString(KEY_CURRENT_INPUT, "0")
            calculator.operator = state.getString(KEY_OPERATOR, "")
            calculator.operand1 = state.getDouble(KEY_OPERAND1, 0.0)
            calculator.isNewInput = state.getBoolean(KEY_IS_NEW_INPUT, true)
        }
        
        setupButtons()
        applyCurrentTheme()
        updateDisplay()
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
                
                // Speak number or animal sound based on theme
                if (themeManager.isAnimalTheme()) {
                    val animalSound = getAnimalSoundForNumber(number)
                    speakText(animalSound)
                } else {
                    speakText(number)
                }
            }
        }
        
        // Operator buttons
        findViewById<Button>(R.id.btn_plus).setOnClickListener {
            onOperatorPressed("+")
            if (themeManager.isAnimalTheme()) {
                speakText(getString(R.string.tts_animal_plus))
            } else {
                speakText(getString(R.string.tts_plus))
            }
        }
        
        findViewById<Button>(R.id.btn_minus).setOnClickListener {
            onOperatorPressed("-")
            if (themeManager.isAnimalTheme()) {
                speakText(getString(R.string.tts_animal_minus))
            } else {
                speakText(getString(R.string.tts_minus))
            }
        }
        
        findViewById<Button>(R.id.btn_multiply).setOnClickListener {
            onOperatorPressed("*")
            if (themeManager.isAnimalTheme()) {
                speakText(getString(R.string.tts_animal_multiply))
            } else {
                speakText(getString(R.string.tts_multiply))
            }
        }
        
        findViewById<Button>(R.id.btn_divide).setOnClickListener {
            onOperatorPressed("/")
            if (themeManager.isAnimalTheme()) {
                speakText(getString(R.string.tts_animal_divide))
            } else {
                speakText(getString(R.string.tts_divide))
            }
        }
        
        findViewById<Button>(R.id.btn_equals).setOnClickListener {
            onEqualsPressed()
        }
        
        findViewById<Button>(R.id.btn_clear).setOnClickListener {
            onClearPressed()
            if (themeManager.isAnimalTheme()) {
                speakText(getString(R.string.tts_animal_clear))
            } else {
                speakText(getString(R.string.tts_clear))
            }
        }
        
        // Decimal point button
        findViewById<Button>(R.id.btn_decimal).setOnClickListener {
            onDecimalPressed()
            if (themeManager.isAnimalTheme()) {
                speakText(getString(R.string.tts_animal_decimal))
            } else {
                speakText("точка")
            }
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
        calculator.onNumberPressed(number)
        updateDisplay()
    }
    
    private fun onDecimalPressed() {
        calculator.onDecimalPressed()
        updateDisplay()
    }
    
    private fun onOperatorPressed(op: String) {
        calculator.onOperatorPressed(op)
        updateDisplay()
    }
    
    private fun onEqualsPressed() {
        calculator.onEqualsPressed()
        updateDisplay()
        
        // Speak the result
        val result = calculator.currentInput
        if (result.isNotEmpty() && result != "Error") {
            if (themeManager.isAnimalTheme()) {
                speakText("${getString(R.string.tts_animal_equals)} $result")
            } else {
                speakText("${getString(R.string.tts_equals)} $result")
            }
        } else {
            speakText(getString(R.string.tts_error))
        }
    }
    
    private fun onClearPressed() {
        calculator.onClearPressed()
        updateDisplay()
    }
    
    private fun updateDisplay() {
        val displayText = if (calculator.currentInput.isEmpty()) "0" else calculator.currentInput
        display.text = displayText
    }
    
    private fun getAnimalSoundForNumber(number: String): String {
        return when (number) {
            "0" -> getString(R.string.tts_animal_0)
            "1" -> getString(R.string.tts_animal_1)
            "2" -> getString(R.string.tts_animal_2)
            "3" -> getString(R.string.tts_animal_3)
            "4" -> getString(R.string.tts_animal_4)
            "5" -> getString(R.string.tts_animal_5)
            "6" -> getString(R.string.tts_animal_6)
            "7" -> getString(R.string.tts_animal_7)
            "8" -> getString(R.string.tts_animal_8)
            "9" -> getString(R.string.tts_animal_9)
            else -> number
        }
    }
    
    private fun getAnimalEmojiForNumber(number: String): String {
        return when (number) {
            "0" -> "🐴"
            "1" -> "🐱"
            "2" -> "🐶"
            "3" -> "🐸"
            "4" -> "🐮"
            "5" -> "🐷"
            "6" -> "🐔"
            "7" -> "🐑"
            "8" -> "🦆"
            "9" -> "🦆"
            else -> ""
        }
    }
    
    private fun updateButtonTexts() {
        val isAnimal = themeManager.isAnimalTheme()
        
        // Update number buttons with animal emojis in animal theme
        val numberButtons = arrayOf(
            R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4,
            R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9
        )
        
        numberButtons.forEach { buttonId ->
            val button = findViewById<Button>(buttonId)
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
                else -> ""
            }
            
            if (isAnimal) {
                button.text = "$number${getAnimalEmojiForNumber(number)}"
            } else {
                button.text = number
            }
        }
        
        // Update operator buttons with animal emojis in animal theme
        if (isAnimal) {
            findViewById<Button>(R.id.btn_plus).text = "+🐝"
            findViewById<Button>(R.id.btn_minus).text = "-🐺"
            findViewById<Button>(R.id.btn_multiply).text = "×🐯"
            findViewById<Button>(R.id.btn_divide).text = "÷🐘"
            findViewById<Button>(R.id.btn_equals).text = "=🦁"
            findViewById<Button>(R.id.btn_clear).text = "C🐻"
            findViewById<Button>(R.id.btn_decimal).text = ".🐭"
        } else {
            findViewById<Button>(R.id.btn_plus).text = getString(R.string.plus)
            findViewById<Button>(R.id.btn_minus).text = getString(R.string.minus)
            findViewById<Button>(R.id.btn_multiply).text = getString(R.string.multiply)
            findViewById<Button>(R.id.btn_divide).text = getString(R.string.divide)
            findViewById<Button>(R.id.btn_equals).text = getString(R.string.equals)
            findViewById<Button>(R.id.btn_clear).text = getString(R.string.clear)
            findViewById<Button>(R.id.btn_decimal).text = "."
        }
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
            val versionInfo = try {
                val version = packageManager.getPackageInfo(packageName, 0).versionName
                "Version $version"
            } catch (e: Exception) {
                ""
            }
            speakText("${getString(R.string.tts_hello_prefix)} $savedName. $versionInfo")
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
        
        val versionInfo = try {
            "Version ${packageManager.getPackageInfo(packageName, 0).versionName}"
        } catch (e: Exception) {
            ""
        }
        
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_name_title))
            .setMessage(versionInfo)
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
        val isAnimal = themeManager.isAnimalTheme()
        
        // Update main layout background
        val mainLayout = findViewById<android.widget.LinearLayout>(R.id.main_layout)
        mainLayout.setBackgroundColor(getColor(
            when {
                isAnimal -> R.color.animal_background
                isLionKing -> R.color.lion_king_background
                else -> R.color.kid_background
            }
        ))
        
        // Update display background and text color
        display.setBackgroundColor(getColor(
            when {
                isAnimal -> R.color.animal_surface
                isLionKing -> R.color.lion_king_surface
                else -> R.color.kid_surface
            }
        ))
        display.setTextColor(getColor(
            when {
                isAnimal -> R.color.animal_on_surface
                isLionKing -> R.color.lion_king_on_surface
                else -> R.color.kid_on_surface
            }
        ))
        
        // Update number buttons
        val numberButtons = arrayOf(
            R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4,
            R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9, R.id.btn_decimal
        )
        numberButtons.forEach { buttonId ->
            val button = findViewById<Button>(buttonId)
            button.backgroundTintList = getColorStateList(
                when {
                    isAnimal -> R.color.animal_number_button
                    isLionKing -> R.color.lion_king_number_button
                    else -> R.color.number_button
                }
            )
            button.setTextColor(getColor(
                when {
                    isAnimal -> R.color.animal_on_primary
                    isLionKing -> R.color.lion_king_on_primary
                    else -> R.color.kid_on_primary
                }
            ))
        }
        
        // Update operator buttons
        val operatorButtons = arrayOf(R.id.btn_plus, R.id.btn_minus, R.id.btn_multiply, R.id.btn_divide)
        operatorButtons.forEach { buttonId ->
            val button = findViewById<Button>(buttonId)
            button.backgroundTintList = getColorStateList(
                when {
                    isAnimal -> R.color.animal_operator_button
                    isLionKing -> R.color.lion_king_operator_button
                    else -> R.color.operator_button
                }
            )
            button.setTextColor(getColor(
                when {
                    isAnimal -> R.color.animal_on_secondary
                    isLionKing -> R.color.lion_king_on_secondary
                    else -> R.color.kid_on_secondary
                }
            ))
        }
        
        // Update equals button
        findViewById<Button>(R.id.btn_equals).apply {
            backgroundTintList = getColorStateList(
                when {
                    isAnimal -> R.color.animal_equals_button
                    isLionKing -> R.color.lion_king_equals_button
                    else -> R.color.equals_button
                }
            )
            setTextColor(getColor(
                when {
                    isAnimal -> R.color.animal_on_primary
                    isLionKing -> R.color.lion_king_on_primary
                    else -> R.color.kid_on_primary
                }
            ))
        }
        
        // Update clear button
        findViewById<Button>(R.id.btn_clear).apply {
            backgroundTintList = getColorStateList(
                when {
                    isAnimal -> R.color.animal_clear_button
                    isLionKing -> R.color.lion_king_clear_button
                    else -> R.color.clear_button
                }
            )
            setTextColor(getColor(
                when {
                    isAnimal -> R.color.animal_on_error
                    isLionKing -> R.color.lion_king_on_error
                    else -> R.color.kid_on_error
                }
            ))
        }
        
        // Update theme button
        findViewById<Button>(R.id.btn_theme).apply {
            backgroundTintList = getColorStateList(
                when {
                    isAnimal -> R.color.animal_tertiary
                    isLionKing -> R.color.lion_king_tertiary
                    else -> R.color.lion_king_gold
                }
            )
        }
        
        // Update help button
        findViewById<Button>(R.id.btn_help).apply {
            backgroundTintList = getColorStateList(
                when {
                    isAnimal -> R.color.animal_accent
                    isLionKing -> R.color.lion_king_accent
                    else -> R.color.kid_tertiary
                }
            )
        }
        
        // Update button texts with animal emojis
        updateButtonTexts()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
    }
    
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        
        // Save calculator state to preserve across orientation changes
        outState.putString(KEY_CURRENT_INPUT, calculator.currentInput)
        outState.putString(KEY_OPERATOR, calculator.operator)
        outState.putDouble(KEY_OPERAND1, calculator.operand1)
        outState.putBoolean(KEY_IS_NEW_INPUT, calculator.isNewInput)
    }
}