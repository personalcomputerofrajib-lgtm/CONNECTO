package com.connecto.app.data

import androidx.room.*

/**
 * ENTITIES
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
    @ColumnInfo(name = "painType") val painType: String = "",
    @ColumnInfo(name = "duration") val duration: String = "",
    @ColumnInfo(name = "notes") val notes: String = ""
)

@Entity(tableName = "reports")
data class ReportEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val patientName: String,
    @ColumnInfo(defaultValue = "0") val patientAge: String,
    val patientGender: String,
    @ColumnInfo(defaultValue = "0") val totalRegions: Int,
    val createdAt: String
)

/**
 * DAOS
 */

@Dao
interface SelectionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(selection: SelectionEntity): Long

    @Delete
    suspend fun delete(selection: SelectionEntity)

    @Query("SELECT * FROM selections WHERE reportId = :reportId")
    suspend fun getSelectionsForReport(reportId: Long): List<SelectionEntity>

    @Query("DELETE FROM selections WHERE reportId = :reportId")
    suspend fun clearForReport(reportId: Long)
}

@Dao
interface ReportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(report: ReportEntity): Long

    @Query("SELECT * FROM reports ORDER BY createdAt DESC")
    suspend fun getAllReports(): List<ReportEntity>

    @Query("SELECT * FROM reports WHERE id = :id")
    suspend fun getReportById(id: Long): ReportEntity?

    @Delete
    suspend fun delete(report: ReportEntity)
}
