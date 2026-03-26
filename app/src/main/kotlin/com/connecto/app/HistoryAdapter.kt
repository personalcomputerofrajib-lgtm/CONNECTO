package com.connecto.app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.connecto.app.data.ReportLog
import com.connecto.app.databinding.ItemHistoryBinding

class HistoryAdapter(
    private val reports: List<ReportLog>,
    private val onClick: (ReportLog) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val report = reports[position]
        holder.binding.tvDate.text = "SCAN_DATE: ${report.createdAt}"
        holder.binding.tvName.text = "PATIENT: ${report.patientName.uppercase()}"
        holder.binding.tvDetail.text = "NODES_SELECTED: ${report.totalRegions}"
        holder.binding.root.setOnClickListener { onClick(report) }
    }

    override fun getItemCount() = reports.size
}
