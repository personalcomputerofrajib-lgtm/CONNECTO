package com.connecto.app.data

import androidx.room.*

@Entity(tableName = "selections")
data class SelectionRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val reportId: Long = 0,
    val regionId: String = "",
    val commonName: String = "",
    val medicalName: String = "",
    val side: String = "",
    val orientation: String = "",
    val painType: String = "",
    val duration: String = "",
    val notes: String = ""
)

@Entity(tableName = "reports")
data class ReportLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val patientName: String = "",
    val patientAge: String = "",
    val patientGender: String = "",
    val totalRegions: Int = 0,
    val createdAt: String = ""
)

@Dao
interface SelectionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(selection: SelectionRecord): Long

    @Delete
    suspend fun delete(selection: SelectionRecord)

    @Query("SELECT * FROM selections WHERE reportId = :reportId")
    suspend fun getSelectionsForReport(reportId: Long): List<SelectionRecord>

    @Query("DELETE FROM selections WHERE reportId = :reportId")
    suspend fun clearForReport(reportId: Long)
}

@Dao
interface ReportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(report: ReportLog): Long

    @Query("SELECT * FROM reports ORDER BY createdAt DESC")
    suspend fun getAllReports(): List<ReportLog>

    @Query("SELECT * FROM reports WHERE id = :id")
    suspend fun getReportById(id: Long): ReportLog?

    @Delete
    suspend fun delete(report: ReportLog)
}
