package com.connecto.app.data

import androidx.room.*

@Entity(tableName = "selections")
class SelectionRecord(
    @PrimaryKey(autoGenerate = true) 
    @ColumnInfo(name = "selectionId")
    var selectionId: Long = 0,
    
    @ColumnInfo(name = "reportId")
    var reportId: Long = 0,
    
    @ColumnInfo(name = "regionId")
    var regionId: String = "",
    
    @ColumnInfo(name = "commonName")
    var commonName: String = "",
    
    @ColumnInfo(name = "medicalName")
    var medicalName: String = "",
    
    @ColumnInfo(name = "side")
    var side: String = "",
    
    @ColumnInfo(name = "orientation")
    var orientation: String = "",
    
    @ColumnInfo(name = "painType")
    var painType: String = "",
    
    @ColumnInfo(name = "duration")
    var duration: String = "",
    
    @ColumnInfo(name = "notes")
    var notes: String = ""
)

@Entity(tableName = "reports")
class ReportLog(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "reportId")
    var reportLogId: Long = 0,
    
    @ColumnInfo(name = "patientName")
    var patientName: String = "",
    
    @ColumnInfo(name = "patientAge")
    var patientAge: String = "",
    
    @ColumnInfo(name = "patientGender")
    var patientGender: String = "",
    
    @ColumnInfo(name = "totalRegions")
    var totalRegions: Int = 0,
    
    @ColumnInfo(name = "createdAt")
    var createdAt: String = ""
)

@Dao
interface SelectionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(selection: SelectionRecord): Long

    @Delete
    suspend fun delete(selection: SelectionRecord)

    @Query("SELECT * FROM selections WHERE reportId = :reportId")
    suspend fun getSelectionsForReport(reportId: Long): List<@JvmSuppressWildcards SelectionRecord>

    @Query("DELETE FROM selections WHERE reportId = :reportId")
    suspend fun clearForReport(reportId: Long)
}

@Dao
interface ReportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(report: ReportLog): Long

    @Query("SELECT * FROM reports ORDER BY createdAt DESC")
    suspend fun getAllReports(): List<@JvmSuppressWildcards ReportLog>

    @Query("SELECT * FROM reports WHERE reportId = :id")
    suspend fun getReportById(id: Long): ReportLog?

    @Delete
    suspend fun delete(report: ReportLog)
}
