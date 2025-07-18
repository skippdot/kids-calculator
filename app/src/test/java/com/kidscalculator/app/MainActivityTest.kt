package com.kidscalculator.app

import android.widget.Button
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class MainActivityTest {
    
    private lateinit var activity: MainActivity
    private lateinit var display: TextView
    
    @Before
    fun setup() {
        activity = Robolectric.buildActivity(MainActivity::class.java)
            .create()
            .resume()
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
    fun addition_operation_should_work() {
        val btn2 = activity.findViewById<Button>(R.id.btn_2)
        val btnPlus = activity.findViewById<Button>(R.id.btn_plus)
        val btn3 = activity.findViewById<Button>(R.id.btn_3)
        val btnEquals = activity.findViewById<Button>(R.id.btn_equals)
        
        btn2.performClick()
        btnPlus.performClick()
        btn3.performClick()
        btnEquals.performClick()
        
        assertEquals("5", display.text.toString())
    }
    
    @Test
    fun subtraction_operation_should_work() {
        val btn1 = activity.findViewById<Button>(R.id.btn_1)
        val btn0 = activity.findViewById<Button>(R.id.btn_0)
        val btnMinus = activity.findViewById<Button>(R.id.btn_minus)
        val btn3 = activity.findViewById<Button>(R.id.btn_3)
        val btnEquals = activity.findViewById<Button>(R.id.btn_equals)
        
        btn1.performClick()
        btn0.performClick()
        btnMinus.performClick()
        btn3.performClick()
        btnEquals.performClick()
        
        assertEquals("7", display.text.toString())
    }
    
    @Test
    fun multiplication_operation_should_work() {
        val btn4 = activity.findViewById<Button>(R.id.btn_4)
        val btnMultiply = activity.findViewById<Button>(R.id.btn_multiply)
        val btn3 = activity.findViewById<Button>(R.id.btn_3)
        val btnEquals = activity.findViewById<Button>(R.id.btn_equals)
        
        btn4.performClick()
        btnMultiply.performClick()
        btn3.performClick()
        btnEquals.performClick()
        
        assertEquals("12", display.text.toString())
    }
    
    @Test
    fun division_operation_should_work() {
        val btn8 = activity.findViewById<Button>(R.id.btn_8)
        val btnDivide = activity.findViewById<Button>(R.id.btn_divide)
        val btn2 = activity.findViewById<Button>(R.id.btn_2)
        val btnEquals = activity.findViewById<Button>(R.id.btn_equals)
        
        btn8.performClick()
        btnDivide.performClick()
        btn2.performClick()
        btnEquals.performClick()
        
        assertEquals("4", display.text.toString())
    }
    
    @Test
    fun clear_button_should_reset_display() {
        val btn5 = activity.findViewById<Button>(R.id.btn_5)
        val btn3 = activity.findViewById<Button>(R.id.btn_3)
        val btnClear = activity.findViewById<Button>(R.id.btn_clear)
        
        btn5.performClick()
        btn3.performClick()
        assertEquals("53", display.text.toString())
        
        btnClear.performClick()
        assertEquals("0", display.text.toString())
    }
    
    @Test
    fun chained_operations_should_work() {
        val btn2 = activity.findViewById<Button>(R.id.btn_2)
        val btnPlus = activity.findViewById<Button>(R.id.btn_plus)
        val btn3 = activity.findViewById<Button>(R.id.btn_3)
        val btnMultiply = activity.findViewById<Button>(R.id.btn_multiply)
        val btn4 = activity.findViewById<Button>(R.id.btn_4)
        val btnEquals = activity.findViewById<Button>(R.id.btn_equals)
        
        btn2.performClick()
        btnPlus.performClick()
        btn3.performClick()
        btnMultiply.performClick() // Should calculate 2+3=5 first
        btn4.performClick()
        btnEquals.performClick() // Should calculate 5*4=20
        
        assertEquals("20", display.text.toString())
    }
    
    @Test
    fun division_by_zero_should_be_handled() {
        val btn5 = activity.findViewById<Button>(R.id.btn_5)
        val btnDivide = activity.findViewById<Button>(R.id.btn_divide)
        val btn0 = activity.findViewById<Button>(R.id.btn_0)
        val btnEquals = activity.findViewById<Button>(R.id.btn_equals)
        
        btn5.performClick()
        btnDivide.performClick()
        btn0.performClick()
        btnEquals.performClick()
        
        assertEquals("0", display.text.toString())
    }
}