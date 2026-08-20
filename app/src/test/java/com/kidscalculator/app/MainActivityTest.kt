package com.kidscalculator.app

import android.content.res.Configuration
import android.widget.Button
import android.widget.TextView
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.android.controller.ActivityController

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33], manifest = Config.NONE)
class MainActivityTest {
    
    private lateinit var controller: ActivityController<MainActivity>
    private lateinit var activity: MainActivity
    private lateinit var display: TextView
    
    @Before
    fun setup() {
        controller = Robolectric.buildActivity(MainActivity::class.java)
        activity = controller
            .create()
            .start()
            .resume()
            .visible()
            .get()
            
        display = activity.findViewById(R.id.display)
    }
    
    @Test
    fun activity_should_initialize_correctly() {
        assertNotNull(activity)
        assertNotNull(display)
        assertEquals("0", display.text.toString())
    }
    
    @Test
    fun activity_should_not_crash_on_theme_application() {
        // This test should verify that applyCurrentTheme doesn't crash
        // Currently it will fail due to ClassCastException
        val controller = Robolectric.buildActivity(MainActivity::class.java)
        
        // The create() call triggers onCreate which calls applyCurrentTheme
        // This should not throw any exception
        val activity = controller.create().get()
        assertNotNull("Activity should be created without crashing", activity)
    }
    
    @Test
    fun number_button_should_update_display() {
        val btn5 = activity.findViewById<Button>(R.id.btn_5)
        btn5.performClick()
        
        assertEquals("5", display.text.toString())
    }
    
    @Test
    fun multiple_number_buttons_should_concatenate() {
        val btn1 = activity.findViewById<Button>(R.id.btn_1)
        val btn2 = activity.findViewById<Button>(R.id.btn_2)
        val btn3 = activity.findViewById<Button>(R.id.btn_3)
        
        btn1.performClick()
        btn2.performClick()
        btn3.performClick()
        
        assertEquals("123", display.text.toString())
    }
    
    @Test
    fun clear_button_should_reset_display() {
        val btn5 = activity.findViewById<Button>(R.id.btn_5)
        val btnClear = activity.findViewById<Button>(R.id.btn_clear)
        
        btn5.performClick()
        assertEquals("5", display.text.toString())
        
        btnClear.performClick()
        assertEquals("0", display.text.toString())
    }
    
    @Test
    fun addition_operation_should_work() {
        val btn2 = activity.findViewById<Button>(R.id.btn_2)
        val btn3 = activity.findViewById<Button>(R.id.btn_3)
        val btnPlus = activity.findViewById<Button>(R.id.btn_plus)
        val btnEquals = activity.findViewById<Button>(R.id.btn_equals)
        
        btn2.performClick()
        btnPlus.performClick()
        btn3.performClick()
        btnEquals.performClick()
        
        assertEquals("5", display.text.toString())
    }
    
    @Test
    fun subtraction_operation_should_work() {
        val btn5 = activity.findViewById<Button>(R.id.btn_5)
        val btn2 = activity.findViewById<Button>(R.id.btn_2)
        val btnMinus = activity.findViewById<Button>(R.id.btn_minus)
        val btnEquals = activity.findViewById<Button>(R.id.btn_equals)
        
        btn5.performClick()
        btnMinus.performClick()
        btn2.performClick()
        btnEquals.performClick()
        
        assertEquals("3", display.text.toString())
    }
    
    @Test
    fun multiplication_operation_should_work() {
        val btn3 = activity.findViewById<Button>(R.id.btn_3)
        val btn4 = activity.findViewById<Button>(R.id.btn_4)
        val btnMultiply = activity.findViewById<Button>(R.id.btn_multiply)
        val btnEquals = activity.findViewById<Button>(R.id.btn_equals)
        
        btn3.performClick()
        btnMultiply.performClick()
        btn4.performClick()
        btnEquals.performClick()
        
        assertEquals("12", display.text.toString())
    }
    
    @Test
    fun division_operation_should_work() {
        val btn8 = activity.findViewById<Button>(R.id.btn_8)
        val btn2 = activity.findViewById<Button>(R.id.btn_2)
        val btnDivide = activity.findViewById<Button>(R.id.btn_divide)
        val btnEquals = activity.findViewById<Button>(R.id.btn_equals)
        
        btn8.performClick()
        btnDivide.performClick()
        btn2.performClick()
        btnEquals.performClick()
        
        assertEquals("4", display.text.toString())
    }
    
    @Test
    fun division_by_zero_should_be_handled() {
        val btn5 = activity.findViewById<Button>(R.id.btn_5)
        val btn0 = activity.findViewById<Button>(R.id.btn_0)
        val btnDivide = activity.findViewById<Button>(R.id.btn_divide)
        val btnEquals = activity.findViewById<Button>(R.id.btn_equals)
        
        btn5.performClick()
        btnDivide.performClick()
        btn0.performClick()
        btnEquals.performClick()
        
        // Division by zero should show error or clear
        assertTrue(display.text.toString() == "Error" || display.text.toString() == "0")
    }
    
    @Test
    fun decimal_button_should_add_decimal_point() {
        val btn5 = activity.findViewById<Button>(R.id.btn_5)
        val btnDecimal = activity.findViewById<Button>(R.id.btn_decimal)
        val btn2 = activity.findViewById<Button>(R.id.btn_2)
        
        btn5.performClick()
        btnDecimal.performClick()
        btn2.performClick()
        
        assertEquals("5.2", display.text.toString())
    }
    
    @Test
    fun chained_operations_should_work() {
        val btn2 = activity.findViewById<Button>(R.id.btn_2)
        val btn3 = activity.findViewById<Button>(R.id.btn_3)
        val btn4 = activity.findViewById<Button>(R.id.btn_4)
        val btnPlus = activity.findViewById<Button>(R.id.btn_plus)
        val btnMultiply = activity.findViewById<Button>(R.id.btn_multiply)
        val btnEquals = activity.findViewById<Button>(R.id.btn_equals)
        
        // 2 + 3 * 4 = 20 (left to right evaluation)
        btn2.performClick()
        btnPlus.performClick()
        btn3.performClick()
        btnMultiply.performClick()
        btn4.performClick()
        btnEquals.performClick()
        
        assertEquals("20", display.text.toString())
    }
    
    @Test
    fun all_required_buttons_should_exist_in_portrait() {
        // Verify all essential UI elements exist in portrait mode
        assertNotNull("Display should exist", activity.findViewById<TextView>(R.id.display))
        
        // Number buttons
        for (i in 0..9) {
            val buttonId = when (i) {
                0 -> R.id.btn_0
                1 -> R.id.btn_1
                2 -> R.id.btn_2
                3 -> R.id.btn_3
                4 -> R.id.btn_4
                5 -> R.id.btn_5
                6 -> R.id.btn_6
                7 -> R.id.btn_7
                8 -> R.id.btn_8
                9 -> R.id.btn_9
                else -> R.id.btn_0
            }
            assertNotNull("Number button $i should exist", activity.findViewById<Button>(buttonId))
        }
        
        // Operator buttons
        assertNotNull("Plus button should exist", activity.findViewById<Button>(R.id.btn_plus))
        assertNotNull("Minus button should exist", activity.findViewById<Button>(R.id.btn_minus))
        assertNotNull("Multiply button should exist", activity.findViewById<Button>(R.id.btn_multiply))
        assertNotNull("Divide button should exist", activity.findViewById<Button>(R.id.btn_divide))
        assertNotNull("Equals button should exist", activity.findViewById<Button>(R.id.btn_equals))
        
        // Other buttons
        assertNotNull("Clear button should exist", activity.findViewById<Button>(R.id.btn_clear))
        assertNotNull("Decimal button should exist", activity.findViewById<Button>(R.id.btn_decimal))
        assertNotNull("Help button should exist", activity.findViewById<Button>(R.id.btn_help))
        assertNotNull("Theme button should exist", activity.findViewById<Button>(R.id.btn_theme))
    }
    
    @Test
    @Config(qualifiers = "land")
    fun all_required_buttons_should_exist_in_landscape() {
        // Create activity in landscape mode
        val landscapeController = Robolectric.buildActivity(MainActivity::class.java)
        val landscapeActivity = landscapeController
            .create()
            .start()
            .resume()
            .visible()
            .get()
            
        // Verify all essential UI elements exist in landscape mode
        assertNotNull("Display should exist in landscape", landscapeActivity.findViewById<TextView>(R.id.display))
        
        // Number buttons
        for (i in 0..9) {
            val buttonId = when (i) {
                0 -> R.id.btn_0
                1 -> R.id.btn_1
                2 -> R.id.btn_2
                3 -> R.id.btn_3
                4 -> R.id.btn_4
                5 -> R.id.btn_5
                6 -> R.id.btn_6
                7 -> R.id.btn_7
                8 -> R.id.btn_8
                9 -> R.id.btn_9
                else -> R.id.btn_0
            }
            assertNotNull("Number button $i should exist in landscape", landscapeActivity.findViewById<Button>(buttonId))
        }
        
        // Operator buttons
        assertNotNull("Plus button should exist in landscape", landscapeActivity.findViewById<Button>(R.id.btn_plus))
        assertNotNull("Minus button should exist in landscape", landscapeActivity.findViewById<Button>(R.id.btn_minus))
        assertNotNull("Multiply button should exist in landscape", landscapeActivity.findViewById<Button>(R.id.btn_multiply))
        assertNotNull("Divide button should exist in landscape", landscapeActivity.findViewById<Button>(R.id.btn_divide))
        assertNotNull("Equals button should exist in landscape", landscapeActivity.findViewById<Button>(R.id.btn_equals))
        
        // Other buttons
        assertNotNull("Clear button should exist in landscape", landscapeActivity.findViewById<Button>(R.id.btn_clear))
        assertNotNull("Decimal button should exist in landscape", landscapeActivity.findViewById<Button>(R.id.btn_decimal))
        assertNotNull("Help button should exist in landscape", landscapeActivity.findViewById<Button>(R.id.btn_help))
        assertNotNull("Theme button should exist in landscape", landscapeActivity.findViewById<Button>(R.id.btn_theme))
    }
    
    @Test
    fun calculator_state_should_be_preserved_during_orientation_change() {
        // Enter some numbers and an operation
        val btn2 = activity.findViewById<Button>(R.id.btn_2)
        val btn5 = activity.findViewById<Button>(R.id.btn_5)
        val btnPlus = activity.findViewById<Button>(R.id.btn_plus)
        val btn3 = activity.findViewById<Button>(R.id.btn_3)
        
        btn2.performClick()
        btn5.performClick()
        btnPlus.performClick()
        btn3.performClick()
        
        // Verify state before orientation change
        assertEquals("3", display.text.toString())
        
        // Simulate configuration change (orientation change)
        val bundle = android.os.Bundle()
        activity.onSaveInstanceState(bundle)
        
        // Create new activity instance with saved state (simulating recreation)
        val newController = Robolectric.buildActivity(MainActivity::class.java)
        val newActivity = newController.create(bundle).start().resume().visible().get()
        val newDisplay = newActivity.findViewById<TextView>(R.id.display)
        
        // Verify state is preserved
        assertEquals("3", newDisplay.text.toString())
        
        // Verify calculation can continue
        val newBtnEquals = newActivity.findViewById<Button>(R.id.btn_equals)
        newBtnEquals.performClick()
        
        assertEquals("28", newDisplay.text.toString()) // 25 + 3 = 28
    }
}