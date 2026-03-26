package com.connecto.app.data

import androidx.room.*

@Dao
interface ReportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(report: ReportLog): Long

    @Query("SELECT * FROM reports ORDER BY createdAt DESC")
    fun getAllReports(): List<ReportLog>

    @Query("SELECT * FROM reports WHERE reportId = :rId")
    fun getReportById(rId: Long): ReportLog?

    @Delete
    fun delete(report: ReportLog)
}
