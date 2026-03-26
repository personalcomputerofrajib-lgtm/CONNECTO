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
) {
    fun copy(
        selectionId: Long = this.selectionId,
        reportId: Long = this.reportId,
        regionId: String = this.regionId,
        commonName: String = this.commonName,
        medicalName: String = this.medicalName,
        side: String = this.side,
        orientation: String = this.orientation,
        painType: String = this.painType,
        duration: String = this.duration,
        notes: String = this.notes
    ): SelectionRecord {
        return SelectionRecord(
            selectionId = selectionId,
            reportId = reportId,
            regionId = regionId,
            commonName = commonName,
            medicalName = medicalName,
            side = side,
            orientation = orientation,
            painType = painType,
            duration = duration,
            notes = notes
        )
    }
}
