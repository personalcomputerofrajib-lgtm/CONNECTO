package com.connecto.app.data

import androidx.room.*

@Dao
interface SelectionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(selection: SelectionRecord): Long

    @Delete
    fun delete(selection: SelectionRecord)

    @Query("SELECT * FROM selections WHERE reportId = :repId")
    fun getSelectionsForReport(repId: Long): List<SelectionRecord>

    @Query("DELETE FROM selections WHERE reportId = :repId")
    fun clearForReport(repId: Long)
}
