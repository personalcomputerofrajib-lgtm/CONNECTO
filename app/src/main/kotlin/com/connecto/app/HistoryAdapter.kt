package com.connecto.app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.connecto.app.data.ReportEntity
import com.connecto.app.databinding.ItemHistoryBinding

class HistoryAdapter(
    private val reports: List<ReportEntity>,
    private val onClick: (ReportEntity) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val report = reports[position]
        holder.binding.tvPatientName.text = report.patientName
        holder.binding.tvDate.text = "${report.createdAt} · ${report.totalRegions} region(s)"
        holder.binding.root.setOnClickListener { onClick(report) }
    }

    override fun getItemCount() = reports.size
}
