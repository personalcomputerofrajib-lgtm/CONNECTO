package com.connecto.app.engine

import android.graphics.PointF
import kotlin.math.abs

/**
 * The Anatomical Mapping Engine.
 * Converts a normalized (x, y) touch point into a DetectionResult.
 */
object AnatomicalEngine {

    /**
     * Detect region for a single tap point.
     * @param nx Normalized X (0.0 to 1.0)
     * @param ny Normalized Y (0.0 to 1.0)
     * @param orientation Front or back view
     */
    fun detectRegion(nx: Float, ny: Float, orientation: Orientation): DetectionResult? {
        val regions = BodyMap.getRegions(orientation)
        for (region in regions) {
            if (isPointInPolygon(nx, ny, region.polygon)) {
                return DetectionResult(region, nx, ny)
            }
        }
        return findNearestRegion(nx, ny, regions)
    }

    /**
     * Detect the dominant region from a painted area (list of points).
     */
    fun detectFromPaint(points: List<PointF>, orientation: Orientation): List<DetectionResult> {
        val hitMap = mutableMapOf<String, Int>() // regionId -> hitCount
        val regions = BodyMap.getRegions(orientation)

        for (p in points) {
            for (region in regions) {
                if (isPointInPolygon(p.x, p.y, region.polygon)) {
                    hitMap[region.id] = (hitMap[region.id] ?: 0) + 1
                    break
                }
            }
        }

        // Sort by hit count descending
        val sorted = hitMap.entries.sortedByDescending { it.value }

        return sorted.mapNotNull { entry ->
            val region = regions.find { it.id == entry.key }
            region?.let { DetectionResult(it, 0f, 0f) }
        }
    }

    /**
     * Point-in-polygon test using ray casting algorithm.
     */
    fun isPointInPolygon(x: Float, y: Float, polygon: List<Pair<Float, Float>>): Boolean {
        var inside = false
        var j = polygon.size - 1
        for (i in polygon.indices) {
            val xi = polygon[i].first
            val yi = polygon[i].second
            val xj = polygon[j].first
            val yj = polygon[j].second

            val intersect = ((yi > y) != (yj > y)) &&
                    (x < (xj - xi) * (y - yi) / (yj - yi) + xi)
            if (intersect) inside = !inside
            j = i
        }
        return inside
    }

    /**
     * Snap to the nearest region if exact match not found.
     */
    private fun findNearestRegion(nx: Float, ny: Float, regions: List<BodyRegion>): DetectionResult? {
        var minDist = Float.MAX_VALUE
        var nearest: BodyRegion? = null

        for (region in regions) {
            val centroid = getCentroid(region.polygon)
            val dx = nx - centroid.first
            val dy = ny - centroid.second
            val dist = dx * dx + dy * dy
            if (dist < minDist) {
                minDist = dist
                nearest = region
            }
        }
        return nearest?.let { DetectionResult(it, nx, ny) }
    }

    private fun getCentroid(polygon: List<Pair<Float, Float>>): Pair<Float, Float> {
        val cx = polygon.map { it.first }.average().toFloat()
        val cy = polygon.map { it.second }.average().toFloat()
        return cx to cy
    }

    /**
     * Generate a human-readable description for a detection result.
     */
    fun generateDescription(result: DetectionResult): String {
        val r = result.region
        return "${r.side.name.lowercase().replaceFirstChar { it.uppercase() }} ${r.commonName} " +
               "(${r.orientation.name.lowercase().replaceFirstChar { it.uppercase() }}) — " +
               "Medical: ${r.medicalName}"
    }
}
