package com.connecto.app

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.connecto.app.data.ConnectoDatabase
import com.connecto.app.data.ReportLog
import com.connecto.app.databinding.ActivityHistoryBinding
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.parseColor("#00000F")
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        setupRecyclerView()
        loadReports()
    }

    private fun setupRecyclerView() {
        binding.rvHistory.layoutManager = LinearLayoutManager(this)
    }

    private fun loadReports() {
        lifecycleScope.launch {
            val db = ConnectoDatabase.getInstance(this@HistoryActivity)
            val reports = db.reportDao().getAllReports()
            
            if (reports.isEmpty()) {
                binding.emptyState.visibility = android.view.View.VISIBLE
            } else {
                binding.emptyState.visibility = android.view.View.GONE
                binding.rvHistory.adapter = HistoryAdapter(reports) { report ->
                    val intent = Intent(this@HistoryActivity, ReportActivity::class.java).apply {
                        putExtra("reportId", report.id)
                    }
                    startActivity(intent)
                }
            }
        }
    }
}
