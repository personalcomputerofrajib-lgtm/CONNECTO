package com.connecto.app.data

import androidx.room.*

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
