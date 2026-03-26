package com.connecto.app.ui

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import android.widget.*
import com.connecto.app.R

/**
 * Dialog for entering pain details after selecting a body region.
 * Captures: pain type, duration, and notes.
 */
class PainDetailDialog(
    context: Context,
    private val regionName: String,
    private val onSave: (painType: String, duration: String, notes: String) -> Unit
) : Dialog(context) {

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.dialog_pain_detail)

        // Set region name
        findViewById<TextView>(R.id.dialogRegionName).text = regionName

        val spinnerPain = findViewById<Spinner>(R.id.spinnerPainType)
        val etDuration  = findViewById<EditText>(R.id.etDuration)
        val etNotes     = findViewById<EditText>(R.id.etNotes)
        val btnSave     = findViewById<Button>(R.id.btnSavePain)
        val btnCancel   = findViewById<Button>(R.id.btnCancelPain)

        val painTypes = arrayOf("Sharp", "Dull", "Burning", "Aching", "Throbbing", "Stabbing", "Cramping", "Tingling")
        spinnerPain.adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, painTypes)

        btnSave.setOnClickListener {
            val painType = spinnerPain.selectedItem.toString()
            val duration = etDuration.text.toString().trim()
            val notes    = etNotes.text.toString().trim()
            onSave(painType, duration, notes)
            dismiss()
        }

        btnCancel.setOnClickListener { dismiss() }
    }
}
