package com.lawrencevalencia.simplecalculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


private const val STATE_PENDING_OPERATION = "PendingOperation"
private const val STATE_OPERAND1 = "Operand1"
private const val STATE_OPERAND1_STORED = "Operand1_Stored"

class MainActivity : AppCompatActivity() {

    // Variables to hold the operands and type of calculation
    private var operand1: Double? = null
    private var pendingOperation = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Number Listener ----------------------------------------
        //adds the button text to newNumber
        val listener = View.OnClickListener {
            //declared a button instance to contain
            //buttons that triggers the listener
            val numBtn = it as Button
            newNumber.append(numBtn.text)
            if (operation.text == "=") {
                result.setText("")
                operation.text = ""
            }
        }
        //Triggers the listener to append text value to new number
        button0.setOnClickListener(listener)
        button1.setOnClickListener(listener)
        button2.setOnClickListener(listener)
        button3.setOnClickListener(listener)
        button4.setOnClickListener(listener)
        button5.setOnClickListener(listener)
        button6.setOnClickListener(listener)
        button7.setOnClickListener(listener)
        button8.setOnClickListener(listener)
        button9.setOnClickListener(listener)
        buttonDot.setOnClickListener(listener)

        // Operation Listener ----------------------------------------
        val opListener = View.OnClickListener {
            val op = (it as Button).text.toString()
            //filters none numeric format filter
            //try-catch from using newNumber in an operation
            try {
                val value = newNumber.text.toString().toDouble()
                performOperation(value)
            } catch (e: NumberFormatException) {
                newNumber.setText("")
            }
            pendingOperation = op
            operation.text = op
        }

        buttonEquals.setOnClickListener(opListener)
        buttonDivide.setOnClickListener(opListener)
        buttonMultiply.setOnClickListener(opListener)
        buttonMinus.setOnClickListener(opListener)
        buttonPlus.setOnClickListener(opListener)

        //Positive-Negative Button
        buttonNeg.setOnClickListener { view ->
            val value = newNumber.text.toString()
            if (value.isEmpty()) {
                newNumber.setText("-")
            } else {
                //try-catch for convertion
                try {
                    var doubleValue = value.toDouble()
                    doubleValue *= -1
                    newNumber.setText(doubleValue.toString())
                } catch (e: NumberFormatException) {
                    //newNumber was "-" or ".", so clear it
                    newNumber.setText("")
                }
            }
        }

    }

    //Perform Calculations
    private fun performOperation(value: Double) {
        if (operand1 == null) {
            operand1 = value
        } else {
            when (pendingOperation) {
                "=" -> operand1 = value

                "/" -> operand1 = if (value == 0.0) {
                    Double.NaN   // handle attempt to divide by zero
                } else {
                    operand1!! / value
                }
                "*" -> operand1 = operand1!! * value
                "-" -> operand1 = operand1!! - value
                "+" -> operand1 = operand1!! + value
            }
        }
        result.setText(operand1.toString())
        newNumber.setText("")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (operand1 != null) {
            //save result value and flag
            outState.putDouble(STATE_OPERAND1, operand1!!)
            outState.putBoolean(STATE_OPERAND1_STORED, true)
        }
        //save pending operation - mandatory
        outState.putString(STATE_PENDING_OPERATION, pendingOperation)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        //load saved result number(operand 1)
        operand1 = if (savedInstanceState.getBoolean(STATE_OPERAND1_STORED, false)) {
            //First statement is always if-condition is TRUE
            //(stored=True) = True, (stored=Null) = False(default)
            savedInstanceState.getDouble(STATE_OPERAND1)
        } else {
            null
        }
        //load operation to internal variable and view text
        pendingOperation = savedInstanceState.getString(STATE_PENDING_OPERATION, "=")
        operation.text = pendingOperation
    }
}