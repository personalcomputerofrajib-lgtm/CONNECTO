package com.connecto.app

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.connecto.app.databinding.ActivityPatientInfoBinding

class PatientInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPatientInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.parseColor("#00000F")
        binding = ActivityPatientInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val regionCount = intent.getIntExtra("regionCount", 0)
        binding.regionCountLabel.text = "$regionCount region(s) selected"

        binding.btnBack.setOnClickListener { finish() }

        binding.btnGenerateReport.setOnClickListener {
            val name   = binding.etName.text.toString().ifBlank { "Unknown" }
            val age    = binding.etAge.text.toString().ifBlank { "—" }
            val gender = binding.spinnerGender.selectedItem.toString()

            val intent = Intent(this, ReportActivity::class.java).apply {
                putExtra("patientName", name)
                putExtra("patientAge", age)
                putExtra("patientGender", gender)
                putExtra("regionCount", regionCount)
            }
            startActivity(intent)
        }
    }
}
