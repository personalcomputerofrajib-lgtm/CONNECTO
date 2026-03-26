package com.connecto.app

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.connecto.app.databinding.ActivityReportBinding
import com.connecto.app.report.ReportGenerator
import com.connecto.app.data.SelectionEntity
import java.text.SimpleDateFormat
import java.util.*

class ReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.parseColor("#00000F")
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name   = intent.getStringExtra("patientName") ?: "Unknown"
        val age    = intent.getStringExtra("patientAge") ?: "—"
        val gender = intent.getStringExtra("patientGender") ?: "—"

        // For now we use stored selections from a shared session object
        // (In full implementation this comes from the database)
        val selections = SessionManager.currentSelections
        val date = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date())

        val report = ReportGenerator.generateStructuredReport(name, age, gender, selections, date)
        binding.reportText.text = report

        binding.btnBack.setOnClickListener { finish() }

        binding.btnShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, report)
                putExtra(Intent.EXTRA_SUBJECT, "CONNECTO Medical Report — $name")
            }
            startActivity(Intent.createChooser(shareIntent, "Share Report via"))
        }

        binding.btnCopy.setOnClickListener {
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as android.content.ClipboardManager
            clipboard.setPrimaryClip(android.content.ClipData.newPlainText("CONNECTO Report", report))
            Toast.makeText(this, "Report copied!", Toast.LENGTH_SHORT).show()
        }
    }
}
