package com.connecto.app.data

import androidx.room.*

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
