package com.connecto.app

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.connecto.app.databinding.ActivityMainBinding
import com.connecto.app.engine.*
import com.connecto.app.ui.BodyMapView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        )
        window.statusBarColor = Color.TRANSPARENT

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBodyMap()
        setupControls()
    }

    private fun setupBodyMap() {
        binding.bodyMapView.onRegionSelected = { results ->
            if (results.isNotEmpty()) {
                val top = results.first()
                binding.regionLabel.text = top.region.commonName
                binding.regionMedical.text = top.region.medicalName
                binding.regionSide.text = "${top.region.side} · ${top.region.orientation}"
                binding.selectionCount.text = "${results.size} area(s) selected"
                binding.regionInfoCard.visibility = android.view.View.VISIBLE

                // Show pain detail dialog for the top selection
                com.connecto.app.ui.PainDetailDialog(
                    this,
                    top.region.commonName
                ) { painType, duration, notes ->
                    SessionManager.setFromDetections(results)
                    // Update pain details on first selection
                    val updated = SessionManager.currentSelections.toMutableList()
                    if (updated.isNotEmpty()) {
                        updated[0] = updated[0].copy(
                            painType = painType,
                            duration = duration,
                            notes = notes
                        )
                        SessionManager.currentSelections = updated
                    }
                }.show()
            } else {
                binding.regionInfoCard.visibility = android.view.View.GONE
            }
        }
    }

    private fun setupControls() {
        // Toggle front/back view
        binding.btnToggleView.setOnClickListener {
            if (binding.bodyMapView.currentOrientation == Orientation.ANTERIOR) {
                binding.bodyMapView.currentOrientation = Orientation.POSTERIOR
                binding.btnToggleView.text = "FRONT"
            } else {
                binding.bodyMapView.currentOrientation = Orientation.ANTERIOR
                binding.btnToggleView.text = "BACK"
            }
        }

        // Tap mode
        binding.btnTap.setOnClickListener {
            binding.bodyMapView.interactionMode = BodyMapView.InteractionMode.TAP
            highlightMode(0)
        }

        // Paint mode
        binding.btnPaint.setOnClickListener {
            binding.bodyMapView.interactionMode = BodyMapView.InteractionMode.PAINT
            highlightMode(1)
        }

        // Multi-select mode
        binding.btnMulti.setOnClickListener {
            binding.bodyMapView.interactionMode = BodyMapView.InteractionMode.MULTI_SELECT
            highlightMode(2)
        }

        // Clear
        binding.btnClear.setOnClickListener {
            binding.bodyMapView.clearSelections()
            binding.regionInfoCard.visibility = View.GONE
        }

        // Undo
        binding.btnUndo.setOnClickListener {
            binding.bodyMapView.undoLastSelection()
            val count = binding.bodyMapView.getSelections().size
            binding.selectionCount.text = "$count area(s) selected"
            if (count == 0) binding.regionInfoCard.visibility = View.GONE
        }

        // Generate Report
        binding.btnReport.setOnClickListener {
            val selections = binding.bodyMapView.getSelections()
            if (selections.isEmpty()) {
                android.widget.Toast.makeText(this, "Please select at least one area.", android.widget.Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            SessionManager.setFromDetections(selections)
            val intent = android.content.Intent(this, PatientInfoActivity::class.java).apply {
                putExtra("regionCount", selections.size)
            }
            startActivity(intent)
        }

        binding.btnHistory.setOnClickListener {
            startActivity(android.content.Intent(this, HistoryActivity::class.java))
        }
    }

    private fun highlightMode(active: Int) {
        val activeColor = Color.parseColor("#00FFCC")
        val inactiveColor = Color.parseColor("#333355")
        binding.btnTap.setBackgroundColor(if (active == 0) activeColor else inactiveColor)
        binding.btnPaint.setBackgroundColor(if (active == 1) activeColor else inactiveColor)
        binding.btnMulti.setBackgroundColor(if (active == 2) activeColor else inactiveColor)
    }
}
