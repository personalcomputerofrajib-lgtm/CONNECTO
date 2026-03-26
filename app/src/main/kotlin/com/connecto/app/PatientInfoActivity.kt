package com.connecto.app

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.connecto.app.data.ConnectoDatabase
import com.connecto.app.data.ReportEntity
import com.connecto.app.databinding.ActivityPatientInfoBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class PatientInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPatientInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.parseColor("#00000F")
        binding = ActivityPatientInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val regionCount = intent.getIntExtra("regionCount", 0)
        // No regionCountLabel in HUD, using title or just ignoring

        binding.btnBack.setOnClickListener { finish() }

        binding.btnGenerateReport.setOnClickListener {
            val name   = binding.etName.text.toString().ifBlank { "Unknown" }
            val age    = binding.etAge.text.toString().ifBlank { "—" }
            val gender = binding.spinnerGender.selectedItem.toString()

            lifecycleScope.launch {
                val db = ConnectoDatabase.getInstance(this@PatientInfoActivity)
                val date = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date())
                
                // 1. Create and Save Report
                val reportId = db.reportDao().insert(ReportEntity(
                    patientName = name,
                    patientAge = age,
                    patientGender = gender,
                    totalRegions = regionCount,
                    createdAt = date
                ))

                // 2. Save Selections linked to this report
                SessionManager.currentSelections.forEach { selection ->
                    db.selectionDao().insert(selection.copy(reportId = reportId))
                }

                // 3. Clear session and move to ReportActivity
                val intent = Intent(this@PatientInfoActivity, ReportActivity::class.java).apply {
                    putExtra("reportId", reportId)
                }
                startActivity(intent)
            }
        }
    }
}
