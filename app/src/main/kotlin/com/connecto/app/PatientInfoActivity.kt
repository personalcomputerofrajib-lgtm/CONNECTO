package com.connecto.app

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.connecto.app.data.ConnectoDatabase
import com.connecto.app.data.ReportLog
import com.connecto.app.databinding.ActivityPatientInfoBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
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
                
                withContext(Dispatchers.IO) {
                    // 1. Create and Save Report
                    val reportId = db.reportDao().insert(ReportLog(
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

                    // 3. Move to ReportActivity (on main thread)
                    withContext(Dispatchers.Main) {
                        val intent = Intent(this@PatientInfoActivity, ReportActivity::class.java).apply {
                            putExtra("reportId", reportId)
                        }
                        startActivity(intent)
                    }
                }
            }
        }
    }
}
