package com.connecto.app.data

import androidx.room.*

@Dao
interface ReportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(report: ReportLog): Long

    @Query("SELECT * FROM reports ORDER BY createdAt DESC")
    suspend fun getAllReports(): List<ReportLog>

    @Query("SELECT * FROM reports WHERE reportId = :id")
    suspend fun getReportById(id: Long): ReportLog?

    @Delete
    suspend fun delete(report: ReportLog)
}
