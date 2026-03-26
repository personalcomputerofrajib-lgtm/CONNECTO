package com.connecto.app

import com.connecto.app.data.SelectionRecord

/**
 * In-memory session manager to hold current selections between Activities.
 * Replaced by Room DB in a future version for persistence.
 */
object SessionManager {
    var currentSelections: List<SelectionRecord> = emptyList()

    fun setFromDetections(results: List<com.connecto.app.engine.DetectionResult>) {
        currentSelections = results.mapIndexed { idx, r ->
            SelectionRecord(
                reportId = 0L,
                regionId = r.region.id,
                commonName = r.region.commonName,
                medicalName = r.region.medicalName,
                side = r.region.side.name,
                orientation = r.region.orientation.name,
                painType = "",
                duration = "",
                notes = ""
            )
        }
    }

    fun clear() {
        currentSelections = emptyList()
    }
}
