package com.connecto.app

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.connecto.app.data.ConnectoDatabase
import com.connecto.app.databinding.ActivityReportBinding
import com.connecto.app.report.ReportGenerator
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.parseColor("#00000F")
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val reportId = intent.getLongExtra("reportId", -1L)
        if (reportId == -1L) {
            Toast.makeText(this, "Error loading report", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        lifecycleScope.launch {
            val db = ConnectoDatabase.getInstance(this@ReportActivity)
            val reportEntity = db.reportDao().getReportById(reportId) 
            val selections = db.selectionDao().getSelectionsForReport(reportId)

            if (reportEntity == null) {
                Toast.makeText(this@ReportActivity, "Report not found", Toast.LENGTH_SHORT).show()
                finish()
                return@launch
            }

            val name = reportEntity.patientName
            val age = reportEntity.patientAge
            val gender = reportEntity.patientGender
            val date = reportEntity.createdAt

            val reportText = ReportGenerator.generateStructuredReport(name, age, gender, selections, date)
            binding.reportText.text = reportText

            binding.btnShare.setOnClickListener {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, reportText)
                    putExtra(Intent.EXTRA_SUBJECT, "CONNECTO Medical Report — $name")
                }
                startActivity(Intent.createChooser(shareIntent, "Share Report via"))
            }

            binding.btnCopy.setOnClickListener {
                val clipboard = getSystemService(CLIPBOARD_SERVICE) as android.content.ClipboardManager
                clipboard.setPrimaryClip(android.content.ClipData.newPlainText("CONNECTO Report", reportText))
                Toast.makeText(this@ReportActivity, "Report copied!", Toast.LENGTH_SHORT).show()
            }

            binding.btnPdf.setOnClickListener {
                val path = com.connecto.app.report.PdfExporter.exportToPdf(this@ReportActivity, reportText, name)
                if (path != null) {
                    Toast.makeText(this@ReportActivity, "PDF saved: $path", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@ReportActivity, "PDF export failed. Check permissions.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnBack.setOnClickListener { finish() }
    }
}
