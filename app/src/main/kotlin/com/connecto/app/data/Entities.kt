package com.connecto.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a single selected body region annotation (stored in Room DB).
 */
@Entity(tableName = "selections")
data class SelectionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val reportId: Long,
    val regionId: String,
    val commonName: String,
    val medicalName: String,
    val side: String,
    val orientation: String,
    val painType: String = "",     // sharp / dull / burning / aching
    val duration: String = "",     // e.g., "2 days"
    val notes: String = ""
)

/**
 * Represents a saved medical report (stored in Room DB).
 */
@Entity(tableName = "reports")
data class ReportEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val patientName: String,
    val patientAge: String,
    val patientGender: String,
    val createdAt: Long = System.currentTimeMillis()
)
