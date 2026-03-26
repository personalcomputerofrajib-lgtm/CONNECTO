package com.connecto.app.data

import androidx.room.*

@Dao
interface SelectionDao {
    @Insert suspend fun insert(selection: SelectionEntity): Long
    @Delete suspend fun delete(selection: SelectionEntity)
    @Query("SELECT * FROM selections WHERE reportId = :reportId")
    suspend fun getSelectionsForReport(reportId: Long): List<SelectionEntity>
    @Query("DELETE FROM selections WHERE reportId = :reportId")
    suspend fun clearForReport(reportId: Long)
}

@Dao
interface ReportDao {
    @Insert suspend fun insert(report: ReportEntity): Long
    @Query("SELECT * FROM reports ORDER BY createdAt DESC")
    suspend fun getAllReports(): List<ReportEntity>
    @Query("SELECT * FROM reports WHERE id = :id")
    suspend fun getReportById(id: Long): ReportEntity?
    @Delete suspend fun delete(report: ReportEntity)
}
